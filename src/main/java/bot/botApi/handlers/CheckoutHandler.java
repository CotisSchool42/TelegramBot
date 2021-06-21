package bot.botApi.handlers;

import bot.entities.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendInvoice;
import org.telegram.telegrambots.meta.api.objects.payments.LabeledPrice;

import java.util.ArrayList;
import java.util.List;

import bot.service.ProductService;

@Slf4j
@Component
public class CheckoutHandler {
    private SendInvoice sendInvoice;

    @Value("${telegram.providerToken}")
    String providerToken;

    public SendInvoice preCheckout(long userId){
        List<LabeledPrice> labeledPrice = new ArrayList<>();
        List<Product> listOfProducts = new ArrayList<>(ProductService.getCard().get(userId));
        listOfProducts.forEach(product -> {
                 LabeledPrice l = new LabeledPrice(product.getName(), (int)product.getPrice() * 100);
            labeledPrice.add(l);
        });

        /** Цена должна быть не менее 1$ по действующему курсу. Здесь цена в копейках, поэтому домножается на сто.
        Если инвойс не присылается, и никаких ошибок - цена либо слишком большая, либо слишком маленькая */

        sendInvoice = new SendInvoice();
        sendInvoice.setChatId(String.valueOf((int)userId));
        sendInvoice.setTitle("Cheque");
        sendInvoice.setDescription("Thank you for being with us!");
        sendInvoice.setPayload(String.valueOf(userId));
        sendInvoice.setProviderToken(providerToken);
        sendInvoice.setStartParameter("true");
        sendInvoice.setCurrency("RUB");
        sendInvoice.setNeedName(true);
        sendInvoice.setNeedEmail(true);
        sendInvoice.setNeedPhoneNumber(true);
        sendInvoice.setNeedPhoneNumber(true);
        sendInvoice.setNeedShippingAddress(true);
        sendInvoice.setPrices(labeledPrice);

        log.info("Invoice {}", sendInvoice);
        return sendInvoice;
    }
}
