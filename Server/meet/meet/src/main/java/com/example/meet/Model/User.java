package com.example.meet.Model;

import com.example.meet.Model.TimestampDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.cloud.Timestamp;
import lombok.*;

import java.io.IOException;
import java.util.List;

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
    private List<String> friendRequests;
    private List<String> sentFriendRequests;
    private List<String> friendList;
    @JsonDeserialize(using = TimestampDeserializer.class)
    private Timestamp createdTimestamp;
    private String userId;

}
