package pl.jakubtworek.articles;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.*;
import org.springframework.kafka.annotation.EnableKafka;

@ComponentScans({
        @ComponentScan(basePackages = "pl.jakubtworek.common.client")
})
@EnableFeignClients(basePackages = "pl.jakubtworek.common.client")
@SpringBootApplication
@EnableKafka
public class ArticlesApplication {
    public static void main(String[] args) {
        SpringApplication.run(ArticlesApplication.class, args);
    }
}
