package bot.entities;

import lombok.Data;

@Data
public class OrderList {
    private int id;

    private String product;

    private String orderId;

    private float price;
}
