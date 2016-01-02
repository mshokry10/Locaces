package com.locaces.locaces;

public class Post {
    public String status, place, user, date;

    public Post(String s, String p, String u, String d) {
        status = s;
        place = p;
        user = u;
        date = d;
    }

    @Override
    public String toString() {
        return "User: " + user + "\n" +
                "Status: " + status + "\n" +
                "Date: " + date + "\n" +
                "Place: " + place + "\n";
    }
}
