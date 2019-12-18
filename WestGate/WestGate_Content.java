package com.example.continuousliving.WestGate;

public class WestGate_Content {
    String wgc_title;
    String wgc_id;
    String wgc_password;
    String wgc_username;
    String wgc_date;
    String wgc_content;
    int wgc_num;

    public WestGate_Content(String wgc_title, String wgc_id, String wgc_password, String wgc_username, String wgc_date, String wgc_content, int wgc_num) {
        this.wgc_title = wgc_title;
        this.wgc_id = wgc_id;
        this.wgc_password = wgc_password;
        this.wgc_username = wgc_username;
        this.wgc_date = wgc_date;
        this.wgc_content = wgc_content;
        this.wgc_num = wgc_num;
    }

    public String getWgc_title() {
        return wgc_title;
    }

    public void setWgc_title(String wgc_title) {
        this.wgc_title = wgc_title;
    }

    public String getWgc_id() {
        return wgc_id;
    }

    public void setWgc_id(String wgc_id) {
        this.wgc_id = wgc_id;
    }

    public String getWgc_password() {
        return wgc_password;
    }

    public void setWgc_password(String wgc_password) {
        this.wgc_password = wgc_password;
    }

    public String getWgc_username() {
        return wgc_username;
    }

    public void setWgc_username(String wgc_username) {
        this.wgc_username = wgc_username;
    }

    public String getWgc_date() {
        return wgc_date;
    }

    public void setWgc_date(String wgc_date) {
        this.wgc_date = wgc_date;
    }

    public String getWgc_content() {
        return wgc_content;
    }

    public void setWgc_content(String wgc_content) {
        this.wgc_content = wgc_content;
    }

    public int getWgc_num() {
        return wgc_num;
    }

    public void setWgc_num(int wgc_num) {
        this.wgc_num = wgc_num;
    }
}
