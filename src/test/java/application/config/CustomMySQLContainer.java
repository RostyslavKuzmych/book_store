package application.config;

import org.testcontainers.containers.MySQLContainer;

public class CustomMySQLContainer extends MySQLContainer<CustomMySQLContainer> {
    private static final String DB_IMAGE = "mysql:8";
    private static CustomMySQLContainer mySQLContainer;
    private CustomMySQLContainer() {
        super(DB_IMAGE);
    }

    public static synchronized CustomMySQLContainer getInstance() {
        return mySQLContainer == null? new CustomMySQLContainer() : mySQLContainer;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("TEST_DB_URL", mySQLContainer.getJdbcUrl());
        System.setProperty("TEST_DB_USERNAME", mySQLContainer.getUsername());
        System.setProperty("TEST_DB_PASSWORD", mySQLContainer.getPassword());
    }

    @Override
    public void stop() {
    }
}
