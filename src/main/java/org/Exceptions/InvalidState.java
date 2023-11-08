package org.Exceptions;
/**
 * Exception class created to handle all the invalid scenarios related to the state of the game.
 */
public class InvalidState extends Exception{
    public InvalidState(String p_msg){ super(p_msg); }
}
