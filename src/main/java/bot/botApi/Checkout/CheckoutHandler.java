package bot.botApi.Checkout;

import org.telegram.telegrambots.meta.api.methods.send.SendInvoice;
import org.telegram.telegrambots.meta.api.objects.payments.LabeledPrice;

import java.util.ArrayList;
import java.util.List;

public class CheckoutHandler {

    public void preCheckout(){
        List<LabeledPrice> labeledPrice = new ArrayList<>();
        LabeledPrice l = new LabeledPrice("1", 80 * 100);
        labeledPrice.add(l);

        SendInvoice sendInvoice = new SendInvoice(String.valueOf(userId), "2", "3", "4", "284685063:TEST:ZTEyMWRiZmU2YzQ0","true" , "RUB" , labeledPrice,
                null,null, null, null, true, true, true, true, null, null, null,
                null, null, null, null, null, null, null);
        log.info("Invoice {}", sendInvoice);
    }
}
