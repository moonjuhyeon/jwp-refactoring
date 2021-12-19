package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import kitchenpos.common.ErrorCode;
import kitchenpos.order.exception.OrderException;

class OrdersTest {

	private OrderTable orderTable;
	private Orders orders;

	@BeforeEach
	void setup() {
		orderTable = OrderTable.of(100, false);
	}

	@Test
	void hasCompletionOrder() {
		// given
		Order order1 = Order.of(orderTable, OrderStatus.COMPLETION);
		Order order2 = Order.of(orderTable, OrderStatus.COOKING);
		orders = Orders.from(Arrays.asList(order1, order2));

		// when // then
		assertThatThrownBy(() -> {
			orders.hasNotCompletion();
		}).isInstanceOf(OrderException.class)
			.hasMessageContaining(ErrorCode.ORDER_IS_NOT_COMPLETION.getMessage());
	}
}