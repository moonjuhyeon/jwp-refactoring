package kitchenpos.tableGroup.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.exception.OrderException;
import kitchenpos.tableGroup.exception.TableException;

class TableGroupTest {

	@Test
	void createNullOrderTables() {
		// given
		List<OrderTable> orderTables = Collections.emptyList();

		// when // then
		assertThatThrownBy(() -> {
			TableGroup.from(orderTables);
		}).isInstanceOf(OrderException.class);
	}

	@Test
	void createEmptyOrderTable() {
		// given
		OrderTable orderTable1 = OrderTable.of(10, false);
		OrderTable orderTable2 = OrderTable.of(10, true);

		// when // then
		assertThatThrownBy(() -> {
			TableGroup.from(Arrays.asList(orderTable1, orderTable2));
		}).isInstanceOf(TableException.class);
	}
}