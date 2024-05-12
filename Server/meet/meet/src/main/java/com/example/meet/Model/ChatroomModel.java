package com.example.meet.Model;

import com.google.cloud.Timestamp;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ChatroomModel {
    String chatroomId;
    List<String> userIds;
    Timestamp lastMessageTimestamp;
    String  lastMessageSenderId;
    String lastMessage;
}
