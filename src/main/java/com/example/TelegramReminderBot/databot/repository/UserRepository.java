package com.example.TelegramReminderBot.databot.repository;

import com.example.TelegramReminderBot.databot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.beans.JavaBean;

public interface UserRepository extends JpaRepository<User, Long> {
}
