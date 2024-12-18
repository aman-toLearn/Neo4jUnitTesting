package com.aman.service;

import com.aman.model.Person;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.Values;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonService {


    private final Driver driver;

    @Autowired
    public PersonService(Driver driver) {
        this.driver = driver;
    }

    public Person createPerson(Person person) {
        try (Session session = driver.session()) {
            // Execute the query and return the created person node's properties
            org.neo4j.driver.Record record = session.run(
                    "CREATE (p:Person {name: $name, age: $age}) " +
                            "RETURN p.name AS name, p.age AS age",
                    Values.parameters(
                            "name", person.getName(),
                            "age", person.getAge()
                    )
            ).single(); // Retrieve the single result record

            // Map the returned properties back to a Person object
            return new Person(record.get("name").asString(), record.get("age").asInt());
        }
    }

    public List<Person> getAllPersons() {
        List<Person> persons = new ArrayList<>();
        try (Session session = driver.session()) {
            session.run("MATCH (p:Person) RETURN p.name AS name, p.age AS age")
                    .forEachRemaining(record -> {
                        persons.add(new Person(
                                record.get("name").asString(),
                                record.get("age").asInt()
                        ));
                    });
        }
        return persons;
    }

    public long countPersons() {
        try (Session session = driver.session()) {
            return session.run("MATCH (p:Person) RETURN count(p) AS count")
                    .single()
                    .get("count")
                    .asLong();
        }
    }
    public void updatePerson(String oldName, String newName, int newAge) {
        try (Session session = driver.session()) {
            session.run(
                    "MATCH (p:Person {name: $oldName}) " +
                            "SET p.name = $newName, p.age = $newAge",
                    Values.parameters("oldName", oldName, "newName", newName, "newAge", newAge)
            );
        }
    }


    public void deletePerson(String name) {
        try (Session session = driver.session()) {
            session.run("MATCH (p:Person {name: $name}) DELETE p",
                    Values.parameters("name", name)
            );
        }
    }
}
