package com.kokutouda.dnote.dnote.data;

public class MainNavData {
    private int res;
    private String text;

    public MainNavData(int res, String text) {
        this.res = res;
        this.text = text;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public int getRes() {
        return res;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
