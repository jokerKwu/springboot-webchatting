package com.joker.webchatting.springboot.domain.chat;

import com.joker.webchatting.springboot.service.chat.ChatService;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class ChatRoom {
   private String roomId;
   private String name;

   public static ChatRoom create(String name){
       ChatRoom chatRoom = new ChatRoom();
       chatRoom.roomId= UUID.randomUUID().toString();
       chatRoom.name = name;
       return chatRoom;
   }
}
