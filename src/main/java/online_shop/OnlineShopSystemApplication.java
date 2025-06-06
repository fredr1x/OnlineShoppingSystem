package online_shop;

import online_shop.dotenv.DotenvLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseDataSource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@LiquibaseDataSource
public class OnlineShopSystemApplication {

    public static void main(String[] args) {
        DotenvLoader loader = new DotenvLoader();
        SpringApplication.run(OnlineShopSystemApplication.class, args);
    }

}
