package bot.service;

import bot.botApi.handlers.UserCardData;
import bot.cache.UserDataCache;
import bot.dao.ClientsDao;
import bot.entities.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ClientService extends ProductService {
    private final ClientsDao clientsDao;
    private final Map<Long, List<Product>> card = super.getCard();

    @Autowired
    public ClientService(ClientsDao clientsDao) {
        this.clientsDao = clientsDao;
    }

    public void setClient(long userId, UserCardData userDataCache) {
        clientsDao.addClient(userId, userDataCache);
        System.out.println(userId);
        System.out.println(userDataCache);
    }
}
