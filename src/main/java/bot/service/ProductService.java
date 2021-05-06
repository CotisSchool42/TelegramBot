package bot.service;

import bot.dao.CategoriesDao;
import bot.dao.ProductsDao;
import bot.entities.Category;
import bot.entities.Product;
import bot.inlineKeyboards.ProductKeyboard;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.*;

@Data
@Service
public class ProductService {
    private static final Map<Long, List<Product>> cart = new HashMap<>();
    private ProductsDao productsDao;
    private CategoriesDao categoriesDao;
    private CardService cardService;
    private CategoryService categoryService;

    public static Map<Long, List<Product>> getCard() {
        return cart;
    }

    public ProductService() {
    }

    @Autowired
    public ProductService(ProductsDao productsDao, CategoriesDao categoriesDao, CardService cardService, CategoryService categoryService) {
        this.productsDao = productsDao;
        this.categoriesDao = categoriesDao;
        this.cardService = cardService;
        this.categoryService = categoryService;
    }

    public Boolean productEqualsCategory(String data) {
        for (Category category : categoriesDao.index()) {
            if (category.getName().equals(data) || data.equals("categories")) {
                return true;
            }
        }
        return false;
    }

    public List<Product> getProduct(String data) {

        int id = Objects.requireNonNull(categoriesDao.index().stream().filter(category ->
                category.getName().contains(data)).findFirst().orElse(null)).getId();

        List<Product> productsCategory = new ArrayList<>();

        for (Product product : productsDao.index()) {
            if (id == product.getCategory()) {
                productsCategory.add(product);
            }
        }
        return productsCategory;
    }


    public InlineKeyboardMarkup getInlineMessageButtons(Product product, long userId) {
        int numberProductsInBasket = getCardService().numberProductsInBasket(product.getName(), userId);

        ProductKeyboard productKeyboard = new ProductKeyboard(product.getName(), numberProductsInBasket);
        return productKeyboard.getInlineMessageButtons();
    }
}
