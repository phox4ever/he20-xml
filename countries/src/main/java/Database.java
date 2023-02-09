import java.sql.*;
import java.util.ArrayList;

public class Database {
    static final String JDBC_DRIVER = "com.mariadb.jdbc.Driver";
    static final String DB_URL = "jdbc:mariadb://localhost:3306/db";

    private Connection conn = null;
    Database() throws SQLException {
        this.conn = DriverManager.getConnection(
                Database.DB_URL,
                "root", "root");
    }

    public int writeDB(String table, ArrayList columns, ArrayList values) {
        String columnsString = "";
        String params = "";
        for (int i = 0; i < columns.size(); i++) {
            params += "?";
            columnsString += "`" + columns.get(i) + "`" ;
            if (i != columns.size() - 1) {
                params += ", ";
                columnsString += ", ";
            }
        }

        try (PreparedStatement statement = conn.prepareStatement(
        "INSERT INTO " + table + " (" + columnsString + ")" +
        " VALUES (" + params + ")"
      )) {
            for (int i = 0; i < columns.size(); i++) {
                statement.setString(i + 1, values.get(i).toString());
            }
            System.out.println(statement);
            return statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getSQLState());
            throw new RuntimeException(e);
        }
    }

    public void close() throws SQLException {
        if (conn != null) {
            conn.close();
        }
    }
}
