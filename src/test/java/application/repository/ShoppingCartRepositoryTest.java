package application.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import application.model.ShoppingCart;
import application.model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ShoppingCartRepositoryTest {
    private static final Long ALICE_ID = 2L;
    private static final String ALICE_EMAIL = "alice@example.com";
    private static final String ALICE_PASSWORD = "alice123";
    private static final String ALICE_FIRST_NAME = "alice";
    private static final String ALICE_LAST_NAME = "smith";
    private static User alice;
    private static final Long INVALID_USER_ID = 100L;
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @BeforeAll
    static void beforeAll() {
        alice = new User()
                .setId(ALICE_ID)
                .setEmail(ALICE_EMAIL)
                .setPassword(ALICE_PASSWORD)
                .setFirstName(ALICE_FIRST_NAME)
                .setLastName(ALICE_LAST_NAME);
    }

    @Test
    @DisplayName("""
            Verify getShoppingCartByUserId() method with correct userId
            """)
    void getShoppingCartByUserId_ValidUserId_ReturnShoppingCart() {
        ShoppingCart expected = new ShoppingCart()
                .setId(1L)
                .setUser(alice);
        ShoppingCart actual = shoppingCartRepository.getShoppingCartByUserId(ALICE_ID);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Verify getShoppingCartByUserId() method with invalid userId
            """)
    void getShoppingCartByUserId_InvalidUserId_ReturnNull() {
        ShoppingCart actual = shoppingCartRepository.getShoppingCartByUserId(INVALID_USER_ID);
        assertNull(actual);
    }
}
