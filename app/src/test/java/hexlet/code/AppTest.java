package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import okhttp3.Response;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.nio.file.Path;

public class AppTest {
    Javalin app;

    private static Path getFixturePath(String fileName) {
        return Paths.get("src", "test", "resources", "fixtures", fileName).toAbsolutePath().normalize();
    }

    private static String readFixture(String fileName) throws IOException {
        Path filePath = getFixturePath(fileName);
        return Files.readString(filePath).trim();
    }

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
            String requestBody = "url=https://vk.com/id486745806ergwerg";
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
            var url = new Url("https://vk.com");
            url.setId(1);
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
    public void textCheck() throws IOException {
        MockWebServer mockServer = new MockWebServer();
        String mockUrl = mockServer.url("/").toString();
        MockResponse mockResponse = new MockResponse().setBody(readFixture("index.html"));
        mockServer.enqueue(mockResponse);
        JavalinTest.test(app, ((server, client) -> {
            String requestBody = "url=" + mockUrl;
            Response response = client.post("/urls", requestBody);
            assertThat(response.code()).isEqualTo(200);
            String formattedName = String.format("%s://%s", mockServer.url("/").url().getProtocol(),
                    mockServer.url("/").url().getAuthority());
            Url addedUrl = UrlRepository.findByName(formattedName).orElse(null);
            assertThat(addedUrl).isNotNull();
            assertThat(addedUrl.getName()).isEqualTo(formattedName);

            Response response2 = client.post("/urls/" + addedUrl.getId() + "/checks");
            assertThat(response2.code()).isEqualTo(200);
            UrlCheck addedUrlCheck = UrlCheckRepository.getListUrlCheck(addedUrl.getId()).get(0);
            assertThat(addedUrlCheck.getTitle()).isEqualTo("Калькулятор");
            assertThat(addedUrlCheck.getH1()).isEqualTo("Калькулятор");
            assertThat(addedUrlCheck.getDescription())
                    .isEqualTo("Мой калькулятор из дополнительных заданий на Хекслете");
        }));
    }

}
