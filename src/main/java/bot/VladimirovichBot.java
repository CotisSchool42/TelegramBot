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
@EqualsAndHashCode(callSuper=false)
@Slf4j
public class VladimirovichBot extends TelegramWebhookBot {

    private String userName;
    private String botToken;
    private String webHookPath;
    private Facade facade;

    public  VladimirovichBot(Facade facade) { this.facade = facade; }

    @Override
    public String getBotUsername() {
        return userName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotPath(){
        return webHookPath;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {

        try {
            return facade.handleUpdate(update);
        } catch (IOException | TelegramApiException e) {
            e.printStackTrace();
            return null;
        }
    }

    @SneakyThrows
    public void  sendMainBotPhoto(long chatId, String imageCaption, String imagePath) {
        File image = ResourceUtils.getFile("classpath:" + imagePath);

        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(String.valueOf(chatId));
        sendPhoto.setCaption(imageCaption);
        InputFile imag = new InputFile().setMedia(image);
        sendPhoto.setPhoto(imag);
        execute(sendPhoto);
    }

    @SneakyThrows
    public void sendProductPhoto(long chatId, Product product, InlineKeyboardMarkup inlineKeyboardMarkup) {
        File image = ResourceUtils.getFile("classpath:" + product.getPhoto_url());

        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setReplyMarkup(inlineKeyboardMarkup);
        sendPhoto.setChatId(String.valueOf(chatId));
        sendPhoto.setCaption(product.getName() + "\n\n" + product.getProduct_description()
                + "\n\n" +  "Price: " + product.getPrice() + "\uD83D\uDCB2");
        InputFile imag = new InputFile().setMedia(image);
        sendPhoto.setPhoto(imag);


        execute(sendPhoto);
    }

    public void sendBucket(SendPhoto sendPhoto) throws TelegramApiException {
        try {
            execute(sendPhoto);
        } catch (TelegramApiRequestException e) {
            System.out.println("Message: " + e.getMessage());
        }
    }

// Исключения кидать в логер
    public void editMessageMedia(EditMessageMedia editMessageMedia) throws TelegramApiException {
        try {
            execute(editMessageMedia);
        } catch (TelegramApiRequestException e) {
            System.out.println("Message: " + e.getMessage());
        }
    }
}

