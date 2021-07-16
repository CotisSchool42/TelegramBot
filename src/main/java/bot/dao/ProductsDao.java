package bot.dao;

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

}
