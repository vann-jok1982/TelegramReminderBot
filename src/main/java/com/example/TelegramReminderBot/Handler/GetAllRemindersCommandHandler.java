package com.example.TelegramReminderBot.Handler;

import com.example.TelegramReminderBot.databot.model.Reminder;
import com.example.TelegramReminderBot.databot.servise.UserServis;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetAllRemindersCommandHandler implements CommandHandler {

    private final UserServis userServis;

    @Override
    public SendMessage handle(Update update) {
        String userMessage = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();
        if (userMessage.equals("все напоминания")) {
            List<Reminder> reminders = userServis.FindBiId(chatId).getReminders();
            if (reminders != null && !reminders.isEmpty()) {
                StringBuilder remindersString = new StringBuilder();
                for (Reminder reminder : reminders) {
                    remindersString.append(" * ").append(reminder.getValue());
                }
                return createSendMessage(chatId, remindersString.toString());
            }
        }
        return null;
    }
    private SendMessage createSendMessage(Long chatId, String text) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
    }
}