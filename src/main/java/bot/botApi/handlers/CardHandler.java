package bot.botApi.handlers;

import bot.botApi.BotState;
import bot.cache.UserDataCache;
import bot.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;


@Component
public class CardHandler implements InputMessageHandler {
    private UserDataCache userDataCache;

    @Autowired
    public CardHandler(UserDataCache userDataCache) {
        this.userDataCache = userDataCache;
    }

    public SendMessage handle(Message message){
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_CARD;
    }

    private SendMessage processUsersInput(Message message) {
        long userId = message.getFrom().getId();
        userDataCache.setUsersCurrentBotState(userId, BotState.SHOW_CARD);
        return null;
    }
}
