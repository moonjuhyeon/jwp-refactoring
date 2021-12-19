package kitchenpos.table.dto;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupResponse {

	private Long id;
	private LocalDateTime createdDate;
	private List<OrderTableResponse> orderTables;

	protected TableGroupResponse() {
	}

	private TableGroupResponse(Long id, LocalDateTime createdDate, List<OrderTableResponse> orderTables) {
		this.id = id;
		this.createdDate = createdDate;
		this.orderTables = orderTables;
	}

	public static TableGroupResponse of(Long id, LocalDateTime createdDate, List<OrderTableResponse> orderTables) {
		return new TableGroupResponse(id, createdDate, orderTables);
	}

	public Long getId() {
		return id;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public List<OrderTableResponse> getOrderTables() {
		return orderTables;
	}
}
