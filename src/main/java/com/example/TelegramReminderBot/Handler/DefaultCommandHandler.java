package com.example.TelegramReminderBot.Handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class DefaultCommandHandler implements CommandHandler {

    @Override
    public SendMessage handle(Update update) {
        Long chatId = update.getMessage().getChatId();
        return createSendMessage(chatId, "Привет "+update.getMessage().getChat().getFirstName()+" я бот для сохранения и отправки напоминаний по дате и времени" +
                " что-бы сохранить напоминание напишите <сохранить:текст напоминания/дата/час.минута> (пример  сохранить:Позвонить маме/дата/17.30" +
                " что-бы просмотреть все напоминания напишите все напоминания");
    }

    private SendMessage createSendMessage(Long chatId, String text) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
    }
}