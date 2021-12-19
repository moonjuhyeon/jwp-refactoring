package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import kitchenpos.tableGroup.domain.TableGroup;

class OrderTablesTest {

	private TableGroup tableGroup;
	private OrderTables orderTables;

	@BeforeEach
	void setup() {
		orderTables = OrderTables.from(Arrays.asList(OrderTable.of(10, tableGroup, true),
			OrderTable.of(10, tableGroup, true)));
		tableGroup = TableGroup.from(orderTables);

	}

	@Test
	void changeTableGroup() {
		// when
		orderTables.changeTableGroup(tableGroup);

		// then
		assertThat(orderTables.getOrderTables().get(0).getTableGroup()).isEqualTo(tableGroup);
	}

	@Test
	void isEmpty() {
		// given
		orderTables = OrderTables.from(Collections.emptyList());

		// when
		boolean result = orderTables.isEmpty();

		// then
		assertThat(result).isTrue();
	}

	@Test
	void findAnyEmptyTable() {
		// given
		OrderTable orderTable1 = OrderTable.of(10, true);
		OrderTable orderTable2 = OrderTable.of(10, false);
		orderTables = OrderTables.from(Arrays.asList(orderTable1, orderTable2));

		// when
		boolean result = orderTables.findAnyEmptyTable();

		// then
		assertThat(result).isTrue();
	}
}