package com.example.TelegramReminderBot.databot.servise;

import com.example.TelegramReminderBot.databot.model.Reminder;
import com.example.TelegramReminderBot.databot.model.User;
import com.example.TelegramReminderBot.databot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UserServis {
    private final UserRepository userRepository;
    @Autowired
    public UserServis(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public String UserFindBiIdReminder(Long chatId){ //находим user из бд и из табл reminder находим его записи
        String textOtoslal;
        User user = userRepository.findById(chatId).orElse(null);
        if (user==null){
            textOtoslal="у тебя нет напоминаний";
        }
        else {
            textOtoslal = user.getReminders().stream()
                    .map(Reminder::getValue)
                    .collect(Collectors.joining(", "));
        }
        return textOtoslal;
    }
    public List<User> FindAll(){
        return userRepository.findAll();
    }

    @Transactional
    public void SaveUser(Long chatId,String UserName) {
        User user = userRepository.findById(chatId).orElse(null);
        if (user == null) {
            user = new User();
            user.setId(chatId);
            user.setName(UserName);// в скобках имя юзера в чате(телеге)
            userRepository.save(user);
        }
    }
    @Transactional
    public void UpdateUserStatus(Long chatId,String status) {
        User user = userRepository.findById(chatId).orElse(null);
        if (user != null) {
            user.setStatus(status);
            userRepository.save(user);
        }
    }


    public User FindBiId(Long chatId){
        return userRepository.findById(chatId).orElse(null);

    }
    @Transactional
    public void DeleteUserAll(){
        userRepository.deleteAll();
    }
}

