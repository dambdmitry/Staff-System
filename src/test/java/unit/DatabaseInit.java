package unit;

import org.internship.system.db.config.Config;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

public class DatabaseInit {
    public static void startNewDB() throws URISyntaxException, IOException {
        URL url = DatabaseInit.class.getClassLoader().getResource("staff_table.sql");
        List<String> str = Files.readAllLines(Paths.get(url.toURI()));
        String createTable = str.stream().collect(Collectors.joining());
        try(Connection connection = getConnection();
            Statement statement = connection.createStatement()) {
            statement.executeUpdate(createTable);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private static Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(
                Config.getProperty(Config.DB_URL),
                Config.getProperty(Config.DB_LOGIN),
                Config.getProperty(Config.DB_PASSWORD)
        );
        return connection;
    }
}
