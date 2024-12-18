/*
package com.aman.service;

import com.aman.model.User;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;
import org.neo4j.harness.Neo4j;
import org.neo4j.harness.Neo4jBuilders;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class UserServiceTest {

    private Neo4j embeddedDatabaseServer;
    private Driver testDriver;

    @BeforeClass
    public void setUp() {
        embeddedDatabaseServer = Neo4jBuilders.newInProcessBuilder().build();
        testDriver = GraphDatabase.driver(embeddedDatabaseServer.boltURI(), AuthTokens.none());
    }

    @AfterClass
    public void tearDown() {
        testDriver.close();
        embeddedDatabaseServer.close();
    }

    @Test
    public void testRunQuery() {
        try (Session session = testDriver.session()) {
            session.run("CREATE (n:Person {name: 'John Doe'}) RETURN n");
        }

        try (Session session = testDriver.session()) {
            String result = session.run("MATCH (n:Person) RETURN n.name AS name").single().get("name").asString();
            assertEquals("John Doe", result);
        }
    }
}
  */
