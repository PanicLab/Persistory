package com.paniclab.persistory;

import com.paniclab.persistory.domainTest.Toy;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.*;
import static org.junit.Assert.*;

/**
 * Created by Сергей on 04.06.2017.
 */
public class DBTest {
    static final String DRIVER = "org.h2.Driver";
    static final String URL = "jdbc:h2:~/persistory_test";
    Connection connection;

    @Before
    public void setUp() throws Exception {

        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        connection = DriverManager.getConnection(URL);

    }

    @After
    public void tearDown() throws Exception {
        try {
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void database_existance_test() throws Exception {

        Statement statement = connection.createStatement();

        String sql = "CREATE TABLE IF NOT EXISTS TOYS (" +
                "NAME VARCHAR(255) NOT NULL UNIQUE ," +
                "VENDOR VARCHAR(255) DEFAULT 'UNKNOWN'," +
                "PRICE NUMERIC (15,2));";

        statement.executeUpdate(sql);

        DatabaseMetaData dmd = connection.getMetaData();
        ResultSet res = dmd.getTables(null, null, "TOYS",
                new String[] {"TABLE"});

        assertTrue(res.next());
        assertEquals("TOYS", res.getString("TABLE_NAME"));
        assertFalse(res.next());
        res.close();

        sql = "DROP TABLE TOYS";
        statement.executeUpdate(sql);

        res = dmd.getTables(null, null, "TOYS",
                new String[] {"TABLE"});

        assertFalse(res.next());

        res.close();
        statement.close();
    }

    @Test
    public void database_insert() throws Exception {
        Statement statement = connection.createStatement();

        String sql = "CREATE TABLE IF NOT EXISTS TOYS (" +
                "NAME VARCHAR(255) NOT NULL UNIQUE ," +
                "VENDOR VARCHAR(255) DEFAULT 'UNKNOWN'," +
                "PRICE NUMERIC (15,2));";

        Toy djeburgashka = new Toy();
        djeburgashka.setName("djeburgashka");
        djeburgashka.setVendor("russia");
        djeburgashka.setPrice(new BigDecimal(700, new MathContext(15)).setScale(2, BigDecimal.ROUND_HALF_UP));

        statement.executeUpdate(sql);

        DatabaseMetaData dmd = connection.getMetaData();
        ResultSet res = dmd.getTables(null, null, "TOYS",
                new String[] {"TABLE"});

        assertTrue(res.next());
        res.close();

        sql = String.format("INSERT INTO TOYS(NAME, VENDOR, PRICE)" +
                " VALUES('%s', '%s', %s)", djeburgashka.getName(), djeburgashka.getVendor(),
                djeburgashka.getPrice());

        statement.executeUpdate(sql);

        sql =  "SELECT * FROM TOYS WHERE NAME='djeburgashka'";
        statement.executeQuery(sql);

        Toy fromDb = new Toy();
        ResultSet rs = statement.getResultSet();
        while (rs.next()) {
            fromDb.setName(rs.getString("NAME"));
            fromDb.setVendor(rs.getString("VENDOR"));
            fromDb.setPrice(rs.getBigDecimal("PRICE"));
        }
        System.out.println("djeburgashka: ");
        System.out.println("NAME: " + djeburgashka.getName());
        System.out.println("VENDOR: " + djeburgashka.getVendor());
        System.out.println("PRICE: " + djeburgashka.getPrice());
        System.out.println();

        System.out.println("fromDb: ");
        System.out.println("NAME: " + fromDb.getName());
        System.out.println("VENDOR: " + fromDb.getVendor());
        System.out.println("PRICE: " + fromDb.getPrice());
        System.out.println();
        assertEquals(djeburgashka, fromDb);

        rs.close();
        statement.close();
    }
}
