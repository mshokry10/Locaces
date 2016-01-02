package com.locaces.locaces;

import java.sql.SQLException;
import java.sql.Date;
import java.text.ParseException;

public class CheckInController {

    private static CheckInModel checkIn = new CheckInModel();
    private static int userID;
    private static boolean initialized = false;

    public static void initialize() {
        userID = UserController.getUser().getId();
    }

    public static void like() throws ClassNotFoundException, SQLException {
        if (!initialized)
            initialize();
        checkIn.addLike(userID);
    }

    public static void comment(String comment) throws ClassNotFoundException, SQLException {
        if (!initialized)
            initialize();
        checkIn.addComment(userID, comment);
    }

    public static void doCheckIn(String status, PlaceModel place, Date time) throws ClassNotFoundException, SQLException, ParseException {
        if (!initialized)
            initialize();
        checkIn = new CheckInModel(userID, place.getId(), time, status);
        checkIn.saveCheckIn();
    }

    public static void getCheckIn(int id) throws ClassNotFoundException, SQLException {
        if (!initialized)
            initialize();
        checkIn.getCheckIn(id);
    }

}