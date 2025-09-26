package com.lojaadocao.util;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseSetup {

    public static void criarTabelas() {
        try (Connection conn = ConexaoFactory.getConnection();
             Statement stmt = conn.createStatement()) {

            String sqlDonos = "CREATE TABLE IF NOT EXISTS donos (" +
                    " id INT AUTO_INCREMENT PRIMARY KEY," +
                    " nome VARCHAR(100) NOT NULL," +
                    " cpf VARCHAR(15) NOT NULL," +
                    " email VARCHAR(50) NOT NULL," +
                    " dataNascimento TIMESTAMP NOT NULL," +
                    " telefone VARCHAR(20)," +
                    " endereco VARCHAR(200)," +
                    " created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    " updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")";

            String sqlAnimais = "CREATE TABLE IF NOT EXISTS animais (" +
                    " id INT AUTO_INCREMENT PRIMARY KEY," +
                    " nome VARCHAR(120) NOT NULL," +
                    " idade INT," +
                    " tipo VARCHAR(50) NOT NULL," +
                    " raca VARCHAR(80)," +
                    " sexo VARCHAR(5)," +
                    " porte VARCHAR(20)," +
                    " castrado BOOLEAN," +
                    " status VARCHAR(20) DEFAULT 'DISPONIVEL' CHECK (status IN ('DISPONIVEL','ADOTADO','RESERVADO','EM_TRATAMENTO'))," +
                    " dono_id INT," +
                    " chegada_date DATE," +
                    " data_adocao TIMESTAMP," +
                    " created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    " updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    " FOREIGN KEY (dono_id) REFERENCES donos(id)" +
                    ")";

            String idxAnimaisStatus = "CREATE INDEX IF NOT EXISTS idx_animais_status ON animais(status)";
            String idxAnimaisDono = "CREATE INDEX IF NOT EXISTS idx_animais_dono ON animais(dono_id)";
            String idxAnimaisTipo = "CREATE INDEX IF NOT EXISTS idx_animais_tipo ON animais(tipo)";

            // executar DDLs
            stmt.execute(sqlDonos);
            stmt.execute(sqlAnimais);

            // criar índices
            stmt.execute(idxAnimaisStatus);
            stmt.execute(idxAnimaisDono);
            stmt.execute(idxAnimaisTipo);

            System.out.println("Tabelas e índices criados/verificados com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
