package application.repository;

import static org.junit.Assert.assertEquals;

import application.model.Book;
import application.model.Order;
import application.model.OrderItem;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:database/orders/save_three_orders_to_orders_table.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:database/order_items/save_three_orderItems_to_order_items_table.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:database/order_items/remove_three_orderItems_from_order_items_table.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = "classpath:database/orders/remove_three_orders_from_orders_table.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class OrderItemRepositoryTest {
    private static final Long INVALID_ORDER_ID = 100L;
    private static OrderItem smallOrderItem;
    private static OrderItem bigOrderItem;
    private static OrderItem veryBigOrderItem;
    @Autowired
    private OrderItemRepository orderItemRepository;

    @BeforeAll
    static void beforeAll() {
        smallOrderItem = new OrderItem()
                .setId(1L)
                .setBook(new Book().setId(1L))
                .setOrder(new Order().setId(2L))
                .setQuantity(5)
                .setPrice(BigDecimal.valueOf(55));
        bigOrderItem = new OrderItem()
                .setId(2L)
                .setBook(new Book().setId(2L))
                .setOrder(new Order().setId(2L))
                .setQuantity(7)
                .setPrice(BigDecimal.valueOf(140));
        veryBigOrderItem = new OrderItem()
                .setId(3L)
                .setBook(new Book().setId(3L))
                .setOrder(new Order().setId(1L))
                .setQuantity(20)
                .setPrice(BigDecimal.valueOf(180));
    }

    @Test
    @DisplayName("""
            Verify findAllByOrderId() method with correct orderId
            """)
    void findAllByOrderId_ValidOrderId_ReturnTwoOrderItems() {
        List<OrderItem> expected =
                List.of(smallOrderItem, bigOrderItem);
        List<OrderItem> actual
                = orderItemRepository.findAllByOrderId(2L);
        assertEquals(2, actual.size());
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Verify findAllByOrderId() method with correct orderId
            """)
    void findAllByOrderId_ValidOrderId_ReturnOrderItem() {
        List<OrderItem> expected =
                List.of(veryBigOrderItem);
        List<OrderItem> actual
                = orderItemRepository.findAllByOrderId(1L);
        assertEquals(1, actual.size());
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Verify findAllByOrderId() method with invalid orderId
            """)
    void findAllByOrderId_InvalidOrderId_ReturnEmptyList() {
        List<OrderItem> expected = new ArrayList<>();
        List<OrderItem> actual = orderItemRepository.findAllByOrderId(INVALID_ORDER_ID);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Verify findAllByOrderId() method with correct orderId
            """)
    void findAllByOrderId_ValidOrderId_ReturnEmptyList() {
        List<OrderItem> expected = new ArrayList<>();
        List<OrderItem> actual = orderItemRepository.findAllByOrderId(3L);
        assertEquals(expected, actual);
    }
}
