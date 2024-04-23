package com.microchip.mh3.plugin.generic_plugin.database.txrx;

import com.google.gson.JsonElement;
import lombok.Data;

@Data
public class Request {

    private String uuid;
    private long sequenceNumber;
    private String path;
    private String method;
    private JsonElement data;

}
