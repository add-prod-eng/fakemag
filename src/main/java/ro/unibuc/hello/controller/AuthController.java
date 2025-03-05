package main.java.ro.unibuc.hello.controller;

import main.java.ro.unibuc.hello.responses.LoginResponse;
import main.java.ro.unibuc.hello.service.JwtService;
import main.java.ro.unibuc.hello.service.AuthenticationService;
import main.java.ro.unibuc.hello.dto.RegisterUserDTO;
import main.java.ro.unibuc.hello.dto.LoginUserDTO;
import main.java.ro.unibuc.hello.model.User;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/auth")
@RestController
public class AuthController {
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final AuthenticationService authService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterUserDTO registerUserDTO) {
        User registeredUser = authService.register(registerUserDTO);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginUserDTO loginUserDTO) {
        User authUser = authService.auth(loginUserDTO);
        String token = jwtService.generateToken(authUser);
        return ResponseEntity.ok(new LoginResponse(token, jwtService.getExpirationDateFromToken(token)));
    }

    @GetMapping("/getUser")
    public ResponseEntity<User> getUser(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        token = token.substring(7);
        String username = jwtService.extractClaim(token, Claims::getSubject);
        User user = authService.getUser(username);
        return ResponseEntity.ok(user);
    }
}
