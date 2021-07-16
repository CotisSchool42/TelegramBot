package bot.config;

import bot.Bot;
import bot.botApi.Facade;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Data
@ComponentScan("bot")
@PropertySource("classpath:application.properties")
@ConfigurationProperties(prefix = "telegram")
@Configuration
public class SpringConfig {

    private String userName;
    private String botToken;
    private String webHookPath;

    private String dbClassName;
    private String dbUrl;
    private String dbUserName;
    private String dbPassword;

    /** Создается объект бина (Spring создает объект класса VladimirovichBot и внедряет в него зависимости).
     *  setWebHookPath устанавливает адрес на который Telegram API будет оправлять запросы.
     *  setBotToken устанавливаем токен для бота, который выдает BotFather
     *  setUserName устанавливаем бота (также указывается при создании в BotFather)
     */
    @Bean
    public Bot vladimirovichBot(Facade facade) {
        Bot bot = new Bot(facade);
        bot.setWebHookPath(webHookPath);
        bot.setBotToken(botToken);
        bot.setUserName(userName);
        return bot;
    }

    /**
     * Интерфейс для разрешения сообщений с поддержкой параметризации и интернационализации таких сообщений.
     */
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource =
                new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    /**
     * Класс JdbcTemplate является центральным классом в базовом пакете JDBC. Он выполняет основной
     * рабочий процесс JDBC, оставляя код приложения для предоставления SQL и извлечения результатов.
     * Этот класс выполняет запросы или обновления SQL
     */
    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    /**
     * JDBC DataSource объекты используются для получения физического соединения с базой данных
     */
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(dbClassName);
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(dbUserName);
        dataSource.setPassword(dbPassword);
        return dataSource;
    }
}

