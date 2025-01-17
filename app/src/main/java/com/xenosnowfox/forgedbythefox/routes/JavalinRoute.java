package com.xenosnowfox.forgedbythefox.routes;

import io.javalin.http.Handler;
import java.util.function.BiConsumer;

public interface JavalinRoute extends Handler {
    default JavalinRoute applyTo(BiConsumer<String, Handler> biConsumer, String path) {
        biConsumer.accept(path, this);
        return this;
    }
}
