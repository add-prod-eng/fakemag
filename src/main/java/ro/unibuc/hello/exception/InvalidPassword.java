package ro.unibuc.hello.exception;

public class InvalidPassword extends RuntimeException {


    public InvalidPassword(String passError) {
        super("Invalid password: " + passError);
    }

    public String getMessage() {
        System.out.println("Invalid password message:");
        System.out.println(super.getMessage());
        return super.getMessage();
    }
}
