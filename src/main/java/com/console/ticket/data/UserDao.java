package com.console.ticket.data;

import com.console.ticket.entity.User;
import com.console.ticket.exception.DatabaseException;
import com.console.ticket.util.ConnectionManager;
import com.console.ticket.util.SqlRequestsUtil;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static java.sql.Statement.*;

public class UserDao implements DaoTemplate<User> {
    private static final UserDao INSTANCE = new UserDao();

    @Override
    public Optional<User> findById(Integer id) throws DatabaseException {
        return Optional.empty();
    }

    @Override
    public void delete(Integer id) throws DatabaseException {

    }

    @Override
    @SneakyThrows
    public Optional<User> save(User user) throws DatabaseException {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(SqlRequestsUtil.USER_SAVE, RETURN_GENERATED_KEYS)) {
            preparedStatement.setObject(1, user.getName());
            preparedStatement.setObject(2, user.getEmail());
            preparedStatement.setObject(3, user.getPassword());
            preparedStatement.setObject(4, user.getRole());
            preparedStatement.setObject(5, user.getCardId());

            ResultSet keys = preparedStatement.getGeneratedKeys();
            keys.next();
            user.setId(keys.getObject("id", Integer.class));

            return Optional.of(user);
        }
    }

    @Override
    public void update(User user) throws DatabaseException {

    }

    @Override
    public List<Optional<User>> findAll() throws DatabaseException {
        return null;
    }

    public static UserDao getInstance() {
        return INSTANCE;
    }
}
