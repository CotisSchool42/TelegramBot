package bot.botApi.Checkout;

import bot.entities.Product;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendInvoice;
import org.telegram.telegrambots.meta.api.objects.payments.LabeledPrice;

import java.util.ArrayList;
import java.util.List;

import bot.service.ProductService;

@Slf4j
public class CheckoutHandler {
    private SendInvoice sendInvoice;
//    private ProductService productService = new ProductService();

    public CheckoutHandler(){}

    public SendInvoice preCheckout(long userId){
        log.info("checkout started");
        List<LabeledPrice> labeledPrice = new ArrayList<>();
        List<Product> listOfProducts = new ArrayList<>(ProductService.getCard().get(userId));
        listOfProducts.forEach(product -> {
            LabeledPrice l = new LabeledPrice(product.getName(), (int)product.getPrice() * 100);
            labeledPrice.add(l);
        });

        sendInvoice = new SendInvoice(String.valueOf((int)userId), "2", "3", "4", "284685063:TEST:ZTEyMWRiZmU2YzQ0","true" , "RUB" , labeledPrice,
                null,null, null, null, true, true, true, true, null, null, null,
                null, null, null, null, null, null, null);
        log.info("Invoice {}", sendInvoice);
        return sendInvoice;
    }
}
