package com.microchip.mh3.plugin.generic_plugin_manager.javafx;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;

public class FxSupport {

    private static boolean initialized = false;

    public static void enable() {
        if (initialized) {
            return;
        }

        new JFXPanel();
        Platform.setImplicitExit(false);

        initialized = true;
    }

    public static boolean isEnabled() {
        return initialized;
    }

}
