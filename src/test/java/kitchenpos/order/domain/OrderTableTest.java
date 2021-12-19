package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import kitchenpos.common.ErrorCode;
import kitchenpos.order.exception.OrderException;
import kitchenpos.tableGroup.domain.TableGroup;
import kitchenpos.tableGroup.exception.TableException;

class OrderTableTest {

	private TableGroup tableGroup;

	@BeforeEach
	void setup() {
		tableGroup = TableGroup.from(Arrays.asList(OrderTable.of(10, tableGroup, true),
			OrderTable.of(10, tableGroup, true)));

	}

	@Test
	void changeEmptyNullTableGroup() {
		// given
		OrderTable orderTable = OrderTable.of(100, tableGroup, false);

		// when // then
		assertThatThrownBy(() -> {
			orderTable.empty(true);
		}).isInstanceOf(TableException.class)
			.hasMessageContaining(ErrorCode.ALREADY_HAS_TABLE_GROUP.getMessage());
	}

	@Test
	void changeTableGroup() {
		// given
		OrderTable orderTable = OrderTable.of(10, false);

		// when
		orderTable.changeTableGroup(tableGroup);

		// then
		assertThat(orderTable.getTableGroup()).isEqualTo(tableGroup);
	}

	@Test
	void addOrder() {
		// given
		OrderTable orderTable = OrderTable.of(100, tableGroup, false);
		Order order = Order.of(orderTable, OrderStatus.COOKING);

		// when
		orderTable.addOrder(order);

		// then
		assertThat(orderTable.getOrders().getOrders()).containsExactly(order);
	}

	@Test
	void changeEmptyIsNotCompletion() {
		// given
		OrderTable orderTable = OrderTable.of(100, false);
		orderTable.addOrder(Order.of(orderTable, OrderStatus.COOKING));

		// when // then
		assertThatThrownBy(() -> {
			orderTable.empty(true);
		}).isInstanceOf(OrderException.class);
	}

	@Test
	void changeNumberOfGuestsUnderZeroGuest() {
		// given
		OrderTable orderTable = OrderTable.of(100, false);

		// when // then
		assertThatThrownBy(() -> {
			orderTable.changeNumberOfGuests(-1);
		}).isInstanceOf(OrderException.class);
	}

	@Test
	void changeNumberOfGuestsEmptyTable() {
		// given
		OrderTable orderTable = OrderTable.of(100, true);

		// when // then
		assertThatThrownBy(() -> {
			orderTable.changeNumberOfGuests(10);
		}).isInstanceOf(OrderException.class);
	}

	@Test
	void changeNumberOfGuests() {
		// given
		OrderTable orderTable = OrderTable.of(100, false);

		// when
		orderTable.changeNumberOfGuests(10);

		// then
		assertThat(orderTable.getNumberOfGuests()).isEqualTo(10);
	}
}