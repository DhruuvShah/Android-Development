package com.example.recycler_view;

public class ChatItemData {
    private final int img;
    private final String name;
    private final String time;
    private final String msg;
    private final int status;

    public ChatItemData(int status, int profile, String name, String time, String msg) {
        this.status = status;
        this.img = profile;
        this.name = name;
        this.time = time;
        this.msg = msg;
    }

    public int getImg() {
        return img;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public String getMsg() {
        return msg;
    }

    public int getStatus() {
        return status;
    }
}
