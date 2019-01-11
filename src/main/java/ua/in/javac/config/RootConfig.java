package ua.in.javac.config;

import org.springframework.context.annotation.*;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import ua.in.javac.service.MessageService;
import ua.in.javac.service.impl.MessageServiceProvider;


@Configuration
@ComponentScan(basePackages = {"ua.in.javac.rest"},
        excludeFilters = {
                @Filter(type = FilterType.ANNOTATION, value = EnableWebMvc.class)
        })
@PropertySource("classpath:/application.properties")
public class RootConfig {

    @Bean
    @Profile("dev")
    public MessageService devMessageService() { return new MessageServiceProvider("Hello from Dev Profile");
    }

    @Bean
    @Profile("test")
    public MessageService testMessageService() {
        return new MessageServiceProvider("Hello from test Profile");
    }

}
