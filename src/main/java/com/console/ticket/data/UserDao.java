package com.console.ticket.data;

import com.console.ticket.entity.Role;
import com.console.ticket.entity.User;
import com.console.ticket.exception.DatabaseException;
import com.console.ticket.exception.InputException;
import com.console.ticket.util.ConnectionManager;
import com.console.ticket.util.SqlRequestsUtil;
import lombok.SneakyThrows;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.sql.Statement.*;

public class UserDao implements DaoTemplate<User> {
    private static final UserDao INSTANCE = new UserDao();

    @Override
    public Optional<User> findById(Integer id) throws DatabaseException {
        if (id == null || id < 0) {
            throw new InputException("Incorrect id format: " + id);
        }

        try (var connection = ConnectionManager.open();
             var preparedStatement = connection.prepareStatement(SqlRequestsUtil.USER_FIND_BY_ID)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            User user = null;

            if (resultSet.next()) {
                user = buildUser(resultSet);
            }

            return Optional.ofNullable(user);
        } catch (SQLException e) {
            throw new DatabaseException("Error find user by id: " + id, e);
        }
    }

    public Optional<User> findByEmail(String email) throws DatabaseException {
        if (email == null || email.isEmpty()) {
            throw new InputException("Incorrect email: " + email);
        }

        try (var connection = ConnectionManager.open();
             var preparedStatement = connection.prepareStatement(SqlRequestsUtil.USER_FIND_BY_EMAIL)) {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            User user = null;

            if (resultSet.next()) {
                user = buildUser(resultSet);
            }

            return Optional.ofNullable(user);
        } catch (SQLException e) {
            throw new DatabaseException("Error find user by email: " + email, e);
        }
    }

    @Override
    public void delete(Integer id) throws DatabaseException {
        if (id == null || id < 0) {
            throw new InputException("Incorrect id format: " + id);
        }

        boolean isDeleted;

        try (var connection = ConnectionManager.open();
             var preparedStatement = connection.prepareStatement(SqlRequestsUtil.USER_DELETE)) {
            preparedStatement.setObject(1, id);

            isDeleted = (preparedStatement.executeUpdate() > 0);
        } catch (SQLException e) {
            throw new DatabaseException("Error delete user by id: " + id, e);
        }

        if (!isDeleted) {
            throw new DatabaseException("No users found by id: " + id);
        }
    }

    @Override
    @SneakyThrows
    public Optional<User> save(User user) throws DatabaseException {
        try (var connection = ConnectionManager.open();
             var preparedStatement = connection.prepareStatement(SqlRequestsUtil.USER_SAVE, RETURN_GENERATED_KEYS)) {
            preparedStatement.setObject(1, user.getName());
            preparedStatement.setObject(2, user.getEmail());
            preparedStatement.setObject(3, user.getPassword());
            preparedStatement.setObject(4, user.getRole().name());
            preparedStatement.setObject(5, user.getCardId());

            preparedStatement.executeUpdate();
            ResultSet keys = preparedStatement.getGeneratedKeys();

            if (keys.next()) {
                user.setId(keys.getInt("id"));
            }

            return Optional.of(user);
        }
    }

    @Override
    public void update(User user) throws DatabaseException {
        try (var connection = ConnectionManager.open();
             var preparedStatement = connection.prepareStatement(SqlRequestsUtil.USER_UPDATE)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setObject(4, user.getRole().name());
            preparedStatement.setObject(5, user.getCardId());
            preparedStatement.setObject(6, user.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error update user: " + user.getName(), e);
        }
    }

    @Override
    public List<Optional<User>> findAll() throws DatabaseException {
        try (var connection = ConnectionManager.open();
             var preparedStatement = connection.prepareStatement(SqlRequestsUtil.USER_FIND_ALL);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            List<Optional<User>> userList = new ArrayList<>();

            while (resultSet.next()) {
                Optional<User> user = Optional.ofNullable(buildUser(resultSet));
                userList.add(user);
            }
            return userList;
        } catch (SQLException e) {
            throw new DatabaseException("Error get all cards from database: " + e.getMessage());
        }
    }

    private User buildUser(ResultSet resultSet) {
        try {
            return User.builder()
                    .id(resultSet.getInt("id"))
                    .name(resultSet.getString("name"))
                    .email(resultSet.getString("email"))
                    .password(resultSet.getString("password"))
                    .role(Role.valueOf(resultSet.getString("role")))
                    .cardId(resultSet.getInt("discount_card"))
                    .build();
        } catch (SQLException e) {
            throw new DatabaseException("Error create card: ", e);
        }
    }

    public static UserDao getInstance() {
        return INSTANCE;
    }
}
