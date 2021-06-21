package bot.dao;

import bot.entities.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.telegram.telegrambots.meta.api.objects.payments.PreCheckoutQuery;
import org.telegram.telegrambots.meta.api.objects.payments.SuccessfulPayment;

import java.util.List;

@Repository
public class OrdersDao {

    private final JdbcTemplate jdbcTemplate;

    public OrdersDao(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }


    public void addOrder(Long userId, SuccessfulPayment successfulPayment) {
        String sql = "INSERT INTO orders(client_id, checkout_id, total_amount) VALUES (?,?,?)";
        jdbcTemplate.update(sql, userId, successfulPayment.getProviderPaymentChargeId(), successfulPayment.getTotalAmount());
    }

    public void orderList(SuccessfulPayment successfulPayment, List<Product> list) {
        String sql = "INSERT INTO order_list(order_id, product, price) VALUES (?,?,?)";
        for (Product product: list) {
            jdbcTemplate.update(sql, successfulPayment.getProviderPaymentChargeId(), product.getName(), product.getPrice());
        }
    }
}
