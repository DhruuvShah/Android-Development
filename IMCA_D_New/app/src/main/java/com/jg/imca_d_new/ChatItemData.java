package com.jg.imca_d_new;

public class ChatItemData {

    int img;
    String name;
    String time;
    String msg;

    int status;

    public ChatItemData(int status,int profile,String name,String time,String msg){
        this.status = status;
        this.img = profile;
        this.name = name;
        this.time = time;
        this.msg = msg;

    }

}
