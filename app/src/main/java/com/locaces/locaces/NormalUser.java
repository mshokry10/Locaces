package com.locaces.locaces;

/**
 * This class represents a normal user.
 * Extends the UserController class.
 * @author Mahmoud Shokry
 * @version 1.0
 */
public class NormalUser extends UserController {

    // TODO: implement this class
    public void upgrade() {
        user.setPremium(true);
    }
}
