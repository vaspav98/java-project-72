package hexlet.code;

import io.javalin.Javalin;

public class App {

    public static Javalin getApp() {

        Javalin app = Javalin.create(config -> {
            // Включаем логгирование
            config.plugins.enableDevLogging();
        });

        app.get("/", ExampleController.hello);

        return app;
    }

    public static void main(String[] args) {
        Javalin app = getApp();
        app.start();
    }

}
