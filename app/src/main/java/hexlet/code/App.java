package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.ResourceCodeResolver;
import hexlet.code.repository.BaseRepository;
import hexlet.code.controller.RootController;
import hexlet.code.controller.UrlController;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

public class App {

    public static Javalin getApp() throws SQLException, IOException {

        HikariConfig hikariConfig = new HikariConfig();
        String jdbsUrl = "jdbc:h2:mem:vaspav";
        if (System.getenv("JDBC_DATABASE_URL") != null) {
            jdbsUrl = System.getenv("JDBC_DATABASE_URL");
            hikariConfig.setUsername(System.getenv("JDBC_DATABASE_USERNAME"));
            hikariConfig.setPassword(System.getenv("JDBC_DATABASE_PASSWORD"));
        }
        hikariConfig.setJdbcUrl(jdbsUrl);
        HikariDataSource dataSource = new HikariDataSource(hikariConfig);
        String sql = getContentFromStream(getFileFromResourceAsStream("schema.sql"));
//        var url = App.class.getClassLoader().getResource("schema.sql");
//        var file = new File(url.getFile());
//        var sql = Files.lines(file.toPath())
//                .collect(Collectors.joining("\n"));
//        String sql = Files.readString(Paths.get("src", "main", "resources", "schema.sql"));
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
        BaseRepository.dataSource = dataSource;

        Javalin app = Javalin.create(config -> {
            config.plugins.enableDevLogging();
            JavalinJte.init(createTemplateEngine());
        });

        app.get("/", RootController.showMainPage);
        app.post("/urls", UrlController.createUrl);
        app.get("/urls", UrlController.listUrls);
        app.get("urls/{id}", UrlController.show);
        app.post("urls/{id}/checks", UrlController.check);

        return app;
    }

    public static TemplateEngine createTemplateEngine() {
        ClassLoader classLoader = App.class.getClassLoader();
        ResourceCodeResolver codeResolver =  new ResourceCodeResolver("templates", classLoader);
        TemplateEngine templateEngine = TemplateEngine.create(codeResolver, ContentType.Html);
        return templateEngine;
    }

    private static InputStream getFileFromResourceAsStream(String fileName) {
        ClassLoader classLoader = App.class.getClassLoader();
        InputStream is = classLoader.getResourceAsStream(fileName);
        return is;
    }

    private static String getContentFromStream(InputStream is) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }

    public static void main(String[] args) throws SQLException, IOException {
        Javalin app = getApp();
        app.start();
    }

}
