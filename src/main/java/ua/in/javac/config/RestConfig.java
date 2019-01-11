package ua.in.javac.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"ua.in.javac.rest"})
public class RestConfig {
}
