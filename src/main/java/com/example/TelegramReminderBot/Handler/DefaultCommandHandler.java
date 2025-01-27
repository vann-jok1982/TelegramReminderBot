package com.example.TelegramReminderBot.Handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class DefaultCommandHandler implements CommandHandler {

    @Override
    public SendMessage handle(Update update) {
        Long chatId = update.getMessage().getChatId();
        return createSendMessage(chatId, "Привет");
    }

    private SendMessage createSendMessage(Long chatId, String text) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
    }
}