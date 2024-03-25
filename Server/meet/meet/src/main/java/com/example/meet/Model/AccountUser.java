package com.example.meet.Model;

import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AccountUser {
    private String email;
    private String password;
    private boolean emailVerified;
    private String name;
    private boolean disabled;
}
