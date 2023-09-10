package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.javalin.Javalin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class App {

    public static Javalin getApp() throws SQLException, IOException {

        HikariConfig hikariConfig = new HikariConfig();
        String jdbsUrl = "jdbc:h2:mem:vaspav";
        if (System.getenv("JDBC_DATABASE_URL") != null) {
            jdbsUrl = System.getenv("JDBC_DATABASE_URL");
            hikariConfig.setUsername(System.getenv("JDBC_DATABASE_USERNAME"));
            hikariConfig.setPassword("JDBC_DATABASE_PASSWORD");
        }
        hikariConfig.setJdbcUrl(jdbsUrl);

        HikariDataSource dataSource = new HikariDataSource(hikariConfig);

        String sql = Files.readString(Paths.get("src", "main", "resources", "schema.sql"));

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
        BaseRepository.dataSource = dataSource;


        Javalin app = Javalin.create(config -> {
            config.plugins.enableDevLogging();
        });

        app.get("/", ExampleController.hello);
        System.out.println();

        return app;
    }

    public static void main(String[] args) throws SQLException, IOException {
        Javalin app = getApp();
        app.start();
    }

}
