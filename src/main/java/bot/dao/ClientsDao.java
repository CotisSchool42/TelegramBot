package bot.dao;

import bot.botApi.handlers.UserCardData;
import bot.entities.Category;
import bot.entities.Client;
import bot.entities.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.telegram.telegrambots.meta.api.objects.payments.PreCheckoutQuery;
import org.telegram.telegrambots.meta.api.objects.payments.SuccessfulPayment;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 DAO используется для соединения с базой данных
 Data Access Object (DAO) — широко распространенный паттерн для сохранения объектов бизнес-области в базе данных.
 В самом широком смысле, DAO — это класс, содержащий CRUD методы для конкретной сущности.    */

@Repository
public class ClientsDao {

    private final JdbcTemplate jdbcTemplate;

    public ClientsDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /** RowMapper - объект, отображающий строки из таблицы в нашей сущности */
    public List<Client> index() {
        return jdbcTemplate.query("SELECT * FROM Client ORDER BY id", new BeanPropertyRowMapper<>(Client.class));
    }

    /** Статус клиента может быть в двух состояниях(тип Boolean), если статус активный, то при нажатии на соответсвующее
     поле мы переключаем статус из акетивного состояния в неактивное и наоборот. В данном методе checkbox мы  получаем
     list в котором содержится единственный обьект из таблицы со значением переданного id. Далее создаем паттерн, который
     должен находить строку со статусом. В matcher.find находится значение статуса и далее записывается в строку статус.
     После мы сравниваем с помощью метода equals значение статуса с его предполагаемыми значениями. Если статус null или
     false , поле в таблице изменяется на противоположное,т.е true (также наоборот). Я думаю, что менять статус можно
     проще, но еще не узнал как.
     **/

    public void checkbox(int id) {
        List<Client> list = jdbcTemplate.query("SELECT * FROM Client WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Client.class));
        String ArrayToString = list.toString();
        String status = "something";
        Pattern pattern = Pattern.compile("status=[a-zA-Z]+[a-zA-Z]");
        Matcher matcher = pattern.matcher(ArrayToString);
        while (matcher.find()) {
            status = ArrayToString.substring(matcher.start(), matcher.end());
        }
        if (status.equals("status=null") || status.equals("status=false"))
            jdbcTemplate.update("UPDATE Client SET status=? WHERE id=?", true, id);
        if (status.equals("status=true"))
            jdbcTemplate.update("UPDATE Client SET status=? WHERE id=?", false, id);
    }

    public Client editClient(int id) {
        return jdbcTemplate.query("SELECT * FROM client  WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Client.class))
                .stream().findAny().orElse(null);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM Client WHERE id=?", id);
    }

    public void updateClient(int id, Client client) {
        jdbcTemplate.update("UPDATE client SET name=? WHERE id=?", client.getName(), id);
    }

    public void addClient(long userId, SuccessfulPayment successfulPayment) {
        String sql = "INSERT INTO client(name, email, phone, country, status, tg_id, city, address) VALUES (?,?,?,?,?,?,?,?)";
        jdbcTemplate.update(sql, successfulPayment.getOrderInfo().getName(), successfulPayment.getOrderInfo().getEmail(),
                successfulPayment.getOrderInfo().getPhoneNumber(), successfulPayment.getOrderInfo().getShippingAddress().getState(),
                true, userId, successfulPayment.getOrderInfo().getShippingAddress().getCity(),
                successfulPayment.getOrderInfo().getShippingAddress().getStreetLine1() + " " +
                        successfulPayment.getOrderInfo().getShippingAddress().getStreetLine2());
    }

}
