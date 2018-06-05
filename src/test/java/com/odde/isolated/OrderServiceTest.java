package com.odde.isolated;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    BookDao mockBookDao = mock(BookDao.class);
    OrderService target = spy(new OrderService());

    @Before
    public void givenMockBookDao() {
        when(target.getBookDao()).thenReturn(mockBookDao);
    }

    @Test
    public void syncbookorders_3_orders_only_2_book_order() {
        givenOrders(
                order("Book1", "Book"),
                order("Book2", "Book"),
                order("Orange", "Food"));

        target.syncBookOrders();

        verifyBookDaoInsertWith("Book1", "Book2");
    }

    private void verifyBookDaoInsertWith(String... productNames) {
        ArgumentCaptor<Order> captor = forClass(Order.class);
        verify(mockBookDao, times(2)).insert(captor.capture());
        assertEquals(productNames[0], captor.getAllValues().get(0).getProductName());
        assertEquals(productNames[1], captor.getAllValues().get(1).getProductName());
    }

    private void givenOrders(Order... orders) {
        when(target.getOrders()).thenReturn(Arrays.asList(orders));
    }

    private Order order(final String productName, final String type) {
        return new Order() {{
            setProductName(productName);
            setType(type);
        }};
    }
}
