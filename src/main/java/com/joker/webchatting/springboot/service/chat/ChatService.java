package com.joker.webchatting.springboot.service.chat;

import com.joker.webchatting.springboot.domain.chat.ChatMessage;
import com.joker.webchatting.springboot.domain.chat.ChatRequest;
import com.joker.webchatting.springboot.domain.chat.ChatResponse;
import com.joker.webchatting.springboot.domain.chat.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.PostConstruct;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
public class ChatService {

    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);
    private Map<ChatRequest, DeferredResult<ChatResponse>> waitingUsers;
    // {key : websocket session id, value : chat room id}
    private Map<String, String> connectedUsers;
    private ReentrantReadWriteLock lock;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PostConstruct
    private void setUp() {
        this.waitingUsers = new LinkedHashMap<>();
        this.lock = new ReentrantReadWriteLock();
        this.connectedUsers = new ConcurrentHashMap<>();
    }

    @Async("asyncThreadPool")
    public void joinChatRoom(ChatRequest request, DeferredResult<ChatResponse> deferredResult) {
        logger.info("## Join chat room request. {}[{}]", Thread.currentThread().getName(), Thread.currentThread().getId());
        if (request == null || deferredResult == null) {
            return;
        }

        try {
            lock.writeLock().lock();
            waitingUsers.put(request, deferredResult);
        } finally {
            lock.writeLock().unlock();
            establishChatRoom();
        }
    }

    public void cancelChatRoom(ChatRequest chatRequest) {
        try {
            lock.writeLock().lock();
            setJoinResult(waitingUsers.remove(chatRequest), new ChatResponse(ChatResponse.ResponseResult.CANCEL, null, chatRequest.getSessionId()));
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void timeout(ChatRequest chatRequest) {
        try {
            lock.writeLock().lock();
            setJoinResult(waitingUsers.remove(chatRequest), new ChatResponse(ChatResponse.ResponseResult.TIMEOUT, null, chatRequest.getSessionId()));
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void establishChatRoom() {
        try {
            logger.debug("Current waiting users : " + waitingUsers.size());
            lock.readLock().lock();
            if (waitingUsers.size() < 2) {
                return;
            }

            Iterator<ChatRequest> itr = waitingUsers.keySet().iterator();
            ChatRequest user1 = itr.next();
            ChatRequest user2 = itr.next();

            String uuid = UUID.randomUUID().toString();

            DeferredResult<ChatResponse> user1Result = waitingUsers.remove(user1);
            DeferredResult<ChatResponse> user2Result = waitingUsers.remove(user2);

            user1Result.setResult(new ChatResponse(ChatResponse.ResponseResult.SUCCESS, uuid, user1.getSessionId()));
            user2Result.setResult(new ChatResponse(ChatResponse.ResponseResult.SUCCESS, uuid, user2.getSessionId()));
        } catch (Exception e) {
            logger.warn("Exception occur while checking waiting users", e);
        } finally {
            lock.readLock().unlock();
        }
    }

    public void sendMessage(String chatRoomId, ChatMessage chatMessage) {
        String destination = getDestination(chatRoomId);
        messagingTemplate.convertAndSend(destination, chatMessage);
    }

    public void connectUser(String chatRoomId, String websocketSessionId) {
        connectedUsers.put(websocketSessionId, chatRoomId);
    }

    public void disconnectUser(String websocketSessionId) {
        String chatRoomId = connectedUsers.get(websocketSessionId);
        ChatMessage chatMessage = new ChatMessage();

        chatMessage.setMessageType(MessageType.DISCONNECTED);
        sendMessage(chatRoomId, chatMessage);
    }

    private String getDestination(String chatRoomId) {
        return "/topic/chat/" + chatRoomId;
    }

    private void setJoinResult(DeferredResult<ChatResponse> result, ChatResponse response) {
        if (result != null) {
            result.setResult(response);
        }
    }
}