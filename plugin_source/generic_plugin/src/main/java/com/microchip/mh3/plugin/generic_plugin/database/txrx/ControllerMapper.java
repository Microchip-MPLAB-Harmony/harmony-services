package com.microchip.mh3.plugin.generic_plugin.database.txrx;

import com.microchip.mh3.log.Log;
import com.microchip.mh3.plugin.generic_plugin.commons.log.LoggingController;
import com.microchip.mh3.plugin.generic_plugin.commons.shortnames.ShortNamesController;
import com.microchip.mh3.plugin.generic_plugin.database.symbol.controller.SymbolController;
import com.microchip.mh3.plugin.generic_plugin.database.symbol.controller.BooleanSymbolController;
import com.microchip.mh3.plugin.generic_plugin.database.symbol.controller.ConfigSymbolController;
import com.microchip.mh3.plugin.generic_plugin.database.symbol.controller.IntegerSymbolController;
import com.microchip.mh3.plugin.generic_plugin.database.symbol.controller.FloatSymbolController;
import com.microchip.mh3.plugin.generic_plugin.database.symbol.controller.KeyValueSetSymbolController;
import com.microchip.mh3.plugin.generic_plugin.database.symbol.controller.VisibleSymbolController;
import com.microchip.mh3.plugin.generic_plugin.database.event.EventController;
import com.microchip.mh3.plugin.generic_plugin.database.symbol.controller.ComboSymbolController;
import com.microchip.mh3.plugin.generic_plugin.database.symbol.controller.CommentSymbolController;
import com.microchip.mh3.plugin.generic_plugin.database.symbol.controller.StringSymbolController;
import com.microchip.mh3.plugin.generic_plugin.database.symbol.controller.SymbolUtilController;
import com.microchip.mh3.plugin.generic_plugin.database.symbol.controller.LongSymbolController;
import com.microchip.mh3.plugin.generic_plugin.database.symbol.controller.HexSymbolController;
import com.microchip.mh3.plugin.generic_plugin.gui.HtmlPluginConfig;
import com.microchip.mh3.plugin.generic_plugin.database.plugin.PluginController;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ControllerMapper {

    private final Map<RequestKey, RequestHandler> controllers = new HashMap<>();

    private final EventController eventController;
    private final PluginController pluginController;

    public ControllerMapper(Consumer<Response> transmitter) {
        register(new SymbolUtilController());
        register(new SymbolController());
        register(new VisibleSymbolController());
        register(new ConfigSymbolController());
        register(new BooleanSymbolController());
        register(new IntegerSymbolController());
        register(new FloatSymbolController());
        register(new ComboSymbolController());
        register(new KeyValueSetSymbolController());
        register(new CommentSymbolController());
        register(new StringSymbolController());

        register(new LoggingController());

        register(new ShortNamesController());
        register(new LongSymbolController());
        register(new HexSymbolController());

        eventController = new EventController(transmitter);
        register(eventController);

        pluginController = new PluginController();
        register(pluginController);
    }

    private void register(Object controllerObject) {
        Class<?> controllerClass = controllerObject.getClass();
        final String path = controllerClass.getAnnotation(ControllerPath.class).value();
        Objects.requireNonNull(path, "no path mentioned for controller class : " + controllerClass.getName());

        Stream.of(controllerClass.getMethods())
                .filter(method -> method.isAnnotationPresent(ControllerMethod.class))
                .filter(method -> method.getReturnType().equals(Response.class))
                .filter(method -> method.getParameterCount() > 0)
                .filter(method -> method.getParameterTypes()[0] == Request.class)
                .forEach(method -> this.addControllerMethod(path, controllerObject, method));

    }

    private void addControllerMethod(final String path, final Object controllerObject, Method method) {
        String methodName = method.getAnnotation(ControllerMethod.class).value();
        if (methodName == null || methodName.isEmpty()) {
            methodName = method.getName();
        }

        controllers.put(new RequestKey(path, methodName), new RequestHandler(controllerObject, method));
    }

    public RequestHandler getRequestHandler(Request request) {
        return controllers.get(new RequestKey(request));
    }

    public void destroy() {
        this.eventController.destroy();
    }

    public void addPluginConfig(HtmlPluginConfig htmlPluginConfig) {
        pluginController.addPluginConfig(htmlPluginConfig);
    }

    private void printApiDocumentation() {
        Comparator<Map.Entry<RequestKey, RequestHandler>> keyComparator = Map.Entry.comparingByKey(Comparator.comparing(RequestKey::getPath).thenComparing(RequestKey::getMethod));
        String apiDocumentation = controllers.entrySet().stream().sorted(keyComparator).map(e -> e.getValue().getRequestFormat()).collect(Collectors.joining("\n\n"));

        Log.write("Generic Plugin", Log.Severity.Info, "API Documentation : \n" + apiDocumentation);
    }
}
