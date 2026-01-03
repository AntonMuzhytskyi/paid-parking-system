package com.parking.samurai.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
* Service responsible for sending real-time updates to clients via WebSocket.
* Encapsulates messaging logic, allowing controllers or schedulers to notify
* clients when parking spots are created, updated, or freed.
* Uses Spring's SimpMessagingTemplate for broadcasting messages to subscribed clients.
*/

@Service
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void notifyParkingSpotsChanged() {
        messagingTemplate.convertAndSend("/topic/parking-spots", "updated");
    }
}