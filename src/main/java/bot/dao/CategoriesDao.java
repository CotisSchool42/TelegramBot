package bot.dao;

import bot.entities.Category;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public  class CategoriesDao {
    private final JdbcTemplate jdbcTemplate;

    public CategoriesDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Category> index() {
        return jdbcTemplate.query("SELECT * FROM categories ORDER BY id", new BeanPropertyRowMapper<>(Category.class));
    }

    public void addCategory(Category category) {
        jdbcTemplate.update("INSERT INTO categories(name) VALUES (?)", category.getName());
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
    }
}