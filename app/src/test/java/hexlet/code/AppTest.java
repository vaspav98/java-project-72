package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import okhttp3.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

public class AppTest {
    Javalin app;

    @BeforeEach
    public final void setUp() throws SQLException, IOException {
        app = App.getApp();
    }

    @Test
    public void testShowMainPage() {
        JavalinTest.test(app, ((server, client) -> {
            Response response = client.get("/");
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("<h1 class=\"display-3 mb-0\">Анализатор страниц</h1>");
        }));
    }
    @Test
    public void testCreateUrl() {
        JavalinTest.test(app, ((server, client) -> {
            var requestBody = "url=https://vk.com/id486745806ergwerg";
            Response response = client.post("/urls", requestBody);
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("<a href=\"/urls/1\">https://vk.com</a>");

            Response response2 = client.post("/urls", requestBody);
            assertThat(response2.code()).isEqualTo(200);
            assertThat(response2.body().string()).contains("<h1 class=\"display-3 mb-0\">Анализатор страниц</h1>");

            assertThat(UrlRepository.checkRecordExist("https://vk.com")).isTrue();
        }));
    }

    @Test
    public void testListUrls() {
        JavalinTest.test(app, ((server, client) -> {
            Response response = client.get("/urls");
            assertThat(response.code()).isEqualTo(200);
        }));
    }

    @Test
    public void testShow() {
        JavalinTest.test(app, ((server, client) -> {
            Date date = new Date();
            Timestamp createdAt = new Timestamp(date.getTime());
            var url = new Url(1, "https://vk.com", createdAt);
            UrlRepository.save(url);
            Response response = client.get("/urls/" + url.getId());
            assertThat(response.code()).isEqualTo(200);
        }));
    }

    @Test
    public void testUrlNotFound() {
        JavalinTest.test(app, ((server, client) -> {
            Response response = client.get("/urls/1");
            assertThat(response.code()).isEqualTo(404);
        }));
    }

    @Test
    public void textCheck() {
        JavalinTest.test(app, ((server, client) -> {
            Date date = new Date();
            Timestamp createdAt = new Timestamp(date.getTime());
            var url = new Url(1, "https://vk.com", createdAt);
            UrlRepository.save(url);

            Response response = client.post("/urls/1/checks");
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains(
                    "<td>" + UrlCheckRepository.getListUrlCheck(1).get(0).getCreatedAt().toString() + "</td>");

            Response response2 = client.post("/urls/1/checks");
            assertThat(response2.code()).isEqualTo(200);
            assertThat(response2.body().string()).contains(
                    "<td>" + UrlCheckRepository.getListUrlCheck(1).get(1).getCreatedAt().toString() + "</td>");
        }));
    }

}
