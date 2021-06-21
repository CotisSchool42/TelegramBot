package bot.cache;

import bot.botApi.BotState;
import bot.botApi.handlers.UserCardData;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserDataCache implements DataCache {
    private Map<Long, BotState> usersBotStates = new HashMap<>();
    private Map<Long, UserCardData> usersCardData = new HashMap<>();

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

    @Override
    public UserCardData getUserCardData(long userId) {
        UserCardData userCardData = usersCardData.get(userId);
        if (userCardData == null) {
            userCardData = new UserCardData();
        }
        return userCardData;
    }

    @Override
    public void saveUserCardData(long userId, UserCardData userCardData) {
        usersCardData.put(userId, userCardData);
    }
}
