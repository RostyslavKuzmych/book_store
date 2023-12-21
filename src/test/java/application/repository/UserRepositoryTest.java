package application.repository;

import static org.junit.Assert.assertEquals;

import application.model.User;
import java.util.Optional;
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
    private static final String ALICE_EMAIL = "alice@example.com";
    private static final String ALICE_PASSWORD
            = "$2a$10$CduBSqrkcg8RKSADri166O0E/SArKip98V19W840zl2VSqi9ZA0ym";
    private static final String ALICE_SHIPPING_ADDRESS = "Shevchenko 123A";
    private static final String JOHN_EMAIL = "john@example.com";
    private static final String ROLE_SET = "roleSet";
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("""
            Verify findUserByEmail() method with correct userEmail
            """)
    void findUserByEmail_ValidUserEmail_ReturnBob() {
        User bob = new User()
                .setId(1L)
                .setEmail(BOB_EMAIL)
                .setPassword(BOB_PASSWORD)
                .setFirstName("Bob")
                .setLastName("Smith")
                .setShippingAddress(BOB_SHIPPING_ADDRESS);
        Optional<User> actual = userRepository.findUserByEmail(BOB_EMAIL);
        EqualsBuilder.reflectionEquals(bob, actual.get(), ROLE_SET);
    }

    @Test
    @DisplayName("""
            Verify findUserByEmail() method with correct userEmail
            """)
    void findUserByEmail_ValidUserEmail_ReturnAlice() {
        User alice = new User()
                .setId(2L)
                .setEmail(ALICE_EMAIL)
                .setPassword(ALICE_PASSWORD)
                .setFirstName("Alice")
                .setLastName("Johnson")
                .setShippingAddress(ALICE_SHIPPING_ADDRESS);
        Optional<User> actual = userRepository.findUserByEmail(ALICE_EMAIL);
        EqualsBuilder.reflectionEquals(alice, actual.get(), ROLE_SET);
    }

    @Test
    @DisplayName("""
            Verify findUserByEmai() method with invalid userEmail
            """)
    void findUserByEmail_InvalidUserEmail_ReturnOptionalEmpty() {
        Optional<User> actual = userRepository.findUserByEmail(JOHN_EMAIL);
        assertEquals(Optional.empty(), actual);
    }
}
