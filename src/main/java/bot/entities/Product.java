package bot.entities;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class Product {
    private int id;

    private String photo_url;

    private Integer categoryId;

    private String name;

    private float price;

    private String product_description;
}
