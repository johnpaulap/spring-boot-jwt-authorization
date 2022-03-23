package com.sha.springbootjwtauthorization.controller;

import com.sha.springbootjwtauthorization.model.User;
import com.sha.springbootjwtauthorization.service.AuthenticationService;
import com.sha.springbootjwtauthorization.service.JwtTokenRefreshService;
import com.sha.springbootjwtauthorization.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("api/authentication")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenRefreshService jwtTokenRefreshService;

    @PostMapping("sign-up")
    public ResponseEntity<?> signup(@RequestBody User user)
    {
        if (userService.findByUsername(user.getUsername()).isPresent())
        {
            return new ResponseEntity<>(user.getUsername()+",Already Exists",HttpStatus.CONFLICT);
        }
        if (user.getCreateTime() == null)
        {
            user.setCreateTime(LocalDateTime.now());
        }
        userService.saveUser(user);
        return new ResponseEntity<User>(user,HttpStatus.CREATED);
    }

    @PostMapping("sign-in")
    public ResponseEntity<?> SignIn(@RequestBody User user)
    {
        return new ResponseEntity<>(authenticationService.SignInAndReturnJWT(user),HttpStatus.OK);
    }

    @PostMapping("refresh-token")
    public ResponseEntity<?> refreshToken(@RequestParam String token)
    {
        return ResponseEntity.ok(jwtTokenRefreshService.generateAccessTokenFromRefreshToken(token));
    }
}
