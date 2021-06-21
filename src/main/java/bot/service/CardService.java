package bot.service;

import bot.dao.ProductsDao;
import bot.entities.Product;

import bot.inlineKeyboards.BucketKeyboard;
import bot.inlineKeyboards.ProductKeyboard;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

@Slf4j
@Service
public class CardService extends ProductService {
    private final ProductsDao productsDao;
    private final Map<Long, List<Product>> card = getCard();

    public CardService(ProductsDao productsDao) {
        this.productsDao = productsDao;
    }

    public void addToCard(Long userId, String data) {
        for (Product product : productsDao.index()) {
            if (product.getName().equals(data))
                card.computeIfAbsent(userId, k -> new ArrayList<>()).add(product);
        }
    }

    public Integer numberProductsInBasket(String productName, long userId) {
        int i = 0;

        try {
            List<Product> list = card.get(userId);

            for (Product product : list) {
                if (product.getName().equals(productName))
                    i++;
            }
            return i;
        } catch (NullPointerException e) {
            return i;
        }
    }

    public void dropFromCard(Long userId, String data) {
        List<Product> list = card.get(userId);

        for (Product product : list) {
            if (product.getName().equals(data)) {
                list.remove(product);
                break;
            }
        }
        card.put(userId, list);

        if (list.isEmpty())
            card.remove(userId);
    }

    public float productTotalPrice(List<Product> products) {
        float price = 0;
        for (Product product : products)
            price += product.getPrice();
        return price;
    }

    public EditMessageMedia editBucket(CallbackQuery buttonQuery) throws IOException {
        final long chatId = buttonQuery.getMessage().getChatId();
        final int messageId = buttonQuery.getMessage().getMessageId();
        final String inlineMessageId = buttonQuery.getInlineMessageId();
        final long userId = buttonQuery.getFrom().getId();
        final String data = buttonQuery.getData();

        Set<Product> setProducts = new LinkedHashSet<>(card.get(userId));
        List<Product> products = new ArrayList<>(setProducts);

        int currentProd = 0;
        Product product;

        try {
            if (data.startsWith(">")) {
                addOrDeleteProdFromBucket(userId, data);
                products = card.get(userId);
                product = products.get(0);
            } else if (data.startsWith("<")) {
                addOrDeleteProdFromBucket(userId, data);
                products = card.get(userId);
                product = products.get(0);
            } else if (data.startsWith("next")) {
                currentProd = Integer.parseInt(data.substring(4));
                try {
                    product = products.get(++currentProd);
                } catch (IndexOutOfBoundsException e) {
                    currentProd = 0;
                    product = products.get(0);
                }
            } else if (data.startsWith("last")) {
                currentProd = Integer.parseInt(data.substring(4));
                try {
                    product = products.get(--currentProd);
                } catch (IndexOutOfBoundsException e) {
                    currentProd = 0;
                    product = products.get(0);
                }
            } else
                product = products.get(0);
        } catch (NullPointerException e) {
            log.info("NullPointerException");
            return null;
        }

        int totalOfCurrentProd = numberProductsInBasket(product.getName(), userId);
        BucketKeyboard bucketKeyboard = new BucketKeyboard(setProducts.size(), currentProd, totalOfCurrentProd, product);

        InputStream url = new URL("file://" + product.getPhoto_url()).openStream();
        InputMediaPhoto inputMediaPhoto = new InputMediaPhoto();
        inputMediaPhoto.setMedia(url, product.getPhoto_url());
        inputMediaPhoto.setCaption(product.getName() + "\n\n" + product.getProductDescription() + "\n\n" + "Price: " + product.getPrice()
                + "\uD83D\uDCB2" + "\n" + "Total price: " + productTotalPrice(card.get(userId)) + "\uD83D\uDCB2");

        return new EditMessageMedia(String.valueOf(chatId), messageId, inlineMessageId, inputMediaPhoto, bucketKeyboard.getInlineMessageButtons());
    }

    public Boolean bucketIsEmpty(long userId) {
        List<Product> products = card.get(userId);
        return products == null;
    }

    public SendPhoto showBucket(CallbackQuery buttonQuery) throws IOException {
        final long chatId = buttonQuery.getMessage().getChatId();
        final long userId = buttonQuery.getFrom().getId();

        Set<Product> setProducts = new LinkedHashSet<>(card.get(userId));
        List<Product> products = new ArrayList<>(setProducts);
        Product product = products.get(0);

        int totalOfCurrentProd = numberProductsInBasket(product.getName(), userId);
        BucketKeyboard bucketKeyboard = new BucketKeyboard(products.size(), 0, totalOfCurrentProd, product);

        File image = ResourceUtils.getFile(product.getPhoto_url());
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setReplyMarkup(bucketKeyboard.getInlineMessageButtons());
        sendPhoto.setChatId(String.valueOf(chatId));
        sendPhoto.setCaption(product.getName() + "\n\n" + product.getProductDescription() + "\n\n" + "Price: "
                + product.getPrice() + "\uD83D\uDCB2" + "\n" + "Total price: " + productTotalPrice(card.get(userId)) + "\uD83D\uDCB2");
        InputFile imag = new InputFile().setMedia(image);
        sendPhoto.setPhoto(imag);

        return sendPhoto;
    }

    public int addOrDeleteProdFromBucket(Long userId, String data) {
        int numberOfProduct = numberProductsInBasket(data.substring(1), userId);

        if (!card.containsKey(userId)) {
            addToCard(userId, data.substring(1));
            numberOfProduct++;
        } else if (data.charAt(0) == '+' || data.startsWith("<")) {
            addToCard(userId, data.substring(1));
            numberOfProduct++;
        } else if ((data.charAt(0) == '-' || data.startsWith(">")) && numberOfProduct != 0) {
            dropFromCard(userId, data.substring(1));
            numberOfProduct--;
        }
        return numberOfProduct;
    }


    public InlineKeyboardMarkup editInlineMessageButtons(Long userId, String data) {
        ProductKeyboard productKeyboard = new ProductKeyboard(data.substring(1), addOrDeleteProdFromBucket(userId, data));
        return productKeyboard.getInlineMessageButtons();
    }
}


/** Ниже представлен пример корзины без фотографий. Отлично подойдет для магазина услуг */

/*    public SendMessage showCard(Long userId) {
        List<Product> products = card.get(userId);

        StringBuilder showCard = new StringBuilder("Bucket: \n\n");

        if (products != null) {
            for (Product product : products) {
                showCard.append(product.getName()).append("\n").append("Price: ").append(product.getPrice())
                .append("\n").append(product.getProduct_description()).append("\n\n");
            }

            card.forEach((k, v) -> System.out.println(v));
            SendMessage replyToUser = new SendMessage(String.valueOf(userId), showCard.toString());
            replyToUser.setReplyMarkup(getInlineMessageButtons(10));
            return replyToUser;
        } else {
            return new SendMessage(String.valueOf(userId), "Bucket is empty");
        }
    }*/