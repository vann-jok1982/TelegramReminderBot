package com.example.TelegramReminderBot.databot.repository;

import com.example.TelegramReminderBot.databot.model.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {
}
