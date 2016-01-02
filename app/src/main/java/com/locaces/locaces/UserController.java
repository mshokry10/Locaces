package com.locaces.locaces;

import java.sql.SQLException;

public class UserController {

  protected static UserModel user = new UserModel();

	/**
	 *
	 * @param email
	 * @param password
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public boolean login(String email, String password) throws ClassNotFoundException, SQLException {
		return user.getUser(email, password);
	}

	/**
	 *
	 * @param _name
	 * @param _password
	 * @param _email
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public boolean signup(String _name, String _password, String _email) throws ClassNotFoundException, SQLException {
		return user.addNewUser(_name, _password, _email);
	}

	/**
	 *
	 * @param creditCard
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static boolean upgrade(String creditCard) throws ClassNotFoundException, SQLException {
		if (verify(creditCard)) {
			user.setPremium(true);
			user.updateUser();
			return true;
		}
		return false;
	}

	/**
	 *
	 * @param creditCard
	 * @return
	 */

	public static boolean verify(String creditCard) {
		return true;
	}

	public static void sendFriendRequest(int id) {

	}

	/**
	 *
	 * @param id
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */

	public static void acceptFriendRequest(int id) throws ClassNotFoundException, SQLException {
		user.addFriend(id);
	}

	public static void logout() {
		user = new UserModel();
	}

	public static UserModel getUser() {
		return user;
	}

}
