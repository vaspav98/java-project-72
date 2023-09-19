package hexlet.code.repository;

import hexlet.code.model.UrlCheck;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class UrlCheckRepository extends BaseRepository {

    public static void save(UrlCheck urlCheck) throws SQLException {
        String sql = "INSERT INTO url_checks (created_at, url_id) VALUES (?, ?)";
        try (Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setTimestamp(1, urlCheck.getCreatedAt());
            preparedStatement.setLong(2, urlCheck.getUrlId());
            preparedStatement.executeUpdate();
        }
    }

    public static List<UrlCheck> getListUrlCheck(long urlId) throws SQLException {
        String sql = "SELECT * FROM url_checks WHERE url_id = ?";
        List<UrlCheck> urlChecks = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, urlId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                Timestamp createdAt = resultSet.getTimestamp("created_at");
                UrlCheck urlCheck = new UrlCheck();
                urlCheck.setId(id);
                urlCheck.setUrlId(urlId);
                urlCheck.setCreatedAt(createdAt);
                urlChecks.add(urlCheck);
            }
            return urlChecks;
        }
    }

    public static List<UrlCheck> getListRecentUrlCheck() throws SQLException {
        String sql = "SELECT url_id, MAX(created_at) AS max FROM url_checks GROUP BY url_id";
        List<UrlCheck> urlChecks = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                long urlId = resultSet.getLong("url_id");
                Timestamp createdAt = resultSet.getTimestamp("max");
                UrlCheck urlCheck = new UrlCheck();
                urlCheck.setUrlId(urlId);
                urlCheck.setCreatedAt(createdAt);
                urlChecks.add(urlCheck);
            }
            return urlChecks;
        }
    }
}
