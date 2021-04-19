package bot.botApi.handlers;

import bot.botApi.BotState;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileNotFoundException;

/**Обработчик сообщений
 */
public interface InputMessageHandler {
    SendMessage handle(Message message);
    BotState getHandlerName();
}
