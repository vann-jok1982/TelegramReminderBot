package com.example.TelegramReminderBot.Handler;

import com.example.TelegramReminderBot.config.TelegramBot;
import com.example.TelegramReminderBot.databot.model.Reminder;
import com.example.TelegramReminderBot.databot.model.User;
import com.example.TelegramReminderBot.databot.servise.UserServis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
//@RequiredArgsConstructor
public class GetAllRemindersCommandHandler implements CommandHandler {
    private final TelegramBot bot;
    private final UserServis userServis;
    private static final Logger logger = LoggerFactory.getLogger(GetAllRemindersCommandHandler.class);

    public GetAllRemindersCommandHandler(@Lazy TelegramBot bot, UserServis userServis) {
        this.bot = bot;
        this.userServis = userServis;
    }

    @Override
    public SendMessage handle(Update update) {
        if (update == null || update.getMessage() == null) {
            logger.error("Received null update or message.");
            return createSendMessage(null, "Неверный запрос.");
        }
        String userMessage = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();

        if (userMessage == null || !userMessage.equals("все напоминания")) {
            return createSendMessage(chatId, "Неверная команда.");
        }

        User user = null;
        try {
            user = userServis.FindBiId(chatId);
        } catch (Exception e) {
            logger.error("Error getting user: ", e);
            return createSendMessage(chatId, "Ошибка при получении пользователя.");
        }
        if (user == null) {
            return createSendMessage(chatId, "Пользователь не найден.");
        }


        List<Reminder> reminders = user.getReminders(); // Прямое обращение к reminders
        if (reminders == null || reminders.isEmpty()) {
            return createSendMessage(chatId, "Напоминаний нет.");
        }

        for (Reminder reminder : reminders) {
            if (reminder.getData() == null || reminder.getValue() == null) {
                logger.warn("Reminder with null data or value found. Skipping.");
                continue;
            }
            StringBuilder remindersText = new StringBuilder();

            remindersText.append(reminder.getData()).append(": ").append(reminder.getValue()).append("\n");

        try {
            bot.execute(createSendMessage(chatId, remindersText.toString()));
        } catch (TelegramApiException e) {
            logger.error("Error sending reminders message.", e);
            return createSendMessage(chatId, "Ошибка при отправке напоминаний.");
        }
    }
        return createSendMessage(chatId, "Напоминания отправлены.");
    }

    private SendMessage createSendMessage(Long chatId, String text) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
    }
}