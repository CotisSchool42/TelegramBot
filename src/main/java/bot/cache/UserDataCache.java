package bot.cache;

import bot.botApi.BotState;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserDataCache implements DataCache {
    private Map<Long, BotState> usersBotStates = new HashMap<>();

    @Override
    public void setUsersCurrentBotState(long userId, BotState botState) {
        usersBotStates.put(userId, botState);
    }


    @Override
    public BotState getUsersCurrentBotState(long userId) {
        BotState botState = usersBotStates.get(userId);
        if (botState == null) {
            botState = BotState.ASK_CATEGORIES;
        }
        return botState;
    }
}
