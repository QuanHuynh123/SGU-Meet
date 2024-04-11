package com.example.meet.Model;

import com.google.cloud.Timestamp;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {
    private String email;
    private String gender;
    private int age;
    private String name;
    private Timestamp createdTimestamp;
    private String userId;
}
