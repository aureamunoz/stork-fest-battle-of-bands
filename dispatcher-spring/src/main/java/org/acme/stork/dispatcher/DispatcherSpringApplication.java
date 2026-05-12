package org.acme.stork.dispatcher;

import io.smallrye.stork.Stork;
import io.smallrye.stork.springboot.SpringBootApplicationContextProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;

@SpringBootApplication
public class DispatcherSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(DispatcherSpringApplication.class, args);
    }

    @Bean
    public SpringBootApplicationContextProvider springBootApplicationContextProvider() {
        return new SpringBootApplicationContextProvider();
    }

    @Bean
    @DependsOn("springBootApplicationContextProvider")
    public Stork stork() {
        Stork.initialize();
        return Stork.getInstance();
    }
}
