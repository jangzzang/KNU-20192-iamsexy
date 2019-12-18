package com.example.continuousliving.NorthGate;

public class NorthGate_Content {
    String ngc_title;
    String ngc_id;
    String ngc_password;
    String ngc_username;
    String ngc_date;
    String ngc_content;
    int ngc_num;

    public NorthGate_Content(String ngc_title, String ngc_id, String ngc_password, String ngc_username, String ngc_date, String ngc_content, int ngc_num) {
        this.ngc_title = ngc_title;
        this.ngc_id = ngc_id;
        this.ngc_password = ngc_password;
        this.ngc_username = ngc_username;
        this.ngc_date = ngc_date;
        this.ngc_content = ngc_content;
        this.ngc_num = ngc_num;
    }

    public String getNgc_title() {
        return ngc_title;
    }

    public void setNgc_title(String ngc_title) {
        this.ngc_title = ngc_title;
    }

    public String getNgc_id() {
        return ngc_id;
    }

    public void setNgc_id(String ngc_id) {
        this.ngc_id = ngc_id;
    }

    public String getNgc_password() {
        return ngc_password;
    }

    public void setNgc_password(String ngc_password) {
        this.ngc_password = ngc_password;
    }

    public String getNgc_username() {
        return ngc_username;
    }

    public void setNgc_username(String ngc_username) {
        this.ngc_username = ngc_username;
    }

    public String getNgc_date() {
        return ngc_date;
    }

    public void setNgc_date(String ngc_date) {
        this.ngc_date = ngc_date;
    }

    public String getNgc_content() {
        return ngc_content;
    }

    public void setNgc_content(String ngc_content) {
        this.ngc_content = ngc_content;
    }

    public int getNgc_num() {
        return ngc_num;
    }

    public void setNgc_num(int ngc_num) {
        this.ngc_num = ngc_num;
    }
}
