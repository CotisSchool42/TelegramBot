package bot.inlineKeyboards;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class ProductKeyboard {
    private String productName;
    private Integer numberProductsInBasket;

    public ProductKeyboard(String productName, Integer numberProductsInBasket) {
        this.productName = productName;
        this.numberProductsInBasket = numberProductsInBasket;
    }

    public InlineKeyboardMarkup getInlineMessageButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton inlinePlus = new InlineKeyboardButton();
        InlineKeyboardButton inlineMinus = new InlineKeyboardButton();
        InlineKeyboardButton inlineNumber = new InlineKeyboardButton();
        InlineKeyboardButton inlineCatalog = new InlineKeyboardButton();
        InlineKeyboardButton inlineCard = new InlineKeyboardButton();

        //➖➕
        inlineMinus.setText("Drop ➖");
        inlineNumber.setText("Pcs. " + numberProductsInBasket);
        inlinePlus.setText("Add ➕");
        inlineCatalog.setText("Categories \uD83C\uDF52");
        inlineCard.setText("Show bucket \uD83D\uDECD");

        inlineMinus.setCallbackData("-" + productName);
        inlineNumber.setCallbackData(productName + "1");
        inlinePlus.setCallbackData("+" + productName);
        inlineCatalog.setCallbackData("categories");
        inlineCard.setCallbackData("Show bucket");

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();

        row1.add(inlineMinus);
        row1.add(inlineNumber);
        row1.add(inlinePlus);
        row2.add(inlineCatalog);
        row2.add(inlineCard);

        rowList.add(row1);
        rowList.add(row2);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }
}
