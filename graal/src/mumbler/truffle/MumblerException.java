package mumbler.truffle;

/**
 * The base exception for any runtime errors.
 *
 */
public class MumblerException extends RuntimeException {
    public MumblerException(String message) {
        super(message);
    }

    private static final long serialVersionUID = 1L;
}
