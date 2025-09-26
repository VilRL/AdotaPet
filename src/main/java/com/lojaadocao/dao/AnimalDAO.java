package com.lojaadocao.dao;

import com.lojaadocao.model.Animal;
import com.lojaadocao.util.ConexaoFactory;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AnimalDAO {

    public Animal salvar(Animal a) {
        String sql = "INSERT INTO animais (nome, idade, tipo, raca, sexo, porte, castrado, status, dono_id, chegada_date, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, a.getNome());
            if (a.getIdade() != null) ps.setInt(2, a.getIdade()); else ps.setNull(2, Types.INTEGER);
            ps.setString(3, a.getTipo());
            ps.setString(4, a.getRaca());
            ps.setString(5, a.getSexo());
            ps.setString(6, a.getPorte());
            if (a.getCastrado() != null) ps.setBoolean(7, a.getCastrado()); else ps.setNull(7, Types.BOOLEAN);
            ps.setString(8, a.getStatus() == null ? "DISPONIVEL" : a.getStatus());
            if (a.getDonoId() != null) ps.setInt(9, a.getDonoId()); else ps.setNull(9, Types.INTEGER);
            if (a.getChegadaDate() != null) ps.setDate(10, Date.valueOf(a.getChegadaDate())); else ps.setNull(10, Types.DATE);

            LocalDateTime now = LocalDateTime.now();
            ps.setTimestamp(11, Timestamp.valueOf(now));
            ps.setTimestamp(12, Timestamp.valueOf(now));

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) a.setId(rs.getInt(1));
            }
            a.setCreatedAt(now);
            a.setUpdatedAt(now);
            return a;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar animal", e);
        }
    }

    public Optional<Animal> findById(int id) {
        String sql = "SELECT * FROM animais WHERE id = ?";
        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRowToAnimal(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar animal por id", e);
        }
    }

    public List<Animal> findAll() {
        String sql = "SELECT * FROM animais ORDER BY nome";
        List<Animal> lista = new ArrayList<>();
        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) lista.add(mapRowToAnimal(rs));
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar animais", e);
        }
    }

    public List<Animal> listarDisponiveis() {
        String sql = "SELECT * FROM animais WHERE status = 'DISPONIVEL' ORDER BY chegada_date";
        List<Animal> lista = new ArrayList<>();
        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) lista.add(mapRowToAnimal(rs));
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar animais disponíveis", e);
        }
    }

    public List<Animal> listarPorDono(int donoId) {
        String sql = "SELECT * FROM animais WHERE dono_id = ? ORDER BY data_adocao DESC";
        List<Animal> lista = new ArrayList<>();
        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, donoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapRowToAnimal(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar animais do dono", e);
        }
    }

    public boolean atualizar(Animal a) {
        String sql = "UPDATE animais SET nome = ?, idade = ?, tipo = ?, raca = ?, sexo = ?, porte = ?, castrado = ?, status = ?, dono_id = ?, chegada_date = ?, updated_at = ? WHERE id = ?";
        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, a.getNome());
            if (a.getIdade() != null) ps.setInt(2, a.getIdade()); else ps.setNull(2, Types.INTEGER);
            ps.setString(3, a.getTipo());
            ps.setString(4, a.getRaca());
            ps.setString(5, a.getSexo());
            ps.setString(6, a.getPorte());
            if (a.getCastrado() != null) ps.setBoolean(7, a.getCastrado()); else ps.setNull(7, Types.BOOLEAN);
            ps.setString(8, a.getStatus());
            if (a.getDonoId() != null) ps.setInt(9, a.getDonoId()); else ps.setNull(9, Types.INTEGER);
            if (a.getChegadaDate() != null) ps.setDate(10, Date.valueOf(a.getChegadaDate())); else ps.setNull(10, Types.DATE);

            LocalDateTime now = LocalDateTime.now();
            ps.setTimestamp(11, Timestamp.valueOf(now));
            ps.setInt(12, a.getId());

            int rows = ps.executeUpdate();
            if (rows == 1) {
                a.setUpdatedAt(now);
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar animal", e);
        }
    }

    public boolean deletar(int id) {
        String sql = "DELETE FROM animais WHERE id = ?";
        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            return rows == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar animal", e);
        }
    }

    /**
     * Tenta adotar um animal: só funciona se o status atual for 'DISPONIVEL'.
     * Retorna true se a atualização ocorreu (animal adotado), false caso contrário.
     */
    public boolean adotar(int animalId, int donoId) {
        String sql = "UPDATE animais SET status = ?, dono_id = ?, data_adocao = ?, updated_at = ? WHERE id = ? AND status = 'DISPONIVEL'";
        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            LocalDateTime now = LocalDateTime.now();
            ps.setString(1, "ADOTADO");
            ps.setInt(2, donoId);
            ps.setTimestamp(3, Timestamp.valueOf(now));
            ps.setTimestamp(4, Timestamp.valueOf(now));
            ps.setInt(5, animalId);

            int rows = ps.executeUpdate();
            return rows == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao adotar animal", e);
        }
    }

    // --- mapeador
    private Animal mapRowToAnimal(ResultSet rs) throws SQLException {
        Animal a = new Animal();
        a.setId(rs.getInt("id"));
        a.setNome(rs.getString("nome"));
        int idade = rs.getInt("idade");
        if (!rs.wasNull()) a.setIdade(idade);
        a.setTipo(rs.getString("tipo"));
        a.setRaca(rs.getString("raca"));
        a.setSexo(rs.getString("sexo"));
        a.setPorte(rs.getString("porte"));
        boolean castradoVal = rs.getBoolean("castrado");
        if (!rs.wasNull()) a.setCastrado(castradoVal);
        a.setStatus(rs.getString("status"));

        int donoId = rs.getInt("dono_id");
        if (!rs.wasNull()) a.setDonoId(donoId);

        Date chegada = rs.getDate("chegada_date");
        if (chegada != null) a.setChegadaDate(chegada.toLocalDate());

        Timestamp adotTs = rs.getTimestamp("data_adocao");
        if (adotTs != null) a.setDataAdocao(adotTs.toLocalDateTime());

        Timestamp created = rs.getTimestamp("created_at");
        if (created != null) a.setCreatedAt(created.toLocalDateTime());
        Timestamp updated = rs.getTimestamp("updated_at");
        if (updated != null) a.setUpdatedAt(updated.toLocalDateTime());

        return a;
    }
}
