package com.aman.service;




import com.aman.model.Person;
import org.neo4j.driver.Driver;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;
import org.neo4j.driver.Values;
import org.neo4j.harness.Neo4j;
import org.neo4j.harness.Neo4jBuilders;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.neo4j.io.fs.FileUtils;

import static org.testng.AssertJUnit.assertEquals;




public class PersonServiceTest {
    private Driver driver;
    Path testdatabaseDir = Paths.get("target/neo4j-embedded-db");


    @BeforeClass
    public void initializeNeo4j() {
        if (Files.exists(testdatabaseDir)) {
            try {
                FileUtils.deleteDirectory(testdatabaseDir.toFile().toPath());
            } catch (IOException e) {
                throw new RuntimeException("Failed to clear database directory", e);
            }
        }
        Neo4j embeddedDatabaseServer = Neo4jBuilders.newInProcessBuilder()
                .withDisabledServer()
                .withProcedure(PersonService.class).withWorkingDir(testdatabaseDir)
                .build();

        this.driver = GraphDatabase.driver(embeddedDatabaseServer.boltURI());
    }
    
    
    
    

    @Test
    public void testCreateNode() {

            PersonService p = new PersonService(driver);
            Person person = new Person("aman",23);

            Person person1 = p.createPerson(person);

            assert person1.getName().equals("aman");
            assertEquals(person1.getAge(),23);
    }



    @Test
    public void testDeletePerson() {
        //  Create a person node to delete
        PersonService personService = new PersonService(driver);
        Person person = new Person("aman", 23);
        personService.createPerson(person);

        // Act: Call the deletePerson method
        personService.deletePerson("aman");

        //Verify the node no longer exists
        try (Session session = driver.session()) {
            org.neo4j.driver.Record record = session.run(
                    "MATCH (p:Person {name: $name}) RETURN COUNT(p) AS count",
                    Values.parameters("name", "aman")
            ).single();
            int count = record.get("count").asInt();
            Assert.assertEquals(count, 0, "Person node should not exist after deletion");
        }
    }


    @Test
    public void testUpdatePerson() {
        // Create a Person node in the database
        PersonService personService = new PersonService(driver);
        Person inputPerson = new Person("BhojakAman", 30);
        personService.createPerson(inputPerson);

        // Update the person's name and age
        personService.updatePerson("BhojakAman", "AmanBhojak", 24);

        // Verify the updated node exists with new properties
        try (Session session = driver.session()) {
            org.neo4j.driver.Record record = session.run(
                    "MATCH (p:Person {name: $newName, age: $newAge}) " +
                            "RETURN p.name AS name, p.age AS age",
                    Values.parameters("newName", "AmanBhojak", "newAge", 24)
            ).single();


            Assert.assertEquals(record.get("name").asString(), "AmanBhojak", "The name should be updated to 'Jane Doe'");
            Assert.assertEquals(record.get("age").asInt(), 24, "The age should be updated to 35");
        }
    }




   /* @AfterClass
    public void tearDown() {
        driver.close();
        serverControls.close();
    }*/

}
