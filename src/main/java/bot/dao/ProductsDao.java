package bot.dao;

import bot.entities.Category;
import bot.entities.Product;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductsDao {
    private final JdbcTemplate jdbcTemplate;

    public ProductsDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Product> index() {
        return jdbcTemplate.query("SELECT * FROM product ORDER BY id", new BeanPropertyRowMapper<>(Product.class));
    }


/*    public void addCategory(Product product) {
        jdbcTemplate.update("INSERT INTO categories(name) VALUES (?)", product.getName());
    }

    public void deleteCategory(int id) {
        jdbcTemplate.update("DELETE FROM categories WHERE id=?", id);
    }

    public Category editCategory(int id) {
        System.out.println( jdbcTemplate.query("SELECT * FROM categories  WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Category.class)).stream().findAny().orElse(null));
        return jdbcTemplate.query("SELECT * FROM categories  WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Category.class))
                .stream().findAny().orElse(null);
    }

    public void updateCategory(int id, Category category) {
        System.out.println(category.getName());
        jdbcTemplate.update("UPDATE categories SET name=? WHERE id=?", category.getName(), id);
    }*/
}
