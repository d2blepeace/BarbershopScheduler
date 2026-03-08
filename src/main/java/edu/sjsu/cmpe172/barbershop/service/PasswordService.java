package edu.sjsu.cmpe172.barbershop.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // Hash function
    public String hashPassword(String rawPwd) {
        return encoder.encode(rawPwd);
    }

    // password check
    public boolean matches(String rawPwd, String storedHash) {
        return encoder.matches(rawPwd, storedHash);
    }

}
