package bot.entities;

import lombok.Data;

@Data
public class Order {
    private int id;
    private float totalAmount;
    private String clientId;
    private String checkoutId;

}
