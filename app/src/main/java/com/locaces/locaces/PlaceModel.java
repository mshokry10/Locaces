package com.locaces.locaces;

import android.location.Location;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PlaceModel {
	private int id, numberOfCheckIns;
	private String name, description;
	private double rate;
	private Location location;

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	PlaceModel() {

	}

	public PlaceModel(int id, int numberOfCheckIns, String name, String description, double rate, Location location) {
		this.id = id;
		this.numberOfCheckIns = numberOfCheckIns;
		this.name = name;
		this.description = description;
		this.rate = rate;
		this.location = location;
	}

	public void savePlace() throws ClassNotFoundException, SQLException {
		DataBaseManager.update("INSERT INTO Place (NumberOfCheckIns, Name, Description, Rate, " +
				"Longitude, Latitude) VALUES (" + numberOfCheckIns + " , " + name + " , " +
				description + " , " + rate + " , " + location.getLongitude() + " , " + location.getLatitude());
	}
	
	public boolean getPlace(int _id) throws ClassNotFoundException, SQLException{
		ResultSet result = DataBaseManager.query("SELECT * FROM Place WHERE ID = " + _id) ;
		if (result.next()){
			setId(result.getInt("ID"));
			setNumberOfCheckIns(result.getInt("NumberOfCheckIns"));
			setName(result.getString("Name"));
			setDescription(result.getString("Description"));
			setRate(result.getDouble("Rate"));
			Location location = new Location("provider");
			location.setLongitude(result.getDouble("Longitude"));
			location.setLatitude(result.getDouble("Latitude"));
			setLocation(location);
			return true;
		}
		return false;
	}

	public void updatePlace() throws ClassNotFoundException, SQLException {
		DataBaseManager.update("UPDATE Place SET ID = " + id + " , NumberOfCheckIns = " + numberOfCheckIns + " , Name = "
				+ name + " , Description = " + description + " , Rate = " + rate + " , Longitude = "
				+ location.getLongitude() + " , Latitude = " + location.getLatitude());
	}

	public static ArrayList<PlaceModel> getAllPlaces() throws ClassNotFoundException, SQLException {
		ResultSet result;
		try {
			result = DataBaseManager.query("SELECT * FROM Place;");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ay 7aga");
			return new ArrayList<>();
		}
		ArrayList<PlaceModel> places = new ArrayList<>();
		while (result.next()) {
			Location location = new Location("provider");
			location.setLongitude(result.getDouble("Longitude"));
			location.setLatitude(result.getDouble("Latitude"));
			places.add(new PlaceModel(result.getInt("ID"), result.getInt("NumberOfCheckIns"), result.getString("name"),
					result.getString("Description"), result.getDouble("Rate"), location));
		}
		return places ;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNumberOfCheckIns() {
		return numberOfCheckIns;
	}

	public void setNumberOfCheckIns(int numberOfCheckins) {
		this.numberOfCheckIns = numberOfCheckins;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

}
