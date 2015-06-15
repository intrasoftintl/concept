package eu.concept.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan({
    //Configuration imports
    "eu.concept.main",
    "eu.concept.repository.concept.service",
    "eu.concept.repository.openproject.service",
    "eu.concept.controller"
})

// Database Configurations will be derived by DatabaseConceptConfig & DatabaseOpenprojectConfig
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
@Configuration 
public class Application extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
