package pl.jakubtworek.authorization;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.cloud.openfeign.*;
import org.springframework.context.annotation.*;

@ComponentScans({
        @ComponentScan(basePackages = "pl.jakubtworek.common.client")
})
@EnableFeignClients(basePackages = "pl.jakubtworek.common.client")
@SpringBootApplication
public class AuthorizationApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthorizationApplication.class, args);
    }
}
