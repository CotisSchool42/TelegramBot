package bot.botApi.handlers;

import bot.botApi.BotState;
import bot.service.CategoryService;
import bot.service.ReplyMessagesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
@Component
public  class CategoriesHandler implements InputMessageHandler {
    private final CategoryService categoryService;
    private final ReplyMessagesService messagesService;

    public CategoriesHandler(CategoryService categoryService, ReplyMessagesService messagesService) {
        this.categoryService = categoryService;
        this.messagesService = messagesService;
    }
    public SendMessage processUsersInput(Message inputMsg) {
        long chatId = inputMsg.getChatId();

        SendMessage replyToUser = messagesService.getReplyMessage(chatId, "reply.askCategory");
        replyToUser.setReplyMarkup(categoryService.getInlineMessageButtons());

        return replyToUser;
    }

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_CATEGORIES;
    }

}
