package org.edwith.webbe.cardmanager.dao;

import org.edwith.webbe.cardmanager.dto.BusinessCard;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BusinessCardManagerDao {

    public List<BusinessCard> searchBusinessCard(String keyword) {
        List<BusinessCard> list = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement ps = conn
                     .prepareStatement("select * from cards where name like ?");) {
            ps.setString(1, "%" + keyword + "%");
            getCards(list, ps);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private void getCards(List<BusinessCard> list, PreparedStatement ps) throws SQLException {
        try (ResultSet rs = ps.executeQuery();) {
            while (rs.next()) {
                BusinessCard vo = new BusinessCard(rs.getString(1), rs.getString(2),
                        rs.getString(3), rs.getTimestamp(4).toLocalDateTime());
                list.add(vo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addBusinessCard(BusinessCard businessCard) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("insert into cards " +
                     "(name,phone,company_name,created_at) values(?,?,?,?)");) {
            ps.setString(1, businessCard.getName());
            ps.setString(2, businessCard.getPhone());
            ps.setString(3, businessCard.getCompanyName());
            ps.setDate(4, java.sql.Date.valueOf(businessCard.getCreateDate().toLocalDate()));
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        String url = "jdbc:mysql://localhost:3306/card_manager?&serverTimezone=UTC";
        String user = "root";
        String password = "";
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
}
