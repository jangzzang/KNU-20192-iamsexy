package com.example.continuousliving.MainGate;

public class MainGate_Content {
    String mgc_title;
    String mgc_id;
    String mgc_password;
    String mgc_username;
    String mgc_date;
    String mgc_content;
    int mgc_num;

    public MainGate_Content(String mgc_title, String mgc_id, String mgc_password, String mgc_username, String mgc_date, String mgc_content, int mgc_num) {
        this.mgc_title = mgc_title;
        this.mgc_id = mgc_id;
        this.mgc_password = mgc_password;
        this.mgc_username = mgc_username;
        this.mgc_date = mgc_date;
        this.mgc_content = mgc_content;
        this.mgc_num = mgc_num;
    }

    public String getMgc_title() {
        return mgc_title;
    }

    public void setMgc_title(String mgc_title) {
        this.mgc_title = mgc_title;
    }

    public String getMgc_id() {
        return mgc_id;
    }

    public void setMgc_id(String mgc_id) {
        this.mgc_id = mgc_id;
    }

    public String getMgc_password() {
        return mgc_password;
    }

    public void setMgc_password(String mgc_password) {
        this.mgc_password = mgc_password;
    }

    public String getMgc_username() {
        return mgc_username;
    }

    public void setMgc_username(String mgc_username) {
        this.mgc_username = mgc_username;
    }

    public String getMgc_date() {
        return mgc_date;
    }

    public void setMgc_date(String mgc_date) {
        this.mgc_date = mgc_date;
    }

    public String getMgc_content() {
        return mgc_content;
    }

    public void setMgc_content(String mgc_content) {
        this.mgc_content = mgc_content;
    }

    public int getMgc_num() {
        return mgc_num;
    }

    public void setMgc_num(int mgc_num) {
        this.mgc_num = mgc_num;
    }
}
