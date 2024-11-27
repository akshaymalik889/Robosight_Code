package com.example.TestingJWT.controllers;


import com.example.TestingJWT.dtos.LoginRequestDTO;
import com.example.TestingJWT.dtos.LoginResponseDTO;
import com.example.TestingJWT.dtos.SignUpRequestDTO;
import com.example.TestingJWT.dtos.UserDto;
import com.example.TestingJWT.services.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    ResponseEntity<UserDto> signUp(@RequestBody SignUpRequestDTO signupDto)
    {
        return new ResponseEntity<>(authService.signup(signupDto), HttpStatus.CREATED);
    }



    @PostMapping("/login")
    ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDto,
                                           HttpServletRequest httpServletRequest,
                                           HttpServletResponse httpServletResponse) {
        // at 0 index -> accessToken  (we pass accessToken in response)
        //at 1 index -> refreshToken  (we Store refreshToken in Cookie)
        String tokens[] = authService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());

        // Create Cookie for refresh Token
        Cookie cookie = new Cookie("token", tokens[1]);
        cookie.setHttpOnly(true); // Prevent access from JavaScript
        cookie.setSecure(false);  // Set to true in production for HTTPS
        cookie.setPath("/");      // Make cookie accessible across all endpoints
        cookie.setMaxAge(6 * 30 * 24 * 60 * 60); // 6 months
        httpServletResponse.addCookie(cookie);

       // httpServletResponse.addHeader("Set-Cookie", String.format("%s; SameSite=None", cookie.toString()));

        return ResponseEntity.ok(new LoginResponseDTO(tokens[0], System.currentTimeMillis() + 10 * 60 * 1000));
    }


    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDTO> refresh(HttpServletRequest request) {
        try {
            // Debugging: Log all cookies received in the request
            Cookie[] cookies = request.getCookies();
            System.out.println("Cookies Received: " + Arrays.toString(cookies));

            if (cookies == null || cookies.length == 0) {
                System.err.println("No cookies found in the request.");
                throw new AuthenticationServiceException("No cookies found");
            }

            // Extract the refresh token from the cookies
            String refreshToken = Arrays.stream(cookies)
                    .filter(cookie -> "token".equals(cookie.getName()))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElseThrow(() -> new AuthenticationServiceException("Refresh Token Not Found in Cookies"));

            System.out.println("Refresh Token Received: " + refreshToken);

            // Generate new access token
            String accessToken = authService.refreshToken(refreshToken);
            System.out.println("New Access Token Generated: " + accessToken);

            return ResponseEntity.ok(new LoginResponseDTO(accessToken, System.currentTimeMillis() + 10 * 60 * 1000));
        } catch (Exception e) {
            System.err.println("Error in Refresh Endpoint: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }




    @GetMapping("/me")
    public UserDto getCurrentUser(@RequestHeader("Authorization") String authorizationHeader) {

        if (!authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid Authorization header format");
        }

        String token = authorizationHeader.substring(7); // Remove "Bearer " prefix
        return authService.getCurrentUser(token);
    }


}
