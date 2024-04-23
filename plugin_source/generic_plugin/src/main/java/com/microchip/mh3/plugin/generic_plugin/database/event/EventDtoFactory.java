package com.microchip.mh3.plugin.generic_plugin.database.event;

import com.microchip.h3.database.DatabaseEvents.ComponentActivatedEvent;
import com.microchip.h3.database.DatabaseEvents.ComponentDeactivatedEvent;
import com.microchip.h3.database.DatabaseEvents.SymbolStateChangedEvent;
import com.microchip.h3.database.DatabaseEvents.SymbolValueChangedEvent;
import com.microchip.h3.database.DatabaseEvents.SymbolVisualChangedEvent;
import com.microchip.mh3.log.Log;
import com.microchip.mh3.plugin.generic_plugin.database.symbol.dto.SymbolDtoFactory;
import com.microchip.mh3.plugin.generic_plugin.database.txrx.Response;
import java.util.function.Consumer;

public class EventDtoFactory {

    private final SymbolDtoFactory symbolDtoFactory = new SymbolDtoFactory();
    private final Consumer<Response> transmitter;

    public EventDtoFactory(Consumer<Response> transmitter) {
        this.transmitter = transmitter;
    }

    public void handle(SymbolValueChangedEvent event) {
        Response response = Response.event(event);

        EventDto eventDto = EventDto.builder()
                .eventName(event.getClass().getSimpleName())
                .symbol(symbolDtoFactory.getSymbol(event.sym))
                .build();

        response.setData(eventDto);
        transmitter.accept(response);
    }

    public void handle(SymbolStateChangedEvent event) {
        Response response = Response.event(event);

        EventDto eventDto = EventDto.builder()
                .eventName(event.getClass().getSimpleName())
                .symbol(symbolDtoFactory.getSymbol(event.sym))
                .build();

        response.setData(eventDto);
        transmitter.accept(response);
    }

    public void handle(SymbolVisualChangedEvent event) {
        Response response = Response.event(event);

        EventDto eventDto = EventDto.builder()
                .eventName(event.getClass().getSimpleName())
                .symbol(symbolDtoFactory.getSymbol(event.sym))
                .build();

        response.setData(eventDto);
        transmitter.accept(response);
    }

    public void handle(ComponentActivatedEvent event) {
        Response response = Response.event(event);

        EventDto eventDto = EventDto.builder()
                .eventName(event.getClass().getSimpleName())
                .component(new ComponentDto(event.component))
                .build();

        response.setData(eventDto);
        transmitter.accept(response);
    }

    public void handle(ComponentDeactivatedEvent event) {
        Response response = Response.event(event);

        EventDto eventDto = EventDto.builder()
                .eventName(event.getClass().getSimpleName())
                .component(new ComponentDto(event.component))
                .build();

        response.setData(eventDto);
        transmitter.accept(response);
    }

}
