package online_shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseDataSource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@LiquibaseDataSource
@EnableJpaRepositories
public class OnlineShopSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineShopSystemApplication.class, args);
    }

}
