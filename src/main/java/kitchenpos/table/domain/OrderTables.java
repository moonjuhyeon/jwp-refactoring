package kitchenpos.table.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class OrderTables {

	@OneToMany(mappedBy = "tableGroup")
	private List<OrderTable> orderTables = new ArrayList<>();

	protected OrderTables() {
	}

	private OrderTables(final List<OrderTable> orderTables) {
		this.orderTables = orderTables;
	}

	public static OrderTables from(final List<OrderTable> orderTables) {
		return new OrderTables(orderTables);
	}

	public List<OrderTable> getOrderTables() {
		return orderTables;
	}
}
