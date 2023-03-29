package com.hub.gifticon.domain.gifticon;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@RequiredArgsConstructor
public class GifticonRepositoryJdbc implements GifticonRepository {
    private final DataSource dataSource;
    private static AtomicLong numOfUsers = new AtomicLong();

    @Override
    public Gifticon add(Gifticon gifticon) {
        String sql = "insert into gifticon(gifticon_id, gifticon_name, expiration_date, is_used, image_filename) values(?, ?, ?, ?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        // set gifticon id
        Long id = numOfUsers.incrementAndGet();
        gifticon.setGifticonId(id);

        // set gifticon is_used
        gifticon.setIsUsed(false);

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, gifticon.getGifticonId());
            pstmt.setString(2, gifticon.getGifticonName());
            pstmt.setDate(3, Date.valueOf(gifticon.getExpirationDate()));  // date 변환
            pstmt.setBoolean(4, gifticon.getIsUsed());
            pstmt.setString(5, gifticon.getImageFilename());
            pstmt.executeUpdate();
            return gifticon;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            close(con, pstmt, null);
        }
    }

    @Override
    public Gifticon findById(Long gifticonId) {
        String sql = "select * from gifticon where gifticon_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, gifticonId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                Gifticon gifticon = createGifticonFrom(rs);
                return gifticon;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            close(con, pstmt, rs);
        }
    }

    @Override
    public List<Gifticon> findAll() {
        String sql = "select * from gifticon";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            List<Gifticon> gifticonList = new ArrayList<>();

            while (rs.next()) {
                Gifticon gifticon = createGifticonFrom(rs);
                gifticonList.add(gifticon);
            }
            return gifticonList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            close(con, pstmt, rs);
        }
    }

    private static Gifticon createGifticonFrom(ResultSet rs) throws SQLException {
        Gifticon gifticon = new Gifticon();
        gifticon.setGifticonId(rs.getLong("gifticon_id"));
        gifticon.setGifticonName(rs.getString("gifticon_name"));
        gifticon.setExpirationDate(rs.getDate("expiration_date").toLocalDate());  // date 변환
        gifticon.setIsUsed(rs.getBoolean("is_used"));
        gifticon.setImageFilename(rs.getString("image_filename"));
        return gifticon;
    }

    @Override
    public void update(Long gifticonId, Gifticon gifticonParam) {
        String sql = "update gifticon set gifticon_name = ?, expiration_date = ?, image_filename = ? where gifticon_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, gifticonParam.getGifticonName());
            pstmt.setDate(2, Date.valueOf(gifticonParam.getExpirationDate()));
            pstmt.setString(3, gifticonParam.getImageFilename());
            pstmt.setLong(4, gifticonId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(con, pstmt, null);
        }
    }

    @Override
    public void delete(Long gifticonId) {
        String sql = "delete from gifticon where gifticon_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, gifticonId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(con, pstmt, null);
        }
    }

    @Override
    public void use(Long gifticonId) {
        String sql = "update gifticon set is_used = ? where gifticon_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setBoolean(1, true);
            pstmt.setLong(2, gifticonId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(con, pstmt, null);
        }
    }

    @Override
    public void close(Connection con, Statement stmt, ResultSet rs) {
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        JdbcUtils.closeConnection(con);
    }
}
