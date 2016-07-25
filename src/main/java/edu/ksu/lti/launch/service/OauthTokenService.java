package edu.ksu.lti.launch.service;

public interface OauthTokenService {
    String getOauthToken(String userId);
    String storeToken(String userId, String accessToken);
    String updateToken(String userId, String accessToken);
    String getRefreshToken(String userId);
    String updateRefreshToken(String userId, String refreshToken);
}
