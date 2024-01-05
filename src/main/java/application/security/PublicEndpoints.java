package application.security;

import java.util.List;

public class PublicEndpoints {
    private static List<String> endpoints =
            List.of("/api/auth/login", "/api/auth/registration");

    public static List<String> getEndpoints() {
        return endpoints;
    }
}
