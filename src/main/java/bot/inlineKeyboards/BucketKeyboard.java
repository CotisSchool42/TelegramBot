package bot.inlineKeyboards;

import bot.entities.Product;
import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

/*
public class BucketKeyboard {
    public InlineKeyboardMarkup getInlineMessageButtons(int size, int total, int numberProduct, Product product) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton inlineLast = new InlineKeyboardButton();
        InlineKeyboardButton inlinePrevious = new InlineKeyboardButton();
        InlineKeyboardButton inlineTotal = new InlineKeyboardButton();

        InlineKeyboardButton inlinePlus = new InlineKeyboardButton();
        InlineKeyboardButton inlineMinus = new InlineKeyboardButton();
        InlineKeyboardButton inlineNumber = new InlineKeyboardButton();

        InlineKeyboardButton inlineCatalog = new InlineKeyboardButton();

        InlineKeyboardButton inlinePurchase = new InlineKeyboardButton();


        inlinePrevious.setText("\uD83D\uDD19");
        inlineTotal.setText(total + 1 + "/" + size);
        inlineLast.setText("\uD83D\uDD1C");

        inlineMinus.setText("Drop ➖");
        inlineNumber.setText("Pcs. " + numberProduct);
        inlinePlus.setText("Add ➕");

        inlineCatalog.setText("Categories \uD83E\uDE84");
        inlinePurchase.setText("Checkout now ✅");

        inlinePrevious.setCallbackData("last" + total);
        inlineLast.setCallbackData("next" + total);
        inlineTotal.setCallbackData("total");

        inlineMinus.setCallbackData(">" + product.getName());
        inlineNumber.setCallbackData("1");
        inlinePlus.setCallbackData("<" +  product.getName());

        inlineCatalog.setCallbackData("categories");
        inlinePurchase.setCallbackData("Checkout now ✅");

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();

        row1.add(inlinePrevious);
        row1.add(inlineTotal);
        row1.add(inlineLast);
        row2.add(inlineMinus);
        row2.add(inlineNumber);
        row2.add(inlinePlus);
        row3.add(inlineCatalog);
        row3.add(inlinePurchase);

        rowList.add(row1);
        rowList.add(row2);
        rowList.add(row3);


        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }
}*/


public class BucketKeyboard {
    private final int totalProdInCard;
    private final int currentProd;
    private final int totalOfCurrentProd;
    private final Product product;

    public BucketKeyboard(int totalProdInCard, int currentProd, int totalOfCurrentProd, Product product) {
        this.totalProdInCard = totalProdInCard;
        this.currentProd = currentProd;
        this.totalOfCurrentProd = totalOfCurrentProd;
        this.product = product;
    }

    public InlineKeyboardMarkup getInlineMessageButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton inlineLast = new InlineKeyboardButton();
        InlineKeyboardButton inlinePrevious = new InlineKeyboardButton();
        InlineKeyboardButton inlineTotal = new InlineKeyboardButton();

        InlineKeyboardButton inlinePlus = new InlineKeyboardButton();
        InlineKeyboardButton inlineMinus = new InlineKeyboardButton();
        InlineKeyboardButton inlineNumber = new InlineKeyboardButton();

        InlineKeyboardButton inlineCatalog = new InlineKeyboardButton();
        InlineKeyboardButton inlinePurchase = new InlineKeyboardButton();

        inlinePrevious.setText("\uD83D\uDD19");
        inlineTotal.setText(currentProd + 1 + "/" + totalProdInCard);
        inlineLast.setText("\uD83D\uDD1C");

        inlineMinus.setText("Drop ➖");
        inlineNumber.setText("Pcs. " + totalOfCurrentProd);
        inlinePlus.setText("Add ➕");

        inlineCatalog.setText("Categories \uD83C\uDF52");
        inlinePurchase.setText("Checkout now ✅");

        inlinePrevious.setCallbackData("last" + currentProd);
        inlineLast.setCallbackData("next" + currentProd);
        inlineTotal.setCallbackData("total");

        inlineMinus.setCallbackData(">" + product.getName());
        inlineNumber.setCallbackData("1");
        inlinePlus.setCallbackData("<" +  product.getName());

        inlineCatalog.setCallbackData("categories");
        inlinePurchase.setCallbackData("Checkout now ✅");

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();

        row1.add(inlinePrevious);
        row1.add(inlineTotal);
        row1.add(inlineLast);
        row2.add(inlineMinus);
        row2.add(inlineNumber);
        row2.add(inlinePlus);
        row3.add(inlineCatalog);
        row3.add(inlinePurchase);

        rowList.add(row1);
        rowList.add(row2);
        rowList.add(row3);

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }
}


