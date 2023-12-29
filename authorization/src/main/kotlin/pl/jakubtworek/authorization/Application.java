package pl.jakubtworek.authorization;

import org.slf4j.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

@ComponentScans({
        @ComponentScan(basePackages = "pl.jakubtworek.common.client")
})
@EnableFeignClients(basePackages = "pl.jakubtworek.common.client")
@SpringBootApplication
class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        final SpringApplicationBuilder builder = new SpringApplicationBuilder(Application.class);
        final ConfigurableApplicationContext ctx = builder.run(args);
        logger.info(getStartupInfo(ctx));
    }

    static String getStartupInfo(ConfigurableApplicationContext ctx) {
        final Environment env = ctx.getEnvironment();
        final String port = env.getProperty("server.port");
        final List<String> profile = Arrays.asList(env.getActiveProfiles());
        return "Started SpringBoot application running on port: " + port + " with profile: " + profile;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
