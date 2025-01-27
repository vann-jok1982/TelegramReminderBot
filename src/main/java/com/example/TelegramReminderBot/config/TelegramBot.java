package com.example.TelegramReminderBot.config;

import com.example.TelegramReminderBot.botfasad.BotFasade;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {

    private final BotFasade botFasade;
    private static final Logger logger = LoggerFactory.getLogger(TelegramBot.class);

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
                SendMessage sendMessage = botFasade.obrabotkaHandleUpdate(update);
                if (sendMessage != null) {
                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        logger.error("Error processing update: {}", e.getMessage(), e);
                        throw new RuntimeException(e);
                    }
                }
            }
    }

    @Override
    public String getBotUsername() {
        return "/@VancheloBot";
    }

    @Override
    public String getBotToken() {
        return "6978858559:AAHQyJvLkC8myIpxrpoS67jWsHn_aFmY0tM";
    }
}