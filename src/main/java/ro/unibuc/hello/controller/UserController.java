package ro.unibuc.hello.controller;

import io.micrometer.core.ipc.http.HttpSender.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


import ro.unibuc.hello.dto.UserDTO;
import ro.unibuc.hello.dto.UserLoginDTO;
import ro.unibuc.hello.dto.UserCreateDTO;
import ro.unibuc.hello.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ro.unibuc.hello.exception.EntityNotFoundException;
import ro.unibuc.hello.exception.InvalidCredentialsException;
import ro.unibuc.hello.exception.InvalidEmail;
import ro.unibuc.hello.exception.InvalidPassword;

import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) throws EntityNotFoundException {
        try{
            return ResponseEntity.ok(userService.getUserById(id));
        } catch (EntityNotFoundException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) throws EntityNotFoundException {
        try{
            return ResponseEntity.ok(userService.getUserByUsername(username));
        } catch (EntityNotFoundException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserCreateDTO userDTO) {
        try {
            userService.checkPassword(userDTO.getPassword());
            userService.checkEmail(userDTO.getEmail());
            return ResponseEntity.ok(userService.saveUser(userDTO));
        } catch (InvalidPassword e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(401).body(e.getMessage());
        } catch (InvalidEmail e){
            System.err.println(e.getMessage());
            return ResponseEntity.status(401).body(e.getMessage());
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.status(204).build();
        } catch (EntityNotFoundException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticateUser(@RequestBody UserLoginDTO userDTO) {
        try{
            userService.authenticateUser(userDTO);
            return ResponseEntity.ok().build();
        } catch (InvalidCredentialsException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(401).body(e.getMessage());
        } catch (EntityNotFoundException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
