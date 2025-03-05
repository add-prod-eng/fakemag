package main.java.ro.unibuc.hello.data;

import org.springframework.data.annotation.Id;

public class UserEntity {

    @Id
    private String id;

    private String username;
    private String password;
    private String email;

    public UserEntity() {
    }

    public UserEntity(String id, String name, String email, String password) {
        this.id = id;
        this.username = name;
        this.password = password;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        this.username = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return String.format(
                "User[id='%s', name='%s', email='%s']",
                id, username, email);
    }
}
