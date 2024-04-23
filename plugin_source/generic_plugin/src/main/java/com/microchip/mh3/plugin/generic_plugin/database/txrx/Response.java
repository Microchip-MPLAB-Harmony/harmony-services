package com.microchip.mh3.plugin.generic_plugin.database.txrx;

import com.microchip.utils.event.Event;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;

//interface Response {
//  status: string; // success or error or event
//  uuid: string; // uuid
//  sequenceNumber: number
//  event: string;
//  data: any;
//}
@Data
public class Response {

    private String status;
    private String uuid;
    private long sequenceNumber;
    private String eventName;
    private String eventFilterCacheId;
    private Object data;

    public static Response event(Event event) {
        Response response = new Response();
        response.status = "event";
        response.eventName = event.getClass().getSimpleName();
        return response;
    }
    
    public static Response success(Map<String, Object> attributes) {
        Response response = Response.success();
        response.data = attributes;
        return response;
    }
    
     public static Response success(Object data) {
        Response response = Response.success();
        response.data = data;
        return response;
    }
    
    public static Response success() {
        Response response = new Response();
        response.status = "success";
        return response;
    }
    
    public static Response error(String message, Request request) {
        Response response = new Response();
        response.status = "error";
        
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("message", message);
        attributes.put("request", request);
        response.data = attributes;
        return response;
    }
    
    public static Response error(String message, String requestBody) {
        Response response = new Response();
        response.status = "error";
        
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("message", message);
        attributes.put("request", requestBody);
        response.data = attributes;
        return response;
    }
}
