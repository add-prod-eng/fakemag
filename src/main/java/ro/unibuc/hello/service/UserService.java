package ro.unibuc.hello.service;

import ro.unibuc.hello.data.UserEntity;
import ro.unibuc.hello.data.UserRepository;
import ro.unibuc.hello.dto.UserDTO;
import ro.unibuc.hello.dto.UserCreateDTO;
import ro.unibuc.hello.dto.UserLoginDTO;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicInteger;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.unibuc.hello.exception.EntityNotFoundException;
import ro.unibuc.hello.exception.InvalidCredentialsException;
import ro.unibuc.hello.exception.InvalidPassword;

import java.util.List;
import java.util.stream.Collectors;
import org.mindrot.jbcrypt.BCrypt;
import java.util.Optional;
import io.micrometer.core.instrument.Gauge;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    private final AtomicLong counter = new AtomicLong();

    private final MeterRegistry meterRegistry;

    private final Counter userCounter;

    private final Counter userDeletedCounter;

    private final Counter authCounter;

    private final Counter gmailCounter;

    private  AtomicInteger authAvgResponseTimeGauge;


    public UserService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.userCounter = Counter.builder("user.created").register(meterRegistry);
        this.userDeletedCounter = Counter.builder("user.deleted").register(meterRegistry);
        this.authCounter = Counter.builder("user.nr_auth").register(meterRegistry);
        this.gmailCounter = Counter.builder("user.gmail").register(meterRegistry);
        this.authAvgResponseTimeGauge = meterRegistry.gauge("user.auth_avg_response_time", new AtomicInteger(0));
    }

    public UserDTO getUserById(String id) throws EntityNotFoundException {
        Optional<UserEntity> optionalUser = userRepository.findById(id);
        UserEntity user = optionalUser.orElseThrow(() -> new EntityNotFoundException(id));
        return new UserDTO(user.getId(), user.getUsername(), user.getEmail());
    }

    public UserDTO getUserByUsername(String username) throws EntityNotFoundException {
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            throw new EntityNotFoundException(username);
        }
        return new UserDTO(user.getId(), user.getUsername(), user.getEmail());
    }

    public List<UserDTO> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();
        return users.stream()
                .map(user -> new UserDTO(user.getId(), user.getUsername(), user.getEmail()))
                .collect(Collectors.toList());
    }

    public UserDTO saveUser(UserCreateDTO userDTO) {
        UserEntity user = new UserEntity(
                userDTO.getUsername(),
                Long.toString(counter.incrementAndGet()),
                userDTO.getPassword(),
                userDTO.getEmail()
        );
        userRepository.save(user);
        userCounter.increment();
        if(user.getEmail().split("@").equals("gmail.com"))
            gmailCounter.increment();
        return new UserDTO(user.getId(), user.getUsername(), user.getEmail());
    }

    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException(id);
        }
        userRepository.deleteById(id);
        userDeletedCounter.increment();
    }

    public void checkPassword(String password) {
        if (! (password.length() >= 6)){
            throw new InvalidPassword("password must be at least 6 characters long");
        }
        boolean hasLower = false;
        boolean hasUpper = false;
        boolean hasDigit = false;
        for (char c : password.toCharArray()) {
            if (Character.isLowerCase(c)) {
                hasLower = true;
            }
            if (Character.isUpperCase(c)) {
                hasUpper = true;
            }
            if (Character.isDigit(c)) {
                hasDigit = true;
            }
        }
        if(!hasLower) {
            throw new InvalidPassword("password must contain at least one lowercase letter");
        }
        if(!hasUpper) {
            throw new InvalidPassword("password must contain at least one uppercase letter");
        } 
        if(!hasDigit) {
            throw new InvalidPassword("password must contain at least one digit");
        }
    }

    public void checkEmail(String email) {
        if(!email.contains("@")) {
            throw new InvalidPassword("email must contain @");
        }
        if(!email.contains(".")) {
            throw new InvalidPassword("email must contain domain .");
        }
    }

    public void authenticateUser(UserLoginDTO userDTO) {
        long startTime = System.currentTimeMillis();
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        UserEntity user = userRepository.findByUsername(username);
        if(user == null) {
            throw new EntityNotFoundException(username);
        }
        if(!(BCrypt.checkpw(password, user.getPassword())))
            throw new InvalidCredentialsException();
        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;
        authAvgResponseTimeGauge.set((int) responseTime);
        authCounter.increment();
    }

    public void equalUser(UserLoginDTO userDTO, String userId){
        UserEntity user = userRepository.findById(userId).orElse(null);
        if(!(user != null && user.getUsername().equals(userDTO.getUsername())))
            throw new InvalidCredentialsException();
    }
}
