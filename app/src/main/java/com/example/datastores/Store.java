package com.example.datastores;

class Store {
    String name,nick;
    public Store(String n, String nn) {
        this.name=n;
        this.nick=nn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
}
