//package com.microchip.mh3.plugin.generic_plugin.database.event;
//
//import static com.microchip.h3.database.DatabaseEvents.*;
//import static com.microchip.h3.database.DatabaseEvents.SymbolValueChangedEvent;
//import java.util.function.Consumer;
//
//public class EventAgent {
//
//    private final Consumer<String> transceiverSender;
//    private final EventJet eventJet;
//
//    public EventAgent(Consumer<String> transceiverSender) {
//        this.transceiverSender = transceiverSender;
//        this.eventJet = new EventJet();
//        EventDtoFactory eventDtoFactory = new EventDtoFactory(transceiverSender);
//        
////        this.eventJet.addEventListener(SymbolValueChangedEvent.class, this::onSymbolValueChanged); // 1st way of creating consumers
//        this.eventJet.addEventListener(SymbolValueChangedEvent.class, eventDtoFactory::create); // 2nd and better way of creating consumers
//        this.eventJet.addEventListener(SymbolStateChangedEvent.class, eventDtoFactory::create);    
//        this.eventJet.addEventListener(SymbolVisualChangedEvent.class, eventDtoFactory::create);
//        
//        this.eventJet.addEventListener(ComponentActivatedEvent.class, eventDtoFactory::create);
//        this.eventJet.addEventListener(ComponentDeactivatedEvent.class, eventDtoFactory::create);
//    }
//    
//    public void destroy() {
//        this.eventJet.destroy();
//    }
//
////    public void onSymbolValueChanged(SymbolValueChangedEvent event) {
////        SymbolValueChangedEventDto symbolValueChangedEventDto = new SymbolValueChangedEventDto(event);
////        eventConsumer.accept(symbolValueChangedEventDto.toJson());
////    }
//}
