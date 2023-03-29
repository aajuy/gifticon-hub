package com.hub.gifticon.domain.user;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public interface UserRepository {
    public User add(User user);
    public User findById(String userId);
    public void close(Connection con, Statement stmt, ResultSet rs);
}
