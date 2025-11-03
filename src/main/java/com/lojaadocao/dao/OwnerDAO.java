package com.lojaadocao.dao;

import com.lojaadocao.model.Owner;
import com.lojaadocao.util.ConnectionFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OwnerDAO {

    public Owner save(Owner owner) {
        String sql = "INSERT INTO owners (name, cpf, email, birth_date, phone, address, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, owner.getName());
            ps.setString(2, owner.getCpf());
            ps.setString(3, owner.getEmail());
            if (owner.getBirthDate() != null) {
                ps.setTimestamp(4, Timestamp.valueOf(owner.getBirthDate()));
            } else {
                ps.setNull(4, Types.TIMESTAMP);
            }
            ps.setString(5, owner.getPhone());
            ps.setString(6, owner.getAddress());

            LocalDateTime now = LocalDateTime.now();
            ps.setTimestamp(7, Timestamp.valueOf(now));
            ps.setTimestamp(8, Timestamp.valueOf(now));

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    owner.setId(rs.getInt(1));
                }
            }
            owner.setCreatedAt(now);
            owner.setUpdatedAt(now);
            return owner;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving owner", e);
        }
    }

    public Optional<Owner> findById(int id) {
        String sql = "SELECT * FROM owners WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRowToOwner(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error finding owner by id", e);
        }
    }

    public Optional<Owner> findByCpf(String cpf) {
        String sql = "SELECT * FROM owners WHERE cpf = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cpf);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRowToOwner(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error finding owner by cpf", e);
        }
    }

    public List<Owner> findAll() {
        String sql = "SELECT * FROM owners ORDER BY name";
        List<Owner> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRowToOwner(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("Error listing owners", e);
        }
    }

    public boolean update(Owner owner) {
        String sql = "UPDATE owners SET name = ?, cpf = ?, email = ?, birth_date = ?, phone = ?, address = ?, updated_at = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, owner.getName());
            ps.setString(2, owner.getCpf());
            ps.setString(3, owner.getEmail());
            if (owner.getBirthDate() != null) {
                ps.setTimestamp(4, Timestamp.valueOf(owner.getBirthDate()));
            } else {
                ps.setNull(4, Types.TIMESTAMP);
            }
            ps.setString(5, owner.getPhone());
            ps.setString(6, owner.getAddress());
            LocalDateTime now = LocalDateTime.now();
            ps.setTimestamp(7, Timestamp.valueOf(now));
            ps.setInt(8, owner.getId());

            int rows = ps.executeUpdate();
            if (rows == 1) {
                owner.setUpdatedAt(now);
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating owner", e);
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM owners WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            return rows == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting owner", e);
        }
    }

    private Owner mapRowToOwner(ResultSet rs) throws SQLException {
        Owner d = new Owner();
        d.setId(rs.getInt("id"));
        d.setName(rs.getString("name"));
        d.setCpf(rs.getString("cpf"));
        d.setEmail(rs.getString("email"));
        Timestamp ts = rs.getTimestamp("birth_date");
        if (ts != null) d.setBirthDate(ts.toLocalDateTime());
        d.setPhone(rs.getString("phone"));
        d.setAddress(rs.getString("address"));

        Timestamp created = rs.getTimestamp("created_at");
        if (created != null) d.setCreatedAt(created.toLocalDateTime());
        Timestamp updated = rs.getTimestamp("updated_at");
        if (updated != null) d.setUpdatedAt(updated.toLocalDateTime());
        return d;
    }
}