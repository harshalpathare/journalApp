package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.api.response.WeatherResponse;
import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.JournalEntryService;
import net.engineeringdigest.journalApp.service.UserService;
import net.engineeringdigest.journalApp.service.WeatherService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
@Autowired
private PasswordEncoder passwordEncoder;
@Autowired
    private UserService userService;
@Autowired
   private WeatherService weatherService;
@GetMapping
public List<User> getAllUsers(){
    return userService.getAll();
}



@PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user){
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userName = authentication.getName();
  User userInDb=  userService.findByUsername(userName);
    if (userInDb == null) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    if (user.getUserName() != null) {
        userInDb.setUserName(user.getUserName());
    }

    if (user.getPassword() != null) {
        userInDb.setPassword(
                passwordEncoder.encode(user.getPassword())
        );
    }


    userService.saveNewUser(userInDb);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
}
    @GetMapping("/greeting")
    public ResponseEntity<?> greeting() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        WeatherResponse weatherResponse = weatherService.getWeather("Mumbai");

        String greeting = "";

        if (weatherResponse != null &&
                weatherResponse.getCurrent() != null) {

            greeting = " | Weather feels like "
                    + weatherResponse.getCurrent().getFeelslike() + "°C";
        }

        return ResponseEntity.ok(
                "Hi " + authentication.getName() + greeting
        );
    }



}
