package tech.never.more.xmore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
public class XMoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(XMoreApplication.class, args);
    }
}
