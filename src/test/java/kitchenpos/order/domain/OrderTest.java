package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.ErrorCode;
import kitchenpos.order.exception.OrderException;
import kitchenpos.table.domain.OrderTable;

@DisplayName("단위 테스트 : 주문 관련")
class OrderTest {

	private OrderTable orderTable;

	@DisplayName("주문 테이블이 비어있는 경우 주문 생성에 실패한다.")
	@Test
	void createOrderEmptyTable() {
		// given
		orderTable = OrderTable.of(10, true);

		// when // then
		assertThatThrownBy(() -> {
			Order.of(orderTable, OrderStatus.COOKING);
		}).isInstanceOf(OrderException.class)
			.hasMessageContaining(ErrorCode.ORDER_TABLE_IS_NO_EMPTY.getMessage());
	}

	@DisplayName("주문이 완료 상태인 경우 상태 변경 요청시 예외처리 테스트")
	@Test
	void changeOrderStatusIsCompletionOrder() {
		// given
		orderTable = OrderTable.of(10, false);
		Order order = Order.of(orderTable, OrderStatus.COMPLETION);

		// when // then
		assertThatThrownBy(() -> {
			order.updateOrderStatus(OrderStatus.COOKING);
		}).isInstanceOf(OrderException.class)
			.hasMessageContaining(ErrorCode.ORDER_IS_COMPLETION.getMessage());
	}
}
