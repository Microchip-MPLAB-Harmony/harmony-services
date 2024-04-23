package com.microchip.mh3.plugin.generic_plugin.database.txrx;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.microchip.mh3.log.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.Value;

@Value
public class RequestHandler {

    private final Object controllerObject;
    private final Method method;

    private static final Gson GSON = new Gson();

    public Response handle(Request request) {
        if (!request.getData().isJsonObject()) {
            return Response.error("Illegal Arguments: Argument data must be an Object", request);
        }
        try {
            return (Response) method.invoke(controllerObject, fetchArgs(request));
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Log.write("Generic Plugin", Log.Severity.Error, "Exception while calling request handler. " + ex.getMessage() + ".\nRequest: " + request);
            return Response.error(ex.getMessage(), request);
        }
    }

    private Object[] fetchArgs(Request request) {
        JsonObject data = request.getData().getAsJsonObject();
        try {
            return Arrays.stream(method.getParameters())
                    .map(parameter -> {
                        if (parameter.getName().equals("request")) {
                            return request;
                        }

                        JsonElement element = data.get(parameter.getName());
                        if (element == null) {
                            throw new IllegalArgumentException(
                                    "Argument Not Found: " + parameter.getName() + "\n"
                                    + "Request Format:\n"
                                    + getRequestFormat()
                            );
                        }
                        return new Gson().fromJson(element, parameter.getType());
                    })
                    .toArray();
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new IllegalArgumentException("Request Format Exception: \n"
                    + "Request Format:\n"
                    + getRequestFormat()
            );
        }
    }

    public String getRequestFormat() {
        return "path: \"" + controllerObject.getClass().getAnnotation(ControllerPath.class).value() + "\"\n"
                + "method: \"" + method.getName() + "\"\n"
                + "data: {"
                + Arrays.stream(method.getParameters(), 1, method.getParameterCount())
                        .map(e -> e.getName() + ": " + e.getType().getSimpleName())
                        .collect(Collectors.joining("\n\t", "\n\t", "\n"))
                + "}";
    }
}
