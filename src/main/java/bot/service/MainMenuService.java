package bot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;


@Service
public class MainMenuService {

    public MainMenuService() {
    }

    public SendMessage getMainMenuMessage(final long chatId, final String textMessage) {
        final ReplyKeyboardMarkup replyKeyboardMarkup = getMainMenuKeyboard();
        return createMessageWithKeyboard(chatId, textMessage, replyKeyboardMarkup);
    }

    /** ResizeKeyboard - указывает клиенту подогнать высоту клавиатуры под количество кнопок
     * (сделать меньше, если кнопок мало). По умолчанию False, то есть клавиатура всегда
     * такого же размера, как и стандартная клавиатура устройства.
     *
     * Selective - этот параметр нужен, чтобы показывать клавиатуру только определенным
     * пользователям. 1) Пользователи, которые были упомянуты в поле text объекта Message
     * 2) Если сообщения являются ответом (содержит поле reply_to_message_id), авторы этого
     * сообщения. Пример: Пользователь отправляет запрос на смену языка бота. Бот отправляет
     * клавиатуру со списком языков, видимую только этому пользователю
     *
     * OneTimeKeyboard - указывает клиенту скрыть клавиатуру после использования (после нажатия
     * на кнопку). Ее по-прежнему можно будет открыть через иконку в поле ввода сообщения.
     * False - по умолчанию.*/

    public ReplyKeyboardMarkup getMainMenuKeyboard() {

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("Categories \uD83C\uDF52"));
        row1.add(new KeyboardButton("Help \uD83C\uDD98"));
        keyboard.add(row1);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    //Bucket 🛍
    private SendMessage createMessageWithKeyboard(final long chatId,
                                                  String textMessage,
                                                  final ReplyKeyboardMarkup replyKeyboardMarkup) {
        final SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textMessage);
        if (replyKeyboardMarkup != null) {
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
        }
        return sendMessage;
    }
}