package com.joker.webchatting.springboot.domain.chat;

public class ChatResponse {

    private ResponseResult responseResult;
    private String chatRoomId;
    private String sessionId;

    public ChatResponse() {
    }

    public ChatResponse(ResponseResult responseResult, String chatRoomId, String sessionId) {
        this.responseResult = responseResult;
        this.chatRoomId = chatRoomId;
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public ResponseResult getResponseResult() {
        return responseResult;
    }

    public void setResponseResult(ResponseResult responseResult) {
        this.responseResult = responseResult;
    }

    @Override
    public String toString() {
        return "ChatResponse{" + "responseResult=" + responseResult + ", chatRoomId='" + chatRoomId + '\'' + ", sessionId='" + sessionId + '\'' + '}';
    }

    public enum ResponseResult {
        SUCCESS, CANCEL, TIMEOUT;
    }
}