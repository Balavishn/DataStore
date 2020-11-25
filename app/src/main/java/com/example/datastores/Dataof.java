package com.example.datastores;

class Dataof {
    String title,data;
    public Dataof(String t, String d) {
        this.title=t;
        this.data=d;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
