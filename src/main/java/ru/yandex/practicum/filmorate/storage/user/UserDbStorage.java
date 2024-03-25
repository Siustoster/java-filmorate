package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.Exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component("UserDbStorage")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getAll() {
        String sql = "SELECT u.ID as user_id, " +
                "u.name as user_name," +
                "u.LOGIN as user_login," +
                "u.EMAIL as user_email," +
                "u.BIRTHDATE as user_birthdate, " +
                "f.USER_ID as friend_id " +
                "FROM USERS u " +
                "LEFT JOIN FRIENDSHIP f on f.FRIEND_ID = u.ID";
        return jdbcTemplate.query(sql, new UserExtractor());
    }

    @Override
    public User createUser(User user) {
        String sql = "INSERT INTO USERS (NAME,LOGIN,EMAIL,BIRTHDATE) VALUES " +
                "(?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"id"});
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getEmail());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);

        int userId = keyHolder.getKey().intValue();


        return getUserById(userId);
    }

    @Override
    public User updateUser(User user) {
        String sql = "UPDATE USERS " +
                "set NAME=?, " +
                "LOGIN=?, " +
                "EMAIL=?, " +
                "BIRTHDATE=? " +
                " " +
                "WHERE USERS.ID=?";

        jdbcTemplate.update(sql, user.getName(),
                user.getLogin(),
                user.getEmail(),
                Date.valueOf(user.getBirthday()),
                user.getId());

        return getUserById(user.getId());
    }

    @Override
    public User getUserById(int id) {
        String sql = "SELECT u.ID as user_id, " +
                "u.name as user_name," +
                "u.LOGIN as user_login," +
                "u.EMAIL as user_email," +
                "u.BIRTHDATE as user_birthdate, " +
                "f.FRIEND_ID as friend_id " +
                "FROM USERS u " +
                "LEFT JOIN FRIENDSHIP f on f.USER_ID = u.ID " +
                "WHERE u.ID=?";

        List<User> users = jdbcTemplate.query(sql, new UserExtractor(), id);
        if (users.isEmpty())
            throw new NotFoundException("Пользователь с айди " + id + " не найден");
        return users.get(0);
    }

    @Override
    public void addFriend(int userId, int friendId) {
        deleteFriend(userId, friendId);

        String insertSql = "INSERT INTO FRIENDSHIP (USER_ID,FRIEND_ID) VALUES " +
                "(?,?)";
        jdbcTemplate.update(insertSql, userId, friendId);
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        String deleteSql = "DELETE FROM FRIENDSHIP WHERE USER_ID=" + userId + " and FRIEND_ID=" + friendId;
        jdbcTemplate.execute(deleteSql);
    }

    @Override
    public List<User> getFriends(int userId) {
        String sql = "SELECT f.FRIEND_ID FROM FRIENDSHIP f where f.USER_ID = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), userId);
    }

    @Override
    public List<Integer> getFriendsIntersection(int userId, int friendId) {
        return null;
    }

    private User makeUser(ResultSet rs) throws SQLException {
        int id = rs.getInt("FRIEND_ID");
        return getUserById(id);
    }

}
