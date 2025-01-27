package com.example.TelegramReminderBot.Handler;

import com.example.TelegramReminderBot.databot.servise.ReminderServis;
import com.example.TelegramReminderBot.databot.servise.UserServis;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class SaveReminderCommandHandler implements CommandHandler {

    private final UserServis userServis;
    private final ReminderServis reminderServis;

    @Override
    public SendMessage handle(Update update) {
        String userMessage = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();

        if (userMessage.toLowerCase().startsWith("сохранить:")) {
            String reminderMessage = userMessage.substring(10);
            if (reminderMessage.contains("<дата>")) {
                String data = reminderMessage.split("<дата>", 2)[1];
                reminderMessage = reminderMessage.split("<дата>", 2)[0];
                reminderServis.SaveReminder(reminderMessage, "не выполнено", data, userServis.FindBiId(chatId));
                return createSendMessage(chatId, "Напоминание сохранено");
            } else {
                return createSendMessage(chatId, "Неверный формат,нет <дата>");
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