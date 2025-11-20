package com.example.sms;

import java.sql.*;

public class LoginDAO {

    private final String url = "jdbc:mysql://localhost:3306/sms_db?useUnicode=true&characterEncoding=utf8";
    private final String user = "root";
    private final String pass = "Rohit888399";

    public boolean validate(String username, String password) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pass);

            String sql = "SELECT * FROM users WHERE username=? AND password=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
