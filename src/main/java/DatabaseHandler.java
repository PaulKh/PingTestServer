import model.Ping;

import java.net.UnknownHostException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul on 05/07/15.
 */
public class DatabaseHandler {
    private static DatabaseHandler databaseHandler;

    public static synchronized DatabaseHandler instance() {
        if (databaseHandler == null) {
            databaseHandler = new DatabaseHandler();
        }
        return databaseHandler;
    }

    public Connection getConnection() {
        Connection connection = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:ping_database.db");
            System.out.println("Opened database successfully");
            stmt = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS PING " +
                    " (SOURCE_IP           TEXT    NOT NULL," +
                    " DESTINATION_IP           TEXT    NOT NULL, " +
                    " PING_RESULT            TEXT     NOT NULL) ";
            stmt.executeUpdate(sql);
            stmt.close();
            return connection;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return null;
    }

    public List<Ping> getAll() {
        List<Ping> pings = new ArrayList<Ping>();
        Connection connection = getConnection();
        if (connection == null)
            return null;
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM PING;");
            while (rs.next()) {
                String result = rs.getString("PING_RESULT");
                String sourceIp = rs.getString("SOURCE_IP");
                String destinationIp = rs.getString("DESTINATION_IP");
                try {
                    pings.add(Ping.pingBuilder(result, sourceIp, destinationIp));
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }

            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pings;
    }

    public void addNewPing(Ping ping) {
        Connection connection = getConnection();
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            String sql = "INSERT INTO PING (SOURCE_IP,DESTINATION_IP,PING_RESULT) " +
                    "VALUES (\"" + ping.getSourceAddress().getHostAddress() + "\", \"" + ping.getDestinationAddress().getHostAddress() + "\", \"" + ping.getPingResult() + "\");";
            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
