package bot.cache;


import bot.botApi.BotState;

public interface DataCache {
    void setUsersCurrentBotState(long userId, BotState botState);
    BotState getUsersCurrentBotState(long userId);

}