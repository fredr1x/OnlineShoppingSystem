package online_shop;

import online_shop.dotenv.DotenvLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OnlineShopSystemApplication {

    public static void main(String[] args) {
        DotenvLoader loader = new DotenvLoader();
        SpringApplication.run(OnlineShopSystemApplication.class, args);
    }

}
