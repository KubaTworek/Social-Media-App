package pl.jakubtworek.authors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.*;

@ComponentScans({
        @ComponentScan(basePackages = "pl.jakubtworek.common.client")
})
@EnableFeignClients(basePackages = "pl.jakubtworek.common.client")
@SpringBootApplication
public class AuthorsApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthorsApplication.class, args);
    }
}
