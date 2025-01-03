package Example.Board.Exception;

public abstract class BoardException extends RuntimeException{

    public BoardException(String message) {
        super(message);
    }

    public BoardException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();
}
