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

}