package bot.service;

import bot.dao.ClientsDao;
import bot.dao.OrdersDao;
import bot.entities.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerPreCheckoutQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.payments.PreCheckoutQuery;
import org.telegram.telegrambots.meta.api.objects.payments.SuccessfulPayment;

import java.util.List;
import java.util.Map;

@Service
public class ClientService extends ProductService {
    private final ClientsDao clientsDao;
    private final OrdersDao ordersDao;
    private final Map<Long, List<Product>> card = getCard();

    @Autowired
    public ClientService(ClientsDao clientsDao, OrdersDao ordersDao) {
        this.clientsDao = clientsDao;
        this.ordersDao = ordersDao;
    }

    public AnswerPreCheckoutQuery setAnswerPreCheckoutQuery(PreCheckoutQuery preCheckoutQuery) {

        AnswerPreCheckoutQuery answerPreCheckoutQuery = new AnswerPreCheckoutQuery();
        answerPreCheckoutQuery.setPreCheckoutQueryId(preCheckoutQuery.getId());
        answerPreCheckoutQuery.setOk(true);
        System.out.println(answerPreCheckoutQuery);
        return answerPreCheckoutQuery;
    }

    public SendMessage setClient(SuccessfulPayment successfulPayment, Long userId) {
        clientsDao.addClient(userId, successfulPayment);
        ordersDao.addOrder(userId, successfulPayment);
        ordersDao.orderList(successfulPayment, card.get(userId));
        card.remove(userId);

        return new SendMessage(String.valueOf(userId) ,"Thanks");
    }
}
