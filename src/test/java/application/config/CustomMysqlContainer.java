package application.config;

import org.testcontainers.containers.MySQLContainer;

public class CustomMysqlContainer extends MySQLContainer<CustomMysqlContainer> {
    private static final String DB_IMAGE = "mysql:8";
    private static CustomMysqlContainer mySQLContainer;

    private CustomMysqlContainer() {
        super(DB_IMAGE);
    }

    public static synchronized CustomMysqlContainer getInstance() {
        return mySQLContainer == null ? new CustomMysqlContainer() : mySQLContainer;
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
