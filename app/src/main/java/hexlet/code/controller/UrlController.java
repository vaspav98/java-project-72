package hexlet.code.controller;

import hexlet.code.dto.UrlPage;
import hexlet.code.dto.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.BaseRepository;
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
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class UrlController extends BaseRepository {

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

        String port = url.getPort() == -1 ? "" : ":" + url.getPort();
        String name = url.getProtocol() + "://" + url.getHost() + port;
        if (UrlRepository.checkRecordExist(name)) {
            ctx.sessionAttribute("flash", "Страница уже существует");
            ctx.sessionAttribute("flashType", "alert-danger");
            ctx.redirect("/");
            return;
        }

        Date date = new Date();
        Timestamp createdAt = new Timestamp(date.getTime());
        Url newUrl = new Url(name, createdAt);
        UrlRepository.save(newUrl);
        ctx.sessionAttribute("flash", "Страница успешно добавлена");
        ctx.sessionAttribute("flashType", "alert-success");
        ctx.redirect("/urls");
    };

    public static Handler listUrls = ctx -> {
        List<Url> urls = UrlRepository.getListUrls();
        List<UrlCheck> urlChecks = UrlCheckRepository.getListRecentUrlCheck();
        UrlsPage page = new UrlsPage(urls, urlChecks);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flashType"));
        ctx.render("allUrls.jte", Collections.singletonMap("page", page));
    };

    public static Handler show = ctx -> {
        long id = ctx.pathParamAsClass("id", Long.class).get();
        Url url = UrlRepository.find(id);
        if (url == null) {
            throw new NotFoundResponse("Url not found");
        }
        List<UrlCheck> urlChecks = UrlCheckRepository.getListUrlCheck(id);
        UrlPage page = new UrlPage(url, urlChecks);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flashType"));
        ctx.render("show.jte", Collections.singletonMap("page", page));
    };

    public static Handler check = ctx -> {
        long urlId = ctx.pathParamAsClass("id", Long.class).get();
        String url = UrlRepository.find(urlId).getName();
        HttpResponse<String> response;
        try {
            response = Unirest.get(url).asString();
        } catch (UnirestException e) {
            ctx.sessionAttribute("flash", "Connect to " + url + " failed");
            ctx.sessionAttribute("flashType", "alert-danger");
            ctx.redirect("/urls/" + urlId);
            return;
        }

        String body =  response.getBody();
        Document doc = Jsoup.parse(body);
        int statusCode = response.getStatus();
        String title = doc.title();
        Element h1Temp = doc.selectFirst("h1");
        String h1 = null;
        if (h1Temp != null) {
            h1 = h1Temp.text();
        }
        Element descriptionTemp = doc.selectFirst("meta[name=description]");
        String description = null;
        if (descriptionTemp != null) {
            description = descriptionTemp.attr("content");
        }
        Date date = new Date();
        Timestamp createdAt = new Timestamp(date.getTime());

        UrlCheck urlCheck = new UrlCheck(statusCode, title, h1, description, urlId, createdAt);
        UrlCheckRepository.save(urlCheck);

        ctx.sessionAttribute("flash", "Страница успешно проверена");
        ctx.sessionAttribute("flashType", "alert-success");
        ctx.redirect("/urls/" + urlId);
    };

}
