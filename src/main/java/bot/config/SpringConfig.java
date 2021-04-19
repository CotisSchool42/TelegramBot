package bot.config;

import bot.VladimirovichBot;
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
@Configuration
@ConfigurationProperties(prefix = "telegram")
public class SpringConfig{

    private String userName;
    private String botToken;
    private String webHookPath;


    @Bean
    public VladimirovichBot vladimirovichBot(Facade facade) {
        VladimirovichBot vladimirovichBot = new VladimirovichBot(facade);
        vladimirovichBot.setWebHookPath(webHookPath);
        vladimirovichBot.setBotToken(botToken);
        vladimirovichBot.setUserName(userName);
        return vladimirovichBot;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource =
                new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    /* Нужно для того чтобы Spring знал к какой БД подключаться */
   @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }


    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        /* Эти данные нужно вынести в отдельный файл */
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/postgres");
        dataSource.setUsername("over");
        return dataSource;
    }

}

