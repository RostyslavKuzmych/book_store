package application.security;

import java.util.List;

public class PublicEndpoints {
    private static List<String> endpoints = List.of("/auth/login", "/auth/registration");

    public static List<String> getEndpoints() {
        return endpoints;
    }
}
