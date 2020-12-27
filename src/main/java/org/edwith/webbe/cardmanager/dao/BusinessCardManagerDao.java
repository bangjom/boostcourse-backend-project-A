package org.edwith.webbe.cardmanager.dao;

import org.edwith.webbe.cardmanager.dto.BusinessCard;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BusinessCardManagerDao {
    private static Connection conn;
    private static PreparedStatement ps;
    private static ResultSet rs;

    public List<BusinessCard> searchBusinessCard(String keyword) {
        List<BusinessCard> list = new ArrayList<>();
        conn = getConnection();
        try {
            ps = conn.prepareStatement("select * from cards where name like ?");
            ps.setString(1, "%" + keyword + "%");
            rs = ps.executeQuery();
            getCards(list);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close(conn, ps, rs);
        return list;
    }

    private void getCards(List<BusinessCard> list) throws SQLException {
        while (rs.next()) {
            BusinessCard vo = new BusinessCard(rs.getString(1), rs.getString(2), rs.getString(3));
            list.add(vo);
        }
    }

    public void addBusinessCard(BusinessCard businessCard) {
        conn = getConnection();
        try {
            ps = conn.prepareStatement("insert into cards " +
                    "(name,phone,company_name,created_at) values(?,?,?,?)");
            ps.setString(1, businessCard.getName());
            ps.setString(2, businessCard.getPhone());
            ps.setString(3, businessCard.getCompanyName());
            ps.setDate(4, new java.sql.Date(businessCard.getCreateDate().getTime()));
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close(conn, ps, rs);
    }

    public static Connection getConnection() {
        String url = "jdbc:mysql://localhost:3306/card_manager";
        String user = "root";
        String password = "ssu";
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static void close(Connection conn, PreparedStatement ps, ResultSet rs) {
        closePreparedStatement(ps);
        closeConnection(conn);
        closeResultSet(rs);
    }

    private static void closeResultSet(ResultSet rs) {
        Optional.ofNullable(rs)
                .ifPresent(resultSet -> {
                    try {
                        resultSet.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
    }

    private static void closeConnection(Connection conn) {
        Optional.ofNullable(conn)
                .ifPresent(connection -> {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
    }

    private static void closePreparedStatement(PreparedStatement ps) {
        Optional.ofNullable(ps)
                .ifPresent(preparedStatement -> {
                    try {
                        preparedStatement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
    }
}
