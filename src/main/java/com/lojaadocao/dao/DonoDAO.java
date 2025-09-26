package com.lojaadocao.dao;

import com.lojaadocao.model.Dono;
import com.lojaadocao.util.ConexaoFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DonoDAO {

    public Dono salvar(Dono dono) {
        String sql = "INSERT INTO donos (nome, cpf, email, dataNascimento, telefone, endereco, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, dono.getNome());
            ps.setString(2, dono.getCpf());
            ps.setString(3, dono.getEmail());
            if (dono.getDataNascimento() != null) {
                ps.setTimestamp(4, Timestamp.valueOf(dono.getDataNascimento()));
            } else {
                ps.setNull(4, Types.TIMESTAMP);
            }
            ps.setString(5, dono.getTelefone());
            ps.setString(6, dono.getEndereco());

            LocalDateTime now = LocalDateTime.now();
            ps.setTimestamp(7, Timestamp.valueOf(now));
            ps.setTimestamp(8, Timestamp.valueOf(now));

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    dono.setId(rs.getInt(1));
                }
            }
            dono.setCreatedAt(now);
            dono.setUpdatedAt(now);
            return dono;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar dono", e);
        }
    }

    public Optional<Dono> findById(int id) {
        String sql = "SELECT * FROM donos WHERE id = ?";
        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRowToDono(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar dono por id", e);
        }
    }

    public Optional<Dono> findByCpf(String cpf) {
        String sql = "SELECT * FROM donos WHERE cpf = ?";
        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cpf);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRowToDono(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar dono por cpf", e);
        }
    }

    public List<Dono> findAll() {
        String sql = "SELECT * FROM donos ORDER BY nome";
        List<Dono> lista = new ArrayList<>();
        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapRowToDono(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar donos", e);
        }
    }

    public boolean atualizar(Dono dono) {
        String sql = "UPDATE donos SET nome = ?, cpf = ?, email = ?, dataNascimento = ?, telefone = ?, endereco = ?, updated_at = ? WHERE id = ?";
        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, dono.getNome());
            ps.setString(2, dono.getCpf());
            ps.setString(3, dono.getEmail());
            if (dono.getDataNascimento() != null) {
                ps.setTimestamp(4, Timestamp.valueOf(dono.getDataNascimento()));
            } else {
                ps.setNull(4, Types.TIMESTAMP);
            }
            ps.setString(5, dono.getTelefone());
            ps.setString(6, dono.getEndereco());
            LocalDateTime now = LocalDateTime.now();
            ps.setTimestamp(7, Timestamp.valueOf(now));
            ps.setInt(8, dono.getId());

            int rows = ps.executeUpdate();
            if (rows == 1) {
                dono.setUpdatedAt(now);
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar dono", e);
        }
    }

    public boolean deletar(int id) {
        String sql = "DELETE FROM donos WHERE id = ?";
        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            return rows == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar dono", e);
        }
    }

    // --- mapeador
    private Dono mapRowToDono(ResultSet rs) throws SQLException {
        Dono d = new Dono();
        d.setId(rs.getInt("id"));
        d.setNome(rs.getString("nome"));
        d.setCpf(rs.getString("cpf"));
        d.setEmail(rs.getString("email"));
        Timestamp ts = rs.getTimestamp("dataNascimento");
        if (ts != null) d.setDataNascimento(ts.toLocalDateTime());
        d.setTelefone(rs.getString("telefone"));
        d.setEndereco(rs.getString("endereco"));

        Timestamp created = rs.getTimestamp("created_at");
        if (created != null) d.setCreatedAt(created.toLocalDateTime());
        Timestamp updated = rs.getTimestamp("updated_at");
        if (updated != null) d.setUpdatedAt(updated.toLocalDateTime());
        return d;
    }
}
