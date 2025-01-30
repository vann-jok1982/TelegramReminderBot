package com.example.TelegramReminderBot.databot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Table(name = "user", schema = "public")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @Column(name = "id_user", nullable = false)
    private Long id;

    @Column(name = "user_name")
    private String name;

    @Column(name = "user_status")
    private String status;

    @OneToMany(mappedBy = "user",fetch = FetchType.EAGER)
    private List<Reminder> reminders;
}
