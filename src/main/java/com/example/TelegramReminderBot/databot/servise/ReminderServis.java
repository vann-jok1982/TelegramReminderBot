package com.example.TelegramReminderBot.databot.servise;

import com.example.TelegramReminderBot.databot.model.Reminder;
import com.example.TelegramReminderBot.databot.model.User;
import com.example.TelegramReminderBot.databot.repository.ReminderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ReminderServis {
    private final ReminderRepository reminderRepository;
    @Autowired
    public ReminderServis(ReminderRepository reminderRepository) {
        this.reminderRepository = reminderRepository;
    }

    @Transactional
    public void SaveReminder(String reminderMessage,String status,String data, User user){
        Reminder reminder = new Reminder();
        reminder.setValue(reminderMessage);
        reminder.setStatus(status);
        reminder.setData(data);
        reminder.setUser(user);
        reminderRepository.save(reminder);
    }
    @Transactional
    public void DeleteWordAll(){
        reminderRepository.deleteAll();
    }
}
