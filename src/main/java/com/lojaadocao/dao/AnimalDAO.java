package com.lojaadocao.dao;

import com.lojaadocao.model.Animal;
import com.lojaadocao.model.Cat;
import com.lojaadocao.model.Dog;
import com.lojaadocao.util.ConnectionFactory;
import com.lojaadocao.util.Logger;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AnimalDAO {

    public Animal save(Animal a) {
        String sql = "INSERT INTO animals (name, age, type, breed, gender, size, neutered, status, owner_id, arrival_date, dog_breed_group, cat_coat_type, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, a.getName());
            if (a.getAge() != null) ps.setInt(2, a.getAge()); else ps.setNull(2, Types.INTEGER);
            ps.setString(3, a.getType());
            ps.setString(4, a.getBreed());
            ps.setString(5, a.getGender());
            ps.setString(6, a.getSize());
            if (a.getNeutered() != null) ps.setBoolean(7, a.getNeutered()); else ps.setNull(7, Types.BOOLEAN);
            ps.setString(8, a.getStatus() == null ? "AVAILABLE" : a.getStatus());
            if (a.getOwnerId() != null) ps.setInt(9, a.getOwnerId()); else ps.setNull(9, Types.INTEGER);
            if (a.getArrivalDate() != null) ps.setDate(10, Date.valueOf(a.getArrivalDate())); else ps.setNull(10, Types.DATE);

            if (a instanceof Dog) {
                ps.setString(11, ((Dog) a).getBreedGroup());
                ps.setNull(12, Types.VARCHAR);
            } else if (a instanceof Cat) {
                ps.setNull(11, Types.VARCHAR);
                ps.setString(12, ((Cat) a).getCoatType());
            } else {
                ps.setNull(11, Types.VARCHAR);
                ps.setNull(12, Types.VARCHAR);
            }

            LocalDateTime now = LocalDateTime.now();
            ps.setTimestamp(13, Timestamp.valueOf(now));
            ps.setTimestamp(14, Timestamp.valueOf(now));

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) a.setId(rs.getInt(1));
            }
            a.setCreatedAt(now);
            a.setUpdatedAt(now);
            return a;
        } catch (SQLException e) {
            Logger.error("Error saving animal", e);
            throw new RuntimeException("Error saving animal", e);
        }
    }

    public Optional<Animal> findById(int id) {
        String sql = "SELECT * FROM animals WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRowToAnimal(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error finding animal by id", e);
        }
    }

    public List<Animal> findAll() {
        String sql = "SELECT * FROM animals ORDER BY name";
        List<Animal> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) list.add(mapRowToAnimal(rs));
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("Error listing animals", e);
        }
    }

    public List<Animal> findAvailable() {
        String sql = "SELECT * FROM animals WHERE status = 'AVAILABLE' ORDER BY arrival_date";
        List<Animal> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) list.add(mapRowToAnimal(rs));
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("Error listing available animals", e);
        }
    }

    public List<Animal> findByOwner(int ownerId) {
        String sql = "SELECT * FROM animals WHERE owner_id = ? ORDER BY adoption_date DESC";
        List<Animal> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ownerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRowToAnimal(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("Error listing animals by owner", e);
        }
    }

    public boolean update(Animal a) {
        String sql = "UPDATE animals SET name = ?, age = ?, type = ?, breed = ?, gender = ?, size = ?, neutered = ?, status = ?, owner_id = ?, arrival_date = ?, dog_breed_group = ?, cat_coat_type = ?, updated_at = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, a.getName());
            if (a.getAge() != null) ps.setInt(2, a.getAge()); else ps.setNull(2, Types.INTEGER);
            ps.setString(3, a.getType());
            ps.setString(4, a.getBreed());
            ps.setString(5, a.getGender());
            ps.setString(6, a.getSize());
            if (a.getNeutered() != null) ps.setBoolean(7, a.getNeutered()); else ps.setNull(7, Types.BOOLEAN);
            ps.setString(8, a.getStatus());
            if (a.getOwnerId() != null) ps.setInt(9, a.getOwnerId()); else ps.setNull(9, Types.INTEGER);
            if (a.getArrivalDate() != null) ps.setDate(10, Date.valueOf(a.getArrivalDate())); else ps.setNull(10, Types.DATE);

            // Novos atributos específicos
            if (a instanceof Dog) {
                ps.setString(11, ((Dog) a).getBreedGroup());
                ps.setNull(12, Types.VARCHAR);
            } else if (a instanceof Cat) {
                ps.setNull(11, Types.VARCHAR);
                ps.setString(12, ((Cat) a).getCoatType());
            } else {
                ps.setNull(11, Types.VARCHAR);
                ps.setNull(12, Types.VARCHAR);
            }

            LocalDateTime now = LocalDateTime.now();
            ps.setTimestamp(13, Timestamp.valueOf(now));
            ps.setInt(14, a.getId());

            int rows = ps.executeUpdate();
            if (rows == 1) {
                a.setUpdatedAt(now);
                return true;
            }
            return false;
        } catch (SQLException e) {
            Logger.error("Error updating animal", e);
            throw new RuntimeException("Error updating animal", e);
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM animals WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            return rows == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting animal", e);
        }
    }

    public boolean adopt(int animalId, int ownerId) {
        String sql = "UPDATE animals SET status = ?, owner_id = ?, adoption_date = ?, updated_at = ? WHERE id = ? AND status = 'AVAILABLE'";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            LocalDateTime now = LocalDateTime.now();
            ps.setString(1, "ADOPTED");
            ps.setInt(2, ownerId);
            ps.setTimestamp(3, Timestamp.valueOf(now));
            ps.setTimestamp(4, Timestamp.valueOf(now));
            ps.setInt(5, animalId);

            int rows = ps.executeUpdate();
            return rows == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Error adopting animal", e);
        }
    }

    private Animal mapRowToAnimal(ResultSet rs) throws SQLException {
        String type = rs.getString("type");
        Animal a;

        switch (type.toUpperCase()) {
            case "DOG":
                Dog dog = new Dog();
                dog.setBreedGroup(rs.getString("dog_breed_group"));
                a = dog;
                break;
            case "CAT":
                Cat cat = new Cat();
                cat.setCoatType(rs.getString("cat_coat_type"));
                a = cat;
                break;
            default:
                throw new RuntimeException("Unknown animal type: " + type);
        }

        a.setId(rs.getInt("id"));
        a.setName(rs.getString("name"));
        int age = rs.getInt("age");
        if (!rs.wasNull()) a.setAge(age);
        a.setBreed(rs.getString("breed"));
        a.setGender(rs.getString("gender"));
        a.setSize(rs.getString("size"));
        boolean neuteredVal = rs.getBoolean("neutered");
        if (!rs.wasNull()) a.setNeutered(neuteredVal);
        a.setStatus(rs.getString("status"));

        int ownerId = rs.getInt("owner_id");
        if (!rs.wasNull()) a.setOwnerId(ownerId);

        Date arrival = rs.getDate("arrival_date");
        if (arrival != null) a.setArrivalDate(arrival.toLocalDate());

        Timestamp adoptTs = rs.getTimestamp("adoption_date");
        if (adoptTs != null) a.setAdoptionDate(adoptTs.toLocalDateTime());

        Timestamp created = rs.getTimestamp("created_at");
        if (created != null) a.setCreatedAt(created.toLocalDateTime());
        Timestamp updated = rs.getTimestamp("updated_at");
        if (updated != null) a.setUpdatedAt(updated.toLocalDateTime());

        return a;
    }
}