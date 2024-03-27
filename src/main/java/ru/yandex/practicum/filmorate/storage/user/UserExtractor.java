package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class UserExtractor implements ResultSetExtractor<List<User>> {
    @Override
    public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Integer, User> users = new LinkedHashMap<>();

        while (rs.next()) {
            Integer userId = rs.getInt("user_id");
            User user = users.get(userId);

            if (user == null) {
                user = User.builder().build();
                users.put(userId, user);
            }

            user.setId(rs.getInt("user_id"));
            user.setName(rs.getString("user_name"));
            user.setEmail(rs.getString("user_email"));
            user.setBirthday(rs.getTimestamp("user_birthdate")
                    .toLocalDateTime().toLocalDate());
            user.setLogin(rs.getString("user_login"));

            Integer friendId = rs.getObject("friend_id", Integer.class);

            if (friendId != null) {
                Set<Integer> friends = user.getFriends();

                if (friends == null) {
                    friends = new LinkedHashSet<>();
                    user.setFriends(friends);
                }
                friends.add(friendId);
            }
        }

        return new ArrayList<>(users.values());
    }
}
