package bot.service;

import bot.dao.CategoriesDao;
import bot.entities.Category;
import org.apache.commons.collections4.ListUtils;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService extends ProductService {
    private final CategoriesDao categoriesDao;

    public CategoryService(CategoriesDao categoriesDao) {
        this.categoriesDao = categoriesDao;
    }

    public InlineKeyboardMarkup getInlineMessageButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<Category> categoryList = categoriesDao.index();
        List<InlineKeyboardButton> listInline = new ArrayList<>();

        for (Category category : categoryList) {
            InlineKeyboardButton inline = new InlineKeyboardButton(category.getName());
            inline.setCallbackData(category.getName());
            listInline.add(inline);
        }

        List<List<InlineKeyboardButton>> rowList = ListUtils.partition(listInline, 2);
        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }
}
