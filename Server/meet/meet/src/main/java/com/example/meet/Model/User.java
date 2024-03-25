package com.example.meet.Model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {
    private String documentId;
    private int age;
    private String email;
    private String gender;
    private String name;
}
