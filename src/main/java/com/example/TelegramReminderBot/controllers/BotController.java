package com.example.TelegramReminderBot.controllers;

import com.example.TelegramReminderBot.config.ReminderConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
@RequiredArgsConstructor // lombok
public class BotController {
    private final ReminderConfig reminderConfig;

    @PostMapping("/")
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        return reminderConfig.onWebhookUpdateReceived(update);
    }

}
