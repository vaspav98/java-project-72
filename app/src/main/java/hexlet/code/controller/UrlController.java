package hexlet.code.controller;

import hexlet.code.dto.UrlPage;
import hexlet.code.dto.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import io.javalin.http.Handler;
import io.javalin.http.NotFoundResponse;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class UrlController {

    public static Handler createUrl = ctx -> {
        String receivedUrl = ctx.formParam("url");
        URL url;
        try {
            url = new URL(receivedUrl);
        } catch (MalformedURLException exception) {
            ctx.sessionAttribute("flash", "Некорректный URL");
            ctx.sessionAttribute("flashType", "alert-danger");
            ctx.redirect("/");
            return;
        }
        String name = String.format("%s://%s", url.getProtocol(), url.getAuthority());

        if (UrlRepository.checkRecordExist(name)) {
            ctx.sessionAttribute("flash", "Страница уже существует");
            ctx.sessionAttribute("flashType", "alert-danger");
            ctx.redirect("/");
            return;
        }

        Url newUrl = new Url(name);
        UrlRepository.save(newUrl);
        ctx.sessionAttribute("flash", "Страница успешно добавлена");
        ctx.sessionAttribute("flashType", "alert-success");
        ctx.redirect("/urls");
    };

    public static Handler listUrls = ctx -> {
        List<Url> urls = UrlRepository.getListUrls();
        Map<Long, UrlCheck> urlChecks = UrlCheckRepository.getRecentUrlChecks();
        UrlsPage page = new UrlsPage(urls, urlChecks);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flashType"));
        ctx.render("allUrls.jte", Collections.singletonMap("page", page));
    };

    public static Handler show = ctx -> {
        long id = ctx.pathParamAsClass("id", Long.class).get();
        Url url = UrlRepository.findById(id).orElseThrow(() -> new NotFoundResponse("Url not found"));
        List<UrlCheck> urlChecks = UrlCheckRepository.getListUrlCheck(id);
        UrlPage page = new UrlPage(url, urlChecks);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flashType"));
        ctx.render("show.jte", Collections.singletonMap("page", page));
    };

    public static Handler check = ctx -> {
        long urlId = ctx.pathParamAsClass("id", Long.class).get();
        String url = UrlRepository.findById(urlId).get().getName();
        HttpResponse<String> response;
        try {
            response = Unirest.get(url).asString();
            String body =  response.getBody();
            Document doc = Jsoup.parse(body);
            int statusCode = response.getStatus();
            String title = doc.title();
            Element h1Temp = doc.selectFirst("h1");
            String h1 = h1Temp != null ? h1Temp.text() : null;
            Element descriptionTemp = doc.selectFirst("meta[name=description]");
            String description = descriptionTemp != null ? descriptionTemp.attr("content") : null;

            UrlCheck urlCheck = new UrlCheck(statusCode, title, h1, description);
            urlCheck.setUrlId(urlId);
            UrlCheckRepository.save(urlCheck);

            ctx.sessionAttribute("flash", "Страница успешно проверена");
            ctx.sessionAttribute("flashType", "alert-success");
        } catch (UnirestException e) {
            ctx.sessionAttribute("flash", "Connect to " + url + " failed");
            ctx.sessionAttribute("flashType", "alert-danger");
        } catch (Exception e) {
            ctx.sessionAttribute("flash", e.getMessage());
            ctx.sessionAttribute("flashType", "alert-danger");
        }
        ctx.redirect("/urls/" + urlId);
    };

}
