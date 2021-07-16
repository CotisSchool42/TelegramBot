package bot.botApi;

import bot.Bot;
import bot.botApi.handlers.CheckoutHandler;
import bot.cache.UserDataCache;
import bot.entities.Product;
import bot.service.*;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.payments.PreCheckoutQuery;
import org.telegram.telegrambots.meta.api.objects.payments.SuccessfulPayment;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

/** В приложении используется паттерн проектирования Фасад, который  позволяет скрыть сложность системы
 * путем сведения всех возможных внешних вызовов к одному объекту, делегирующему их соответствующим объектам системы.
 * В данном классе происходит основная логика работы, происходит обработка:  сообщений (message),
 * запроса от кнопок (CallbackQueries), оплаты (preCheckoutQuery и SuccessfulPayment) */

@Slf4j
@Component
public class Facade {
    private final CheckoutHandler checkoutHandler;
    private final BotStateContext botStateContext;
    private final UserDataCache userDataCache;
    private final MainMenuService mainMenuService;
    private final ReplyMessagesService messagesService;
    private final Bot bot;
    private final ProductService productService;


 public Facade(@Lazy Bot bot, CheckoutHandler checkoutHandler, ReplyMessagesService messagesService,
               ProductService productService, BotStateContext botStateContext, UserDataCache userDataCache, MainMenuService mainMenuService) {
        this.botStateContext = botStateContext;
        this.userDataCache = userDataCache;
        this.mainMenuService = mainMenuService;
        this.bot = bot;
        this.messagesService = messagesService;
        this.productService = productService;
        this.checkoutHandler = checkoutHandler;
    }

    /** HandleUpdate - Метод, обрабатывающий полученные Update. */
    public BotApiMethod<?> handleUpdate(Update update) throws IOException, TelegramApiException {
        SendMessage replyMessage = null;

        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            log.info("New callbackQuery from User: {}, userId: {}, with data: {}", update.getCallbackQuery().getFrom().getUserName(),
                    update.getCallbackQuery().getFrom().getId(), update.getCallbackQuery().getData());
            return processCallbackQuery(callbackQuery);
        }


        if (update.hasPreCheckoutQuery()) {
            PreCheckoutQuery preCheckoutQuery = update.getPreCheckoutQuery();
            log.info("PreCheckout {}", preCheckoutQuery);
            return  productService.getClientService().setAnswerPreCheckoutQuery(preCheckoutQuery);
        }

        Message message = update.getMessage();

        if (message != null && message.hasText()) {
            try {
                log.info("New message from User:{}, chatId: {},  with text: {}", message.getFrom().getUserName(), message.getChatId(), message.getText());
                replyMessage = handleInputMessage(message);
            } catch (NullPointerException e) {
                return null;
            }
        }

        if (message != null && message.hasSuccessfulPayment()) {
            try {
                SuccessfulPayment successfulPayment = message.getSuccessfulPayment();
                Long userId = message.getFrom().getId();
                return productService.getClientService().setClient(successfulPayment, userId);
            } catch (NullPointerException e) {
                return null;
            }
        }

        return replyMessage;
    }

    /** Метод, обрабатывающий Message. Возвращает сформированное сообщение. */
    private SendMessage handleInputMessage(Message message) {
        String inputMsg = message.getText();
        log.info("New message from with text: {}", message.getText());
        long userId = message.getFrom().getId();
        long chatId = message.getChatId();
        BotState botState;
        SendMessage replyMessage;

        switch (inputMsg) {
            case "/start":
                botState = BotState.SHOW_MAIN_MENU;
                bot.sendMainBotPhoto(chatId, messagesService.getReplyText("reply.hello"), "mainPhoto/pi.jpg");
                break;
            case "Categories \uD83C\uDF52":
                botState = BotState.SHOW_CATEGORIES;
                break;
            case "Help \uD83C\uDD98":
                botState = BotState.SHOW_HELP_MENU;
                break;
            default:
                botState = userDataCache.getUsersCurrentBotState(userId);
                break;
        }
        userDataCache.setUsersCurrentBotState(userId, botState);
        replyMessage = botStateContext.processInputMessage(botState, message);
        return replyMessage;
    }

    /**  Метод, обрабатывающий Inline клавиатуру */
    private BotApiMethod<?> processCallbackQuery(CallbackQuery buttonQuery) throws TelegramApiException, IOException {
        final long chatId = buttonQuery.getMessage().getChatId();
        final int messageId = buttonQuery.getMessage().getMessageId();
        final long userId = buttonQuery.getFrom().getId();
        final String data = buttonQuery.getData();
        BotApiMethod<?> callBackAnswer;

        if (data.equals("categories")) {
            SendMessage replyToUser = messagesService.getReplyMessage(chatId, "reply.askCategory");
            replyToUser.setReplyMarkup(productService.getCategoryService().getInlineMessageButtons());
            callBackAnswer = replyToUser;
        } else if (productService.productEqualsCategory(data)) {
            InlineKeyboardMarkup inlineKeyboardMarkup;
            for (Product product : productService.getProduct(buttonQuery.getData())) {
                inlineKeyboardMarkup = productService.getInlineMessageButtons(product, userId);
                bot.sendProductPhoto(chatId, product, inlineKeyboardMarkup);
            }
            return null;

        } else if (data.charAt(0) == '+' || data.charAt(0) == '-') {
            InlineKeyboardMarkup inlineKeyboardMarkup = productService.getCardService().editInlineMessageButtons(userId, data);
            callBackAnswer = new EditMessageReplyMarkup(String.valueOf(chatId), messageId, null, inlineKeyboardMarkup);

        } else if (data.equals("Show bucket") || data.startsWith("next") || data.startsWith("last")
                || data.startsWith("<") || data.startsWith(">")) {
            if (productService.getCardService().bucketIsEmpty(userId))
                callBackAnswer = mainMenuService.getMainMenuMessage(chatId, "Empty bucket");

            else if (!data.startsWith("Show bucket")) {
                EditMessageMedia editMessageMedia = productService.getCardService().editBucket(buttonQuery);
                if (editMessageMedia == null)
                    return mainMenuService.getMainMenuMessage(chatId, "Empty bucket");
                bot.editMessageMedia(editMessageMedia);
                callBackAnswer = null;
            } else {
                SendPhoto sendPhoto = productService.getCardService().showBucket(buttonQuery);
                bot.sendBucket(sendPhoto);
                callBackAnswer = null;
            }

        } else if (data.equals("Checkout now ✅")) {
            if (productService.getCardService().bucketIsEmpty(userId))
                return mainMenuService.getMainMenuMessage(chatId, "Empty bucket");
            return checkoutHandler.preCheckout(userId);
        } else {
            userDataCache.setUsersCurrentBotState(userId, BotState.SHOW_MAIN_MENU);
            callBackAnswer = null;
        }

        return callBackAnswer;
    }
}
