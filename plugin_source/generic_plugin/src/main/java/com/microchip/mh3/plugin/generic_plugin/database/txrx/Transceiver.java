package com.microchip.mh3.plugin.generic_plugin.database.txrx;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.microchip.mh3.log.Log;
import com.microchip.mh3.plugin.generic_plugin.gui.HtmlPluginConfig;
//import com.microchip.mh3.plugin.generic_plugin.database.event.EventAgent;
import com.microchip.mh3.plugin.generic_plugin.gui.JFxWebBrowser;
import com.microchip.mh3.plugin.generic_plugin.database.plugin.PluginController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import javafx.application.Platform;
import javax.swing.SwingUtilities;

public class Transceiver {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Executor swingExecutor = SwingUtilities::invokeLater;
    private final Executor javaFXExecutor = Platform::runLater;
//    private final JFxWebBrowser browserObject;

    private final ControllerMapper controllerMapper;

//    private final JxBrowserConnector jxBrowserConnector;
//    private final Supplier<Connector> connectorFactory;
    private final Function<Consumer<String>, Connector> connectorFactory;
//    private final Connector connector;
    private final Map<String, Connector> connectors = new HashMap<>();
//    private final EventAgent eventAgent;

//    public Transceiver(JFxWebBrowser browserObject) {
    public Transceiver(Function<Consumer<String>, Connector> connectorFactory) {
        Log.write("Generic Plugin", Log.Severity.Info, "Constructing Transceiver");
        this.controllerMapper = new ControllerMapper(this::send);

//        this.browserObject = browserObject;
        this.connectorFactory = connectorFactory;
        Connector connector = connectorFactory.apply(this::receive);
        this.connectors.put(Connector.generateUUID(), connector);
//        this.eventAgent = new EventAgent(this::send);
//        this.jxBrowserConnector = new JxBrowserConnector(this, this.browserObject);

    }

    public void send(Response response) {
        String responseBody = new Gson().toJson(response);
//        Log.write("Server Transceiver", Log.Severity.Info, "Sending response : " + responseBody + ", Thread Name : " + Thread.currentThread().getName()); // TODO: how to avoid creating new Gson object everytime.

        Connector connector = connectors.get(response.getUuid());
        if (connector == null) {
            Log.write("Server Transceiver", Log.Severity.Error, "Connector Not Found. Unable to send Response : " + responseBody); // TODO: how to avoid creating new Gson object everytime.
        } else {
            connector.send(responseBody);
        }
    }

    public void receiveNew(String requestBody) {
        Log.write("Server Transceiver", Log.Severity.Info, "Request received New : " + requestBody);

        JsonObject requestJson = new Gson().fromJson(requestBody, JsonObject.class);

        String uuid = requestJson.get("uuid").getAsString();
        long sequenceNumber = requestJson.get("sequenceNumber").getAsLong();

        final Request request;
        try {
            request = new Gson().fromJson(requestBody, Request.class);
        } catch (JsonSyntaxException ex) {
            this.send(Response.error("Request Format Error: " + ex.getMessage(), requestBody)); // TODO how to get uuid if the parsing itself failed.
            return;
        }

        RequestHandler requestHandler = controllerMapper.getRequestHandler(request);
        if (requestHandler == null) {
            Log.write("Server Transceiver", Log.Severity.Error, "Unable to find request handler for the request: " + requestBody);
            Response response = Response.error("Unable to find request handler for the request", requestBody);
            response.setUuid(request.getUuid());
            response.setSequenceNumber(request.getSequenceNumber());
            this.send(response);
            return;
        }

        CompletableFuture.supplyAsync(() -> requestHandler.handle(request), javaFXExecutor)
                .thenApply(response -> {
                    response.setUuid(request.getUuid());
                    response.setSequenceNumber(request.getSequenceNumber());
                    return response;
                })
                .thenAcceptAsync(this::send, executorService);

    }

    public void receive(String requestBody) {
//        Log.write("Server Transceiver", Log.Severity.Info, "Request received : " + requestBody + ", Thread Name : " + Thread.currentThread().getName());
        final Request request;
        try {
            request = new Gson().fromJson(requestBody, Request.class);
        } catch (JsonSyntaxException ex) {
            this.send(Response.error("Request Format Error: " + ex.getMessage(), requestBody)); // TODO how to get uuid if the parsing itself failed.
            return;
        }

        RequestHandler requestHandler = controllerMapper.getRequestHandler(request);
        if (requestHandler == null) {
            Log.write("Server Transceiver", Log.Severity.Error, "Unable to find request handler for the request: " + requestBody);
            Response response = Response.error("Unable to find request handler for the request", request);
            response.setUuid(request.getUuid());
            response.setSequenceNumber(request.getSequenceNumber());
            this.send(response);
            return;
        }

        CompletableFuture.supplyAsync(() -> requestHandler.handle(request), javaFXExecutor)
                .thenApply(response -> {
                    response.setUuid(request.getUuid());
                    response.setSequenceNumber(request.getSequenceNumber());
                    return response;
                })
                .thenAcceptAsync(this::send, executorService);

    }

//    public JxBrowserConnector getJxBrowserConnector() {
//        return this.jxBrowserConnector;
//    }
    public void destroy() {
        Log.write("Transceiver", Log.Severity.Info, "being destroyed");
        this.controllerMapper.destroy();
//        this.eventAgent.destroy();
        executorService.shutdownNow();
        try {
            executorService.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException ie) {
            Log.write("Generic Plugin", Log.Severity.Error, "Unable to terminate Transceiver's excutorService thread.");
            Log.printException(ie);
            executorService.shutdownNow();
        }
    }

    public void addPluginConfig(HtmlPluginConfig htmlPluginConfig) {
        controllerMapper.addPluginConfig(htmlPluginConfig);
    }
}
