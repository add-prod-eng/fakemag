package ro.unibuc.hello.exception;

public class InvalidEmail extends RuntimeException {


    public InvalidEmail(String emailError) {
        super("Invalid email: " + emailError);
    }

    public String getMessage() {
        return super.getMessage();
    }
}
