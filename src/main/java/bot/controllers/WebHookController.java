package bot.controllers;

import bot.VladimirovichBot;
import lombok.Data;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

/** @RestController is a composed annotation that is itself meta-annotated with @Controller and @ResponseBody
 *  indicating a controller whose every method inherits the type-level @ResponseBody annotation and therefore writes
 *  and therefore writes directly to the response body vs view resolution and rendering with an HTML template. */
@Data
@RestController
public class WebHookController {
    private final VladimirovichBot telegramBot;

    @PostMapping("/")
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        return telegramBot.onWebhookUpdateReceived(update);
    }
}

