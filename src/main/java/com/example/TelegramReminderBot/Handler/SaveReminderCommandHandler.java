package com.example.TelegramReminderBot.Handler;

import com.example.TelegramReminderBot.databot.servise.ReminderServis;
import com.example.TelegramReminderBot.databot.servise.UserServis;
import com.example.TelegramReminderBot.scheduler.ReminderSchedule;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class SaveReminderCommandHandler implements CommandHandler {
    private static final Logger logger = LoggerFactory.getLogger(SaveReminderCommandHandler.class);
    private final UserServis userServis;
    private final ReminderServis reminderServis;
    private final ReminderSchedule reminderSchedule;

    @Override
    public SendMessage handle(Update update) {
        String userMessage = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();

        if (userMessage == null || userMessage.trim().isEmpty()) {
            return createSendMessage(chatId, "Неверный формат: сообщение не может быть пустым.");
        }
        userMessage = userMessage.trim();
        if (userMessage.toLowerCase().startsWith("сохранить:")) {
            if (userMessage.length() <= 10) {
                return createSendMessage(chatId, "Неверный формат: После 'сохранить:' должен идти текст напоминания");
            }
            String Message = userMessage.substring(10).trim();
            if (Message.contains("/дата/")) {
                String data = Message.split("/дата/", 2)[1].trim();
                String reminderMessage = Message.split("/дата/", 2)[0].trim();

                String[] timeParts = data.split("\\.", 2);
                if (timeParts.length < 2) {
                    return createSendMessage(chatId, "Неверный формат даты: должен быть формат 'час.минуты' ");
                }

                int hour;
                int minute;

                try {
                    hour = Integer.parseInt(timeParts[0]);
                    minute = Integer.parseInt(timeParts[1]);

                    if(hour < 0 || hour >23 || minute <0 || minute > 59)
                    {
                        return createSendMessage(chatId, "Неверный формат даты: час должен быть от 0 до 23, минуты от 0 до 59 ");
                    }

                } catch (NumberFormatException e) {
                    logger.error("Error parsing time: ", e);
                    return createSendMessage(chatId, "Неверный формат даты: час и минута должны быть целыми числами");
                }

                try {
                    reminderSchedule.scheduleNotification(chatId,data);

                    if(userServis.FindBiId(chatId) != null) {
                        reminderServis.SaveReminder(reminderMessage, "не выполнено", data, userServis.FindBiId(chatId));
                    } else {
                        return createSendMessage(chatId,"Пользователь не найден.");
                    }
                } catch (Exception e) {
                    logger.error("Error saving reminder: ",e);
                    return createSendMessage(chatId, "Ошибка при сохранении напоминания : "+ e.getMessage());
                }

                return createSendMessage(chatId, "Напоминание сохранено");
            } else {
                return createSendMessage(chatId, "Неверный формат,нет /дата/");
            }
        }
        return createSendMessage(chatId, "Неизвестная команда."); // Возвращаем сообщение об ошибке.
    }
    private SendMessage createSendMessage(Long chatId, String text) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
    }
}