package com.microchip.mh3.plugin.generic_plugin.database.txrx;

import com.microchip.mh3.log.Log;
import com.microchip.mh3.plugin.generic_plugin.gui.JFxWebBrowser;
import com.teamdev.jxbrowser.js.JsAccessible;
import java.util.function.Consumer;

@JsAccessible
public class JxBrowserConnector implements Connector {

    private final Consumer<String> receiver;
    private final JFxWebBrowser browserObject;

    public JxBrowserConnector(Consumer<String> receiver, JFxWebBrowser browserObject) {
        Log.write("Generic Plugin", Log.Severity.Info, "Constructing JxBrowserConnector");
        this.browserObject = browserObject;
        this.receiver = receiver;
    }

    @Override
    public void send(String response) {
        String script = "clientJxBrowserConnectorReceive('" + response + "')";
        try {
            String objectType = browserObject.getFrame().executeJavaScript("typeof clientJxBrowserConnectorReceive");
            if ("function".equals(objectType)) {
                browserObject.getFrame().executeJavaScript(script);
            } else {
                Log.write("Generic Plugin", Log.Severity.Info, "Ignorable Warning: Undefined : clientJxBrowserConnectorReceive. " + script);
            }
        } catch (IllegalStateException ex) {
            Log.write("Generic Plugin", Log.Severity.Info, "Reloading View. Unable to excute javascript api : " + script);
        } catch (Exception ex) {
            Log.write("Generic Plugin", Log.Severity.Warning, "Unable to excute javascript api : " + script);
            Log.printException(ex);
        }
    }

    @JsAccessible
    @Override
    public void receive(String request) {
        this.receiver.accept(request);
    }

}
