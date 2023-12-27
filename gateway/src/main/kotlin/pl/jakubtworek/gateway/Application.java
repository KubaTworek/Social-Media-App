package pl.jakubtworek.gateway;

import org.slf4j.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.util.*;

@EnableDiscoveryClient
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
}