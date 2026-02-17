package com.matchjob.application.port.outgoing;

import java.util.List;

public interface TokenService {
    String generateToken(String subject, List<String> roles);
    boolean isTokenValid(String token, String subject);
    String getSubject(String token);
    List<String> getRoles(String token);
}
