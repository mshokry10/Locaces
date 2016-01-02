

package com.locaces.locaces;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 * This class represents the model for the user of the application.
 * Has the logic for logging in, adding a new user, deleting a user
 * and updating an existing user.
 * @author Mohammad Yasser, Mahmoud Shokry
 * @version 1.0
 */

public class UserModel implements Serializable {

	// the unique identifier for the user
	private int id;

	// true if the user is a premium user, and false otherwise
	private boolean premium;

	// user info
	private String name, email, password;

	private ArrayList<Integer> friends; // Friends' IDs.

	public ArrayList<Integer> getFriends() {
		return friends;
	}

	/**
	 * The user is added, if they don't exist in the database.
	 * @param _name the name of the user.
	 * @param _password the password of the user.
	 * @param _email the email of the user.
	 * @return true if the user is added successfully, and false otherwise.
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public boolean addNewUser(String _name, String _password, String _email) throws ClassNotFoundException, SQLException {
		ResultSet resultSet = DataBaseManager.query("SELECT * FROM User WHERE Email = '" + _email + "';");

		// check if a user with the same email address already exists
		if (resultSet.next())
			return false;

		DataBaseManager.update("INSERT INTO User (Name, Password, Email) VALUES ('" + _name + "', "
				+ "'" + _password + "', " + "'" + _email + "');");

		return true;
	}

	/**
	 * Imports the user and initializes the attributes,
	 * if the login info matches that in the database.
	 * @param _email the email address for the user.
	 * @param _password the password for the user.
	 * @return true if the user was logged in successfully, and false otherwise.
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public boolean getUser(String _email, String _password) throws ClassNotFoundException, SQLException {
		ResultSet resultSet = DataBaseManager.query("SELECT * FROM User WHERE Email = " +
				"'" + _email + "' AND Password = '" + _password + "';");

		// check if the user doesn't exist
		if (!resultSet.next())
			return false;

		id = resultSet.getInt("ID");
		name = resultSet.getString("Name");
		password = _password;
		email =_email;
		premium = resultSet.getBoolean("Premium");

		return true;
	}

	/**
	 *
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */

	public void deleteUser() throws ClassNotFoundException, SQLException {
		DataBaseManager.update("DELETE FROM User WHERE ID = " + id);
	}

	/**
	 *
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */

	public boolean updateUser() throws ClassNotFoundException, SQLException {
		return updateUser(premium, name, email, password);
	}

	/**
	 * Precondition : User is logged in.
	 *
	 * @param _name
	 * @param _email
	 * @param _password
	 * @return true if user is updated successfully, false otherwise.
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public boolean updateUser(boolean _premium, String _name, String _email, String _password)
			throws ClassNotFoundException, SQLException {
		ResultSet resultSet = DataBaseManager.query("SELECT * FROM User WHERE Email = '" + _email + "';");

		// check if a user with the same email address already exists
		if (resultSet.next())
			return false;
		DataBaseManager.update("UPDATE User SET Premium = " + _premium + " , Name = " + _name + " , Email = " + _email
				+ " , Password = " + _password + " WHERE ID = " + id);

		return true;
	}

	/**
	 *
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */

	public void addFriend() throws ClassNotFoundException, SQLException {
		String friendsIDs = new String();
		for (Integer id : friends)
			friendsIDs += id + ",";

		DataBaseManager.update("UPDATE User SET Friends = " + friendsIDs + " WHERE ID = " + id);

	}

	/**
	 *
	 * @param firstId
	 * @param secondId
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	private void addFriend(int firstId, int secondId) throws ClassNotFoundException, SQLException {
		String friendsIDs = DataBaseManager.query("SELECT Friends FROM User WHERE ID = " + firstId).getString("Friends");
		friendsIDs += secondId + ",";
		DataBaseManager.update("UPDATE User SET Friends = " + friendsIDs + " WHERE ID = " + firstId);
	}

	/**
	 *
	 * @param friendId
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */

	public void addFriend(int friendId) throws ClassNotFoundException, SQLException {
		friends.add(friendId);
		addFriend(id, friendId);
		addFriend(friendId, id);
	}

	/**
	 * @return the id of the user.
	 */
	public int getId() {
		return id;
	}

	/**
	 * sets the id of the user.
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return true if the user is premium and false otherwise.
	 */
	public boolean getPremium() {
		return premium;
	}

	/**
	 * sets premium true if the user is premium and false otherwise.
	 * @param premium
	 */
	public void setPremium(boolean premium) {
		this.premium = premium;
	}

	/**
	 * @return the name of the user.
	 */
	public String getName() {
		return name;
	}

	/**
	 * sets the name of the user.
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return returns the password of the user.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * sets the password for the user.
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the email address for the user.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * sets the email address for the user.
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

}
