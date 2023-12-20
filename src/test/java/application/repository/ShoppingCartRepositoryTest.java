package application.repository;

import application.model.ShoppingCart;
import application.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ShoppingCartRepositoryTest {
    private static final Long ALICE_ID = 2L;
    private static final Long INVALID_USER_ID = 100L;
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Test
    @DisplayName("""
            Verify getShoppingCartByUserId() method with correct userId
            """)
    void getShoppingCartByUserId_ValidUserId_ReturnShoppingCart() {
        ShoppingCart expected = new ShoppingCart().setId(1L).setUser(new User().setId(ALICE_ID));
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
