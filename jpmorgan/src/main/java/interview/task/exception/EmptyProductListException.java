package interview.task.exception;

public class EmptyProductListException extends RuntimeException {
    public EmptyProductListException(String message) {
        super(message);
    }
}

