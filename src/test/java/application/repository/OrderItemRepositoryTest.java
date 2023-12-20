package application.repository;

import application.model.Book;
import application.model.Order;
import application.model.OrderItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:database/orders/save_three_orders_to_orders_table.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:database/order_items/save_three_order_items_to_order_items_table.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:database/order_items/remove_three_order_items_from_order_items_table.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = "classpath:database/orders/remove_three_orders_from_orders_table.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class OrderItemRepositoryTest {
    private static final Long INVALID_ORDER_ID = 100L;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Test
    @DisplayName("""
            Verify findAllByOrderId() method with correct orderId
            """)
    void findAllByOrderId_ValidOrderId_ReturnTwoOrderItems() {
        OrderItem smallOrderItem
                = new OrderItem()
                .setId(1L)
                .setBook(new Book().setId(1L))
                .setOrder(new Order().setId(2L))
                .setQuantity(5)
                .setPrice(BigDecimal.valueOf(50));
        OrderItem bigOrderItem
                = new OrderItem()
                .setId(2L)
                .setBook(new Book().setId(2L))
                .setOrder(new Order().setId(2L))
                .setQuantity(10)
                .setPrice(BigDecimal.valueOf(80));
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
        OrderItem veryBigOrderItem
                = new OrderItem()
                .setId(3L)
                .setBook(new Book().setId(3L))
                .setOrder(new Order().setId(1L))
                .setQuantity(12)
                .setPrice(BigDecimal.valueOf(100));
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
