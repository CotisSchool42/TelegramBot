package bot.entities;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class Category {
    private int id;

    @NotEmpty(message = "Name should not be empty")
    @Size(min = 3, max = 25, message = "Name should be between 3 and 25 characters")
    private String name;
}

