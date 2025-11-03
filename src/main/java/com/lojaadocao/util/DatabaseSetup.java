package com.lojaadocao.util;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseSetup {

    public static void createTables() {
        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement()) {

            String sqlOwners = "CREATE TABLE IF NOT EXISTS owners (" +
                    " id INT AUTO_INCREMENT PRIMARY KEY," +
                    " name VARCHAR(100) NOT NULL," +
                    " cpf VARCHAR(15) NOT NULL," +
                    " email VARCHAR(50) NOT NULL," +
                    " birth_date TIMESTAMP NOT NULL," +
                    " phone VARCHAR(20)," +
                    " address VARCHAR(200)," +
                    " created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    " updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")";

            String sqlAnimals = "CREATE TABLE IF NOT EXISTS animals (" +
                    " id INT AUTO_INCREMENT PRIMARY KEY," +
                    " name VARCHAR(120) NOT NULL," +
                    " age INT," +
                    " type VARCHAR(50) NOT NULL," +
                    " breed VARCHAR(80)," +
                    " gender VARCHAR(5)," +
                    " size VARCHAR(20)," +
                    " neutered BOOLEAN," +
                    " status VARCHAR(20) DEFAULT 'AVAILABLE' CHECK (status IN ('AVAILABLE','ADOPTED','RESERVED','UNDER_TREATMENT'))," +
                    " owner_id INT," +
                    " arrival_date DATE," +
                    " adoption_date TIMESTAMP," +
                    " created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    " updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    " FOREIGN KEY (owner_id) REFERENCES owners(id)" +
                    ")";

            String idxAnimalsStatus = "CREATE INDEX IF NOT EXISTS idx_animals_status ON animals(status)";
            String idxAnimalsOwner = "CREATE INDEX IF NOT EXISTS idx_animals_owner ON animals(owner_id)";
            String idxAnimalsType = "CREATE INDEX IF NOT EXISTS idx_animals_type ON animals(type)";

            // execute DDLs
            stmt.execute(sqlOwners);
            stmt.execute(sqlAnimals);

            // create indexes
            stmt.execute(idxAnimalsStatus);
            stmt.execute(idxAnimalsOwner);
            stmt.execute(idxAnimalsType);

            System.out.println("Tables and indexes created/verified successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}