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
        String sql = "INSERT INTO url_checks (status_code, title, h1, description, url_id, created_at)"
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, urlCheck.getStatusCode());
            preparedStatement.setString(2, urlCheck.getTitle());
            preparedStatement.setString(3, urlCheck.getH1());
            preparedStatement.setString(4, urlCheck.getDescription());
            preparedStatement.setLong(5, urlCheck.getUrlId());
            preparedStatement.setTimestamp(6, urlCheck.getCreatedAt());
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
                int statusCode = resultSet.getInt("status_code");
                String title = resultSet.getString("title");
                String h1 = resultSet.getString("h1");
                String description = resultSet.getString("description");
                Timestamp createdAt = resultSet.getTimestamp("created_at");
                UrlCheck urlCheck = new UrlCheck(id, statusCode, title, h1, description, urlId, createdAt);
                urlChecks.add(urlCheck);
            }
            return urlChecks;
        }
    }

    public static List<UrlCheck> getListRecentUrlCheck() throws SQLException {
        String sql = "SELECT url_id, status_code, MAX(created_at) AS max FROM url_checks GROUP BY url_id, status_code";
        List<UrlCheck> urlChecks = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                UrlCheck urlCheck = new UrlCheck();
                urlCheck.setUrlId(resultSet.getLong("url_id"));
                urlCheck.setStatusCode(resultSet.getInt("status_code"));
                urlCheck.setCreatedAt(resultSet.getTimestamp("max"));
                urlChecks.add(urlCheck);
            }
            return urlChecks;
        }
    }
}
