package pl.jakubtworek.notifications;

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
public class NotificationsApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationsApplication.class, args);
    }

}
