package com.locaces.locaces;

import android.location.Location;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.sql.Date;

public class CheckInModel {
    private int id, userID, placeID;
    Date time;
    String status;

    ArrayList<Integer> likers;
    ArrayList<String> comments;
    ArrayList<Integer> commenters;

    public CheckInModel(int userID, int placeID, Date time, String status) {
        this.userID = userID;
        this.placeID = placeID;
        this.time = time;
        this.status = status;
    }

    public CheckInModel() {
    }

    public void saveCheckIn() throws ClassNotFoundException, SQLException, ParseException {
        DataBaseManager.update("INSERT INTO CheckIn (UserID, PlaceID, Time, Status) VALUES ( " + userID + " , "
                + placeID + " , '" + time.toString() + "' , '" + status + "');");
    }

    public void getCheckIn(int _id) throws ClassNotFoundException, SQLException {
        ResultSet result = DataBaseManager.query("SELECT * FROM CheckIn WHERE ID = " + _id);
        id = _id;
        userID = result.getInt("UserID");
        placeID = result.getInt("PlaceID");
        time = result.getDate("Time");
        status = result.getString("Status");

        result = DataBaseManager.query("SELECT UserID FROM Like WHERE ID = " + id);
        likers = (ArrayList<Integer>) result.getArray("UserID");

        result = DataBaseManager.query("SELECT UserID FROM Comment WHERE ID = " + id);
        commenters = (ArrayList<Integer>) result.getArray("UserID");

        result = DataBaseManager.query("SELECT Text FROM Comment WHERE ID = " + id);
        comments = (ArrayList<String>) result.getArray("Text");
    }

    public void update() throws ClassNotFoundException, SQLException {
        DataBaseManager.update("DELETE FROM Like WHERE ID = " + id);
        DataBaseManager.update("DELETE FROM Comment WHERE ID = " + id);

        for (Integer userID : likers)
            DataBaseManager.update("INSERT INTO Like VALUES ( " + id + " , " + userID + " ) ");

        for (int i = 0; i < comments.size(); ++i)
            DataBaseManager.update("INSERT INTO Comment VALUES ( " + id + " , " + commenters.get(i) + " , '"
                    + comments.get(i) + "')");

    }

    public static ArrayList<Post> getAllCheckInPosts() throws SQLException, ClassNotFoundException {
        ResultSet result = DataBaseManager.query("SELECT * FROM CheckIn");
        ArrayList<Post> posts = new ArrayList<>();
        while (result.next()) {
            int userID = result.getInt("UserID");
            int placeID = result.getInt("PlaceID");
            String status = result.getString("Status");
            String time = result.getString("Time");

            ResultSet userData = DataBaseManager.query("SELECT * FROM User WHERE ID = " + userID + ";");
            ResultSet placeData = DataBaseManager.query("SELECT * FROM Place WHERE ID = " + placeID + ";");

            String user = new String();
            if (userData.next())
                user = userData.getString("Name");

            String place = new String();
            if (placeData.next())
                place = placeData.getString("Name");

            posts.add(new Post(status, place, user, time));
        }
        return posts;
    }

    public void addLike(int userID) throws ClassNotFoundException, SQLException {
        likers.add(userID);
        update();

    }

    public void addComment(int userID, String comment) throws ClassNotFoundException, SQLException {
        commenters.add(userID);
        comments.add(comment);
        update();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getPlaceID() {
        return placeID;
    }

    public void setPlaceID(int placeID) {
        this.placeID = placeID;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<Integer> getLikers() {
        return likers;
    }

    public void setLikers(ArrayList<Integer> likers) {
        this.likers = likers;
    }

    public ArrayList<String> getComments() {
        return comments;
    }

    public void setComments(ArrayList<String> comments) {
        this.comments = comments;
    }

}