package com.example.demo.model;

import lombok.Data;

@Data
public class Message {
    //資訊標題
    private String title;
    //資訊主體
    private String content;
    //額外資訊
    private String etraInfo;

    public Message(String title, String content, String etraInfo) {
        super();
        this.title = title;
        this.content = content;
        this.etraInfo = etraInfo;
    }
}
