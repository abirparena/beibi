package mother.exceptions;

/**
 * Custom exception for invalid NID (National ID) format
 */
public class InvalidNIDException extends Exception {
    public InvalidNIDException(String message) {
        super(message);
    }
}