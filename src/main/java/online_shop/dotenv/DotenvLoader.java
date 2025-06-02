package online_shop.dotenv;

import io.github.cdimascio.dotenv.Dotenv;

public class DotenvLoader {
    static {
        Dotenv dotenv = Dotenv.load();
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
    }
}
