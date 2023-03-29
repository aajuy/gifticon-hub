package com.hub.gifticon.domain.gifticon;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public interface GifticonRepository {
    public Gifticon add(Gifticon gifticon);
    public Gifticon findById(Long gifticonId);
    public List<Gifticon> findAll();
    public void update(Long gifticonId, Gifticon updated);
    public void delete(Long gifticonId);
    public void use(Long gifticonId);
    public void close(Connection con, Statement stmt, ResultSet rs);
}
