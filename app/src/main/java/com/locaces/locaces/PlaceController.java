package com.locaces.locaces;

import android.location.Location;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.sql.Date;

public class PlaceController {
	private static Location location;
	private static PlaceModel place;

	public static PlaceModel getNearestPlace(Location _location) throws ClassNotFoundException, SQLException {
		location = _location;
		ArrayList<PlaceModel> places = PlaceModel.getAllPlaces();
		PlaceModel nearest = places.get(0);
		for (PlaceModel p : places) {
			if (location.distanceTo(p.getLocation()) < location.distanceTo(nearest.getLocation()))
				nearest = p;
		}
		place = nearest;
		return place;
	}

	public static void ratePlace(double rate) throws ClassNotFoundException, SQLException {
		place.setRate(rate);
		place.updatePlace();
	}

	public static void addNewPlace(int id, int numberOfCheckIns, String name, String description, double rate,
								   Location location) throws ClassNotFoundException, SQLException {
		new PlaceModel(id, numberOfCheckIns, name, description, rate, location).savePlace();
	}

	public static void saveCheckIn(String status, Date time) throws ClassNotFoundException, SQLException, ParseException {
		place.setNumberOfCheckIns(place.getNumberOfCheckIns() + 1);
		CheckInController.doCheckIn(status, place, time);
	}

}