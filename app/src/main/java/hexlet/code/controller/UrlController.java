package hexlet.code.controller;

import hexlet.code.dto.UrlPage;
import hexlet.code.dto.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.repository.BaseRepository;
import hexlet.code.repository.UrlRepository;
import io.javalin.http.Handler;
import io.javalin.http.NotFoundResponse;
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
        UrlsPage page = new UrlsPage(urls);
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
        UrlPage page = new UrlPage(url);
        ctx.render("show.jte", Collections.singletonMap("page", page));
    };

}
