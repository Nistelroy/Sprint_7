package ru.yandex.practicum.models;

public class Courier {
    private String login;
    private String password;
    private String name;

    public Courier withLogin(String login) {
        this.login = login;
        return this;
    }

    public Courier withPassword(String password) {
        this.password = password;
        return this;
    }

    public Courier withName(String name) {
        this.name = name;
        return this;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
