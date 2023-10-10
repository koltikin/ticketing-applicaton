package com.cydeo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class TicketingProjectSecurıtyApplication {
    public static void main(String[] args) {
        SpringApplication.run(TicketingProjectSecurıtyApplication.class, args);
    }

//    @Bean
//    public MigrateResult migrateResult(DataSource dataSource){
//        return Flyway.configure().baselineOnMigrate(true).dataSource(dataSource).load().migrate();
//    }
}
