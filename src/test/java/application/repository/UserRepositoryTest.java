package application.repository;

import static org.junit.Assert.assertEquals;

import application.model.User;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    private static final String BOB_EMAIL = "bob@example.com";
    private static final String BOB_PASSWORD
            = "$2a$10$nA2TJNchONFwvYeKf7kALuwncLTIoFuywvK4YMa21IrRL.pcB/542";
    private static final String BOB_SHIPPING_ADDRESS = "Shevchenko 122";
    private static final String BOB_FIRST_NAME = "Bob";
    private static final String BOB_LAST_NAME = "Smith";
    private static final String ALICE_FIRST_NAME = "Alice";
    private static final String ALICE_LAST_NAME = "Johnson";
    private static final String ALICE_EMAIL = "alice@example.com";
    private static final String ALICE_PASSWORD
            = "$2a$10$CduBSqrkcg8RKSADri166O0E/SArKip98V19W840zl2VSqi9ZA0ym";
    private static final String ALICE_SHIPPING_ADDRESS = "Shevchenko 123A";
    private static final String JOHN_EMAIL = "john@example.com";
    private static final String ROLE_SET = "roleSet";
    private static User alice;
    private static User bob;
    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    static void beforeAll() {
        alice = new User()
                .setId(2L)
                .setEmail(ALICE_EMAIL)
                .setPassword(ALICE_PASSWORD)
                .setFirstName(ALICE_FIRST_NAME)
                .setLastName(ALICE_LAST_NAME)
                .setShippingAddress(ALICE_SHIPPING_ADDRESS);
        bob = new User()
                .setId(1L)
                .setEmail(BOB_EMAIL)
                .setPassword(BOB_PASSWORD)
                .setFirstName(BOB_FIRST_NAME)
                .setLastName(BOB_LAST_NAME)
                .setShippingAddress(BOB_SHIPPING_ADDRESS);
    }

    @Test
    @DisplayName("""
            Verify findUserByEmail() method with correct userEmail
            """)
    void findUserByEmail_ValidUserEmail_ReturnBob() {
        Optional<User> actual = userRepository.findUserByEmail(BOB_EMAIL);
        EqualsBuilder.reflectionEquals(bob, actual, ROLE_SET);
    }

    @Test
    @DisplayName("""
            Verify findUserByEmail() method with correct userEmail
            """)
    void findUserByEmail_ValidUserEmail_ReturnAlice() {
        Optional<User> actual = userRepository.findUserByEmail(ALICE_EMAIL);
        EqualsBuilder.reflectionEquals(alice, actual, ROLE_SET);
    }

    @Test
    @DisplayName("""
            Verify findUserByEmail() method with invalid userEmail
            """)
    void findUserByEmail_InvalidUserEmail_ReturnOptionalEmpty() {
        Optional<User> actual = userRepository.findUserByEmail(JOHN_EMAIL);
        assertEquals(Optional.empty(), actual);
    }
}
