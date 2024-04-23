package com.microchip.mh3.plugin.generic_plugin.commons.log;

import com.microchip.mh3.log.Log;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.ControllerMethod;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.ControllerPath;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.Request;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.Response;

@ControllerPath("Logging")
public class LoggingController {

    @ControllerMethod
    public Response error(Request request, String context, String message) {
        Log.write(context, Log.Severity.Error, message);
        return Response.success();
    }

    @ControllerMethod
    public Response warn(Request request, String context, String message) {
        Log.write(context, Log.Severity.Warning, message);
        return Response.success();
    }

    @ControllerMethod
    public Response info(Request request, String context, String message) {
        Log.write(context, Log.Severity.Info, message);
        return Response.success();
    }

    @ControllerMethod
    public Response debug(Request request, String context, String message) {
        Log.write(context, Log.Severity.Info, message, Log.Level.DEBUG);
        return Response.success();
    }
}
