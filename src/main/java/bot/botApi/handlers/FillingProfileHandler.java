package bot.botApi.handlers;

import bot.botApi.BotState;
import bot.cache.UserDataCache;
import bot.dao.CategoriesDao;
import bot.entities.Client;
import bot.replyKeyboards.ConfirmationButtons;
import bot.service.ClientService;
import bot.service.MainMenuService;
import bot.service.ReplyMessagesService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;


@Slf4j
@Component
public class FillingProfileHandler implements InputMessageHandler {
    private UserDataCache userDataCache;
    private ReplyMessagesService messagesService;
    private ClientService clientService;

    public FillingProfileHandler(UserDataCache userDataCache, ReplyMessagesService messagesService, ClientService clientService) {
        this.userDataCache = userDataCache;
        this.messagesService = messagesService;
        this.clientService = clientService;
    }

    @Override
    public SendMessage handle(Message message) {
        if (userDataCache.getUsersCurrentBotState(message.getFrom().getId()).equals(BotState.FILLING_PROFILE)) {
            userDataCache.setUsersCurrentBotState(message.getFrom().getId(), BotState.ASK_NAME);
        }
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.FILLING_PROFILE;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        String usersAnswer = inputMsg.getText();
        long userId = inputMsg.getFrom().getId();
        long chatId = inputMsg.getChatId();

        UserCardData profileData = userDataCache.getUserCardData(userId);
        BotState botState = userDataCache.getUsersCurrentBotState(userId);

        SendMessage replyToUser = null;

        if (usersAnswer.equals("Back \uD83D\uDD19")) {
            MainMenuService mainMenuHandler = new MainMenuService();
            replyToUser = new SendMessage(String.valueOf(chatId), "Main Menu \uD83E\uDE84");
            userDataCache.setUsersCurrentBotState(userId, BotState.SHOW_MAIN_MENU);
            replyToUser.setReplyMarkup(mainMenuHandler.getMainMenuKeyboard());
            return replyToUser;
        }

        if (botState.equals(BotState.ASK_NAME) && !usersAnswer.equals("Correct ✅")) {
            replyToUser = new SendMessage(String.valueOf(chatId), "Name : " + usersAnswer);
            profileData.setName(usersAnswer);
            replyToUser.setReplyMarkup(new ConfirmationButtons().getConfirmationKeyboard());
        }

        if (botState.equals(BotState.ASK_NAME) && usersAnswer.equals("Correct ✅")) {
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_COUNTRY);
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askCountry");
        } else if (botState.equals(BotState.ASK_NAME) && usersAnswer.equals("Edit \uD83D\uDEAB")) {
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askName");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_NAME);
        }

        if (botState.equals(BotState.ASK_COUNTRY) && !usersAnswer.equals("Correct ✅")) {
            replyToUser = new SendMessage(String.valueOf(chatId), "Country : " + usersAnswer);
            profileData.setCountry(usersAnswer);
            replyToUser.setReplyMarkup(new ConfirmationButtons().getConfirmationKeyboard());
        }

        if (botState.equals(BotState.ASK_COUNTRY) && usersAnswer.equals("Correct ✅")) {
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_CITY);
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askCity");
        } else if (botState.equals(BotState.ASK_COUNTRY) && usersAnswer.equals("Edit \uD83D\uDEAB")) {
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askCountry");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_COUNTRY);
        }

        if (botState.equals(BotState.ASK_CITY) && !usersAnswer.equals("Correct ✅")) {
            replyToUser = new SendMessage(String.valueOf(chatId), "City : " + usersAnswer);
            profileData.setCity(usersAnswer);
            replyToUser.setReplyMarkup(new ConfirmationButtons().getConfirmationKeyboard());
        }

        if (botState.equals(BotState.ASK_CITY) && usersAnswer.equals("Correct ✅")) {
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_ADDRESS);
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askAddress");
        } else if (botState.equals(BotState.ASK_CITY) && usersAnswer.equals("Edit \uD83D\uDEAB")) {
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askCity");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_CITY);
        }

        if (botState.equals(BotState.ASK_ADDRESS) && !usersAnswer.equals("Correct ✅")) {
            replyToUser = new SendMessage(String.valueOf(chatId), "Address : " + usersAnswer);
            profileData.setAddress(usersAnswer);
            replyToUser.setReplyMarkup(new ConfirmationButtons().getConfirmationKeyboard());
        }

        if (botState.equals(BotState.ASK_ADDRESS) && usersAnswer.equals("Correct ✅")) {
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_PHONE);
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askPhone");
        } else if (botState.equals(BotState.ASK_ADDRESS) && usersAnswer.equals("Edit \uD83D\uDEAB")) {
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askAddress");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_ADDRESS);
        }

        if (botState.equals(BotState.ASK_PHONE) && !usersAnswer.equals("Correct ✅")) {
             replyToUser = new SendMessage(String.valueOf(chatId), "Phone : " + usersAnswer);
             profileData.setPhone(usersAnswer);
             replyToUser.setReplyMarkup(new ConfirmationButtons().getConfirmationKeyboard());
        }

        if (botState.equals(BotState.ASK_PHONE) && usersAnswer.equals("Correct ✅")) {
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_EMAIL);
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askEmail");
        } else if (botState.equals(BotState.ASK_PHONE) && usersAnswer.equals("Edit \uD83D\uDEAB")) {
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askPhone");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_PHONE);
        }

        if (botState.equals(BotState.ASK_EMAIL) && !usersAnswer.equals("Correct ✅")) {
            replyToUser = new SendMessage(String.valueOf(chatId), "Email :" + usersAnswer);
            profileData.setEmail(usersAnswer);
            replyToUser.setReplyMarkup(new ConfirmationButtons().getConfirmationKeyboard());
        }

        if (botState.equals(BotState.ASK_EMAIL) && usersAnswer.equals("Correct ✅")) {
            userDataCache.setUsersCurrentBotState(userId, BotState.PROFILE_FILLED);
            botState = userDataCache.getUsersCurrentBotState(userId);
        } else if (botState.equals(BotState.ASK_EMAIL) && usersAnswer.equals("Edit \uD83D\uDEAB")) {
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askEmail");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_EMAIL);
        }

        if (botState.equals(BotState.PROFILE_FILLED) && !usersAnswer.equals("Process payment ✅")) {
            replyToUser = new SendMessage(String.valueOf(chatId), "User data:\n\n" + profileData.toString());
            replyToUser.setReplyMarkup(new ConfirmationButtons().getPayKeyboard());
            System.out.println(profileData);
        }
        // Process payment
        if (botState.equals(BotState.PROFILE_FILLED) && usersAnswer.equals("Process payment ✅")) {

            MainMenuService mainMenuService = new MainMenuService();
            replyToUser =  new SendMessage(String.valueOf(chatId), "Main Menu \uD83D\uDECD"); // Пишет сообщение Main Menu
            replyToUser.setReplyMarkup(mainMenuService.getMainMenuKeyboard());                     // Устанавливает клавиатуру
            userDataCache.setUsersCurrentBotState(userId, BotState.SHOW_MAIN_MENU);                // Переводит состояние бота в показать меню
            clientService.setClient(userId, profileData);                                          // Добавляет клиента в базу данных (желательно не трогать)
        }
       //  - Сбербанк TEST: 401643678:TEST:d59b4143-69ca-4d31-972e-371c6ff370a7 2021-04-12 13:10
        userDataCache.saveUserCardData(userId, profileData);                                       // Сохраняет состояние, а выше устанавливает состояние

        return replyToUser;
    }
}
