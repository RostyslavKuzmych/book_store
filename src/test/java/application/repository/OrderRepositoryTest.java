package application.repository;

import application.model.Order;
import application.model.Status;
import application.model.User;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:database/orders/save_three_orders_to_orders_table.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:database/orders/remove_three_orders_from_orders_table.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class OrderRepositoryTest {
    private static final Long ALICE_ID = 2L;
    private static final Long BOB_ID = 1L;
    private static final Long INVALID_USER_ID = 100L;
    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("""
            Verify findAllByUserId() method with correct userId
            """)
    void findAllByUserId_ValidUserId_ReturnTwoOrders() {
        Order firstAliceOrder = new Order()
                .setId(1L)
                .setUser(new User().setId(ALICE_ID))
                .setOrderDate(
                        LocalDateTime.of(2023, 12, 17, 2, 0, 0)
                ).setStatus(Status.RECEIVED)
                .setShippingAddress("Shevchenko 123A")
                .setTotal(BigDecimal.valueOf(100));
        Order secondAliceOrder = new Order()
                .setId(2L)
                .setUser(new User().setId(ALICE_ID))
                .setOrderDate(
                        LocalDateTime.of(2023, 12, 17, 3, 0, 0)
                ).setStatus(Status.RECEIVED)
                .setShippingAddress("Shevchenko 123A")
                .setTotal(BigDecimal.valueOf(130));
        List<Order> expected = List.of(firstAliceOrder, secondAliceOrder);
        List<Order> actual = orderRepository.findAllByUserId(ALICE_ID);
        assertEquals(2, actual.size());
        assertEquals(expected, actual);
    }
    @Test
    @DisplayName("""
            Verify findAllByUserId() method with correct userId
            """)
    void findAllByUserId_ValidUserId_ReturnOrder() {
        Order bobOrder = new Order()
                .setId(3L)
                .setUser(new User().setId(BOB_ID))
                .setOrderDate(
                        LocalDateTime.of(2023, 12, 19, 12, 0, 0)
                ).setStatus(Status.RECEIVED)
                .setShippingAddress("Shevchenko 122");
        List<Order> expected = List.of(bobOrder);
        List<Order> actual = orderRepository.findAllByUserId(BOB_ID);
        assertEquals(1, actual.size());
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Verify findAllByUserId() method with invalid userId
            """)
    void findAllByUserId_InvalidUserId_ReturnEmptyList() {
        List<Order> expected = new ArrayList<>();
        List<Order> actual = orderRepository.findAllByUserId(INVALID_USER_ID);
        assertEquals(expected, actual);
    }
}
