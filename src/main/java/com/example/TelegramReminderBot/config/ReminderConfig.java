package com.example.TelegramReminderBot.config;


import com.example.TelegramReminderBot.botfasad.BotFasade;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
@Slf4j  //логирование
@RequiredArgsConstructor
public class ReminderConfig extends TelegramWebhookBot {
    @Lazy
    private final TelegramBot telegramBot;

    private final BotFasade botFasade;
    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) { //метод для возврата сообщений
        SendMessage sendMessage = botFasade.obrabotkaHandleUpdate(update);
        return sendMessage;
    }
    public void execute(SendMessage sendMessage) throws TelegramApiException {
        log.info("Sending message to chatId: " + sendMessage.getChatId());
        telegramBot.execute(sendMessage);
    }

    @Override
    public String getBotPath() {   //просит строчку из нгрок(url)
        return "https://vanjok.pagekite.me/";
    }

    @Override
    public String getBotUsername() {  //просит имя бота
        return "/@VancheloBot";
    }

    @PostConstruct
    public void init() {  //обязательный метод
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(this, SetWebhook.builder().url(getBotPath()).build());
            setWebHook();
        } catch (Exception e) {
        }
    }

    public void setWebHook() throws IOException {
        String token = "6978858559:AAHQyJvLkC8myIpxrpoS67jWsHn_aFmY0tM";
        String url = String.format("https://api.telegram.org/bot%s/setWebhook?url=%s", token, getBotPath());
        log.info(url);
        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        connection.setRequestMethod("GET");
        log.info(connection.getResponseMessage());
    }
}