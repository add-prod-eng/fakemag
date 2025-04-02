package ro.unibuc.hello.exception;

public class InvalidCredentialsException extends RuntimeException {


    public InvalidCredentialsException() {
        super("Invalid credentials");
    }

    public String getMessage() {
        return super.getMessage();
    }
}
