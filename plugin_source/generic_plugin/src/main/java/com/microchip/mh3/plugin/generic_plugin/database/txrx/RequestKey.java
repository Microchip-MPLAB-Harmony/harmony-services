package com.microchip.mh3.plugin.generic_plugin.database.txrx;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class RequestKey {

    private final String path;
    private final String method;

    public RequestKey(Request request) {
        this.path = request.getPath();
        this.method = request.getMethod();
    }

}
