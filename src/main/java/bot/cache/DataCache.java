package bot.cache;


import bot.botApi.BotState;
import bot.botApi.handlers.UserCardData;

public interface DataCache {
    void setUsersCurrentBotState(long userId, BotState botState);

    BotState getUsersCurrentBotState(long userId);

    UserCardData getUserCardData(long userId);

    void saveUserCardData(long userId, UserCardData userCardData);
}