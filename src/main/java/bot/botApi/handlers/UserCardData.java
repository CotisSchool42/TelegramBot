package bot.botApi.handlers;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCardData {
    String name;
    String country;
    String city;
    String address;
    String phone;
    String email;


    @Override
    public String toString() {
        return String.format("Имя: %s%nСтрана: %s%nГород: %s%n" + "Адрес: %s%nТелефон: %s%nEmail: %s%n",
                getName(), getCountry(), getCity(), getAddress(), getPhone(), getEmail());
    }

}