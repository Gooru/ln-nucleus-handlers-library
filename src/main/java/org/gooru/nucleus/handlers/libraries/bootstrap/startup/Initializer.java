package org.gooru.nucleus.handlers.libraries.bootstrap.startup;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

/**
 * @author szgooru Created On: 26-May-2017
 */
public interface Initializer {
    void initializeComponent(Vertx vertx, JsonObject config);
}
