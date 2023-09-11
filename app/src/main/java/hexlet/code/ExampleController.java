package hexlet.code;

import io.javalin.http.Handler;

public class ExampleController {

    public static Handler hello = ctx -> {
        ctx.render("index.jte");
    };

}
