package bot.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.telegram.telegrambots.meta.api.objects.payments.SuccessfulPayment;

/**
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

/** Статус клиента может быть в двух состояниях(тип Boolean), если статус активный, то при нажатии на соответсвующее
     поле мы переключаем статус из акетивного состояния в неактивное и наоборот. В данном методе checkbox мы  получаем
     list в котором содержится единственный обьект из таблицы со значением переданного id. Далее создаем паттерн, который
     должен находить строку со статусом. В matcher.find находится значение статуса и далее записывается в строку статус.
     После мы сравниваем с помощью метода equals значение статуса с его предполагаемыми значениями. Если статус null или
     false , поле в таблице изменяется на противоположное,т.е true (также наоборот). Я думаю, что менять статус можно
     проще, но еще не узнал как.
     **/

    public void addClient(long userId, SuccessfulPayment successfulPayment) {
        String sql = "INSERT INTO client(name, email, phone, country, status, tg_id, city, address) VALUES (?,?,?,?,?,?,?,?)";
        jdbcTemplate.update(sql, successfulPayment.getOrderInfo().getName(), successfulPayment.getOrderInfo().getEmail(),
                successfulPayment.getOrderInfo().getPhoneNumber(), successfulPayment.getOrderInfo().getShippingAddress().getState(),
                true, userId, successfulPayment.getOrderInfo().getShippingAddress().getCity(),
                successfulPayment.getOrderInfo().getShippingAddress().getStreetLine1() + " " +
                        successfulPayment.getOrderInfo().getShippingAddress().getStreetLine2());
    }

}
