package com.park.listener;

import com.park.service.NotificacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class ExpirationMessageListener implements org.springframework.data.redis.connection.MessageListener {
    private final NotificacaoService notificacaoService;

    private Logger logger = Logger.getLogger(ExpirationMessageListener.class.getName());

    @Autowired
    public ExpirationMessageListener(
            NotificacaoService notificacaoService
    ) {
        this.notificacaoService = notificacaoService;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        byte[] body = message.getBody();
        String expiredKey = new String(body);
        try {
            logger.info("Recebido evento de expiração para a chave: " + expiredKey);
            notificacaoService.notificar(expiredKey);
        } catch (ChangeSetPersister.NotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
