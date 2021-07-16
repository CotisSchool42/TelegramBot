package bot;

import bot.botApi.Facade;
import bot.entities.Product;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ResourceUtils;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.io.File;
import java.io.IOException;

@Data
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class Bot extends TelegramWebhookBot {

    private String userName;
    private String botToken;
    private String webHookPath;
    private Facade facade;

    public Bot(Facade facade) {
        this.facade = facade;
    }

    @Override
    public String getBotUsername() {
        return userName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotPath() {
        return webHookPath;
    }


    /** Метод, обрабатывающий Updates от пользователя.  */
    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        try {
            return facade.handleUpdate(update);
        } catch (IOException | TelegramApiException e) {
            e.printStackTrace();
            return null;
        }
    }

    /** Отправляем приветственную фотографию магазина */
    @SneakyThrows
    public void sendMainBotPhoto(long chatId, String imageCaption, String imagePath) {
        File image = ResourceUtils.getFile("classpath:" + imagePath);
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(String.valueOf(chatId));
        sendPhoto.setCaption(imageCaption);
        InputFile imag = new InputFile().setMedia(image);
        sendPhoto.setPhoto(imag);
        execute(sendPhoto);
    }

    /** Отправляем фотографии продуктов из Categories, выбранных пользователем */
    @SneakyThrows
    public void sendProductPhoto(long chatId, Product product, InlineKeyboardMarkup inlineKeyboardMarkup) {
        File image = ResourceUtils.getFile(product.getPhoto_url());

        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setReplyMarkup(inlineKeyboardMarkup);
        sendPhoto.setChatId(String.valueOf(chatId));
        sendPhoto.setCaption(product.getName() + "\n\n" + product.getProductDescription()
                + "\n\n" + "Price: " + product.getPrice() + "₽");
        InputFile imag = new InputFile().setMedia(image);
        sendPhoto.setPhoto(imag);

        try {
            execute(sendPhoto);
        } catch (TelegramApiRequestException e) {
            log.info("TelegramApiRequestException Message: {}", e.getMessage());
        }
    }

    /** Отправляем фотографии продуктов из Bucket, выбранных пользователем. */
    public void sendBucket(SendPhoto sendPhoto) throws TelegramApiException {
        try {
            execute(sendPhoto);
        } catch (TelegramApiRequestException e) {
            log.info("TelegramApiRequestException Message: {}", e.getMessage());
        }
    }

    /** В editMessageMedia происходит редактирование сообщения с корзиной(Bucket)
     * при его изменении(удалении или добавлении товара).
     *
     * @param editMessageMedia - метод для редактирования анимации, аудио, документов, фотографий или видео сообщений.
     */
    public void editMessageMedia(EditMessageMedia editMessageMedia) throws TelegramApiException {
        try {
            execute(editMessageMedia);
        } catch (TelegramApiRequestException e) {
            log.info("TelegramApiRequestException Message: {}", e.getMessage());
        }
    }
}
