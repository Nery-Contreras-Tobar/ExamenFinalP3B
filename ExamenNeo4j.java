/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.examenneo4j;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Values;
/**
 *
 * @author NERY
 */
public class ExamenNeo4j {

     private static final String URI = "bolt://localhost:7687";
    private static final String USERNAME = "neo4j";
    private static final String PASSWORD = "contraseña";
    
   public static void main(String[] args) {
        try (Driver driver = GraphDatabase.driver(URI, AuthTokens.basic(USERNAME, PASSWORD))) {
            createNode(driver, "John", 30);
            createNode(driver, "Alice", 25);
            createNode(driver, "Bob", 35);
            readNodes(driver);
            updateNode(driver, "Alice", 26);
            readNodes(driver);
            deleteNode(driver, "Bob");
            readNodes(driver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createNode(Driver driver, String name, int age) {
        try (Session session = driver.session()) {
            String query = "CREATE (n:Person {name: $name, age: $age})";
            session.run(query, Values.parameters("name", name, "age", age));
        }
    }

    private static void readNodes(Driver driver) {
        try (Session session = driver.session()) {
            String query = "MATCH (n:Person) RETURN n.name, n.age";
            Result result = session.run(query);
            while (result.hasNext()) {
                Record record = result.next();
                String name = record.get("n.name").asString();
                int age = record.get("n.age").asInt();
                System.out.println("Name: " + name + ", Age: " + age);
            }
        }
    }

    private static void updateNode(Driver driver, String name, int newAge) {
        try (Session session = driver.session()) {
            String query = "MATCH (n:Person {name: $name}) SET n.age = $newAge";
            session.run(query, Values.parameters("name", name, "newAge", newAge));
        }
    }

    private static void deleteNode(Driver driver, String name) {
        try (Session session = driver.session()) {
            String query = "MATCH (n:Person {name: $name}) DELETE n";
            session.run(query, Values.parameters("name", name));
        }
    }
}

