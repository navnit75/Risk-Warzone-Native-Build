package org.Exceptions;

/**
 * Exception class created to handle validity of the Map Based Invalid Conditions.
 */
public class MapInvalidException extends Exception {
    public MapInvalidException(String p_errorMsg) {
        super(p_errorMsg);
    }
}
