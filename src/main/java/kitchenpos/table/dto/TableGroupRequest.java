package kitchenpos.table.dto;

import java.util.List;

public class TableGroupRequest {

	private List<Long> orderTables;

	protected TableGroupRequest() {
	}

	private TableGroupRequest(List<Long> orderTables) {
		this.orderTables = orderTables;
	}

	public static TableGroupRequest of(List<Long> orderTables) {
		return new TableGroupRequest(orderTables);
	}

	public List<Long> getOrderTables() {
		return orderTables;
	}
}
