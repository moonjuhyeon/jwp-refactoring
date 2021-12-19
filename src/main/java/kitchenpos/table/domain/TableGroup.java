package kitchenpos.table.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "table_group")
public class TableGroup {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private LocalDateTime createdDate;

	@Embedded
	private OrderTables orderTables = new OrderTables();

	protected TableGroup() {
	}

	private TableGroup(LocalDateTime createdDate, OrderTables orderTables) {
		this.createdDate = createdDate;
		this.orderTables = orderTables;
	}

	public static TableGroup from(OrderTables orderTables) {
		return new TableGroup(LocalDateTime.now(), orderTables);
	}

	public static TableGroup from(List<OrderTable> orderTables) {
		return new TableGroup(LocalDateTime.now(), OrderTables.from(orderTables));
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(final LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public List<OrderTable> getOrderTables() {
		return orderTables.getOrderTables();
	}

	public void setOrderTables(final List<OrderTable> orderTables) {
		this.orderTables = OrderTables.from(orderTables);
	}
}
