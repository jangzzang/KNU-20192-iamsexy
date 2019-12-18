package com.example.continuousliving.SideGate;

public class SideGate_Content {
    String sgc_title;
    String sgc_id;
    String sgc_password;
    String sgc_username;
    String sgc_date;
    String sgc_content;
    int sgc_num;

    public SideGate_Content(String sgc_title, String sgc_id, String sgc_password, String sgc_username, String sgc_date, String sgc_content, int sgc_num) {
        this.sgc_title = sgc_title;
        this.sgc_id = sgc_id;
        this.sgc_password = sgc_password;
        this.sgc_username = sgc_username;
        this.sgc_date = sgc_date;
        this.sgc_content = sgc_content;
        this.sgc_num = sgc_num;
    }

    public String getSgc_title() {
        return sgc_title;
    }

    public void setSgc_title(String sgc_title) {
        this.sgc_title = sgc_title;
    }

    public String getSgc_id() {
        return sgc_id;
    }

    public void setSgc_id(String sgc_id) {
        this.sgc_id = sgc_id;
    }

    public String getSgc_password() {
        return sgc_password;
    }

    public void setSgc_password(String sgc_password) {
        this.sgc_password = sgc_password;
    }

    public String getSgc_username() {
        return sgc_username;
    }

    public void setSgc_username(String sgc_username) {
        this.sgc_username = sgc_username;
    }

    public String getSgc_date() {
        return sgc_date;
    }

    public void setSgc_date(String sgc_date) {
        this.sgc_date = sgc_date;
    }

    public String getSgc_content() {
        return sgc_content;
    }

    public void setSgc_content(String sgc_content) {
        this.sgc_content = sgc_content;
    }

    public int getSgc_num() {
        return sgc_num;
    }

    public void setSgc_num(int sgc_num) {
        this.sgc_num = sgc_num;
    }
}
