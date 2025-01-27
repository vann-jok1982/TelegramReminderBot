package com.example.TelegramReminderBot.botfasad;


import com.example.TelegramReminderBot.Handler.CommandHandler;
import com.example.TelegramReminderBot.Handler.DefaultCommandHandler;
import com.example.TelegramReminderBot.Handler.GetAllRemindersCommandHandler;
import com.example.TelegramReminderBot.Handler.SaveReminderCommandHandler;
import com.example.TelegramReminderBot.databot.servise.UserServis;
import com.example.TelegramReminderBot.scheduler.ReminderSchedule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class BotFasade {
    private final UserServis userServis;
    private final ReminderSchedule reminderSchedule;
    private final DefaultCommandHandler defaultCommandHandler;
    private final Map<String, CommandHandler> commandHandlers = new HashMap<>();

    public BotFasade(UserServis userServis, List<CommandHandler> commandHandlers, ReminderSchedule reminderSchedule,DefaultCommandHandler defaultCommandHandler) {
        this.userServis = userServis;
        this.reminderSchedule = reminderSchedule;
        this.defaultCommandHandler = defaultCommandHandler;
        for (CommandHandler handler : commandHandlers) {
            if (handler instanceof SaveReminderCommandHandler) {
                this.commandHandlers.put("сохранить:", handler);
            } else if (handler instanceof GetAllRemindersCommandHandler) {
                this.commandHandlers.put("все напоминания", handler);
            }

        }
    }

    private SendMessage createSendMessage(Long chatId, String text) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
    }


    public SendMessage obrabotkaHandleUpdate(Update update) {
        String userMessage = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();
        String username = update.getMessage().getFrom().getUserName();
        if (userServis.FindBiId(chatId) == null) userServis.SaveUser(chatId, username);
        SendMessage sendMessage=null;
        for (Map.Entry<String, CommandHandler> entry : commandHandlers.entrySet()) {
            if(userMessage.toLowerCase().startsWith(entry.getKey())){
                sendMessage = entry.getValue().handle(update);
                break;
            }
        }
        if(sendMessage == null)
            sendMessage =  defaultCommandHandler.handle(update);
        reminderSchedule.scheduleNotification(chatId);
        return sendMessage;
    }
}
