package com.aman.cli;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ConnectionTester implements CommandLineRunner {

    @Autowired
    private Driver driver;

    @Override
    public void run(String... args) {
        try (Session session = driver.session()) {
            String databaseName = session.run("CALL db.info()")
                    .single()
                    .get("name")
                    .asString();
            System.out.println("Connected to database: " + databaseName);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
