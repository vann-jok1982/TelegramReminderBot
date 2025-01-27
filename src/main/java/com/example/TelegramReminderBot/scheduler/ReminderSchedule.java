package com.example.TelegramReminderBot.scheduler;

import com.example.TelegramReminderBot.config.TelegramBot;
import com.example.TelegramReminderBot.databot.model.Reminder;
import com.example.TelegramReminderBot.databot.servise.UserServis;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class ReminderSchedule {
    private final TelegramBot bot;
    private final UserServis userServis;

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(); // Планировщик задач

    public ReminderSchedule(@Lazy TelegramBot bot, UserServis userServis) {
        this.bot = bot;
        this.userServis = userServis;
    }

    // Метод для формирования SendMessage
    private SendMessage createSendMessage(Long chatId, String text) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
    }

    // Метод планирования выполнения задачи в заданное время
    public void scheduleNotification(Long chatId) {
        Runnable task = () -> {
            SendMessage sendMessage=createSendMessage(chatId,"user: "+userServis.FindBiId(chatId).getName() );
            List<Reminder> reminders = userServis.FindBiId(chatId).getReminders();
            if (reminders != null && !reminders.isEmpty()) {
                StringBuilder remindersString = new StringBuilder();
                for (Reminder reminder : reminders) {
                    remindersString.append(" * ").append(reminder.getValue());
                }
                 sendMessage=createSendMessage(chatId, remindersString.toString());
            }
            else{
                System.out.println("No Reminders for "+ chatId);
            }
            try {
                bot.execute(sendMessage);
            } catch (TelegramApiException e) {
                System.out.println("Error sending message: " + e.getMessage());
            }
        };

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime targetTime = now.withHour(0).withMinute(21).withSecond(0).withNano(0); // Устанавливаем время отправки напоминаний 10:00
        if(now.isAfter(targetTime))
        {
            targetTime = targetTime.plusDays(1);
        }
        ZonedDateTime zonedTargetTime = targetTime.atZone(ZoneId.systemDefault());
        long initialDelay =  ZonedDateTime.now().until(zonedTargetTime, java.time.temporal.ChronoUnit.MILLIS);


        scheduler.scheduleAtFixedRate(task, initialDelay, 24 * 60 * 60 * 1000, TimeUnit.MILLISECONDS); // Выполнять каждый день в 10:00
    }
}
