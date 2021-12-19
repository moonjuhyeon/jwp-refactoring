package kitchenpos.order.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import kitchenpos.tableGroup.domain.TableGroup;

@Embeddable
public class OrderTables {

	@OneToMany(mappedBy = "tableGroup", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	private List<OrderTable> orderTables;

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

	public void changeTableGroup(TableGroup tableGroup) {
		orderTables.forEach(it -> {
			it.changeTableGroup(tableGroup);
		});
	}

	public boolean isEmpty() {
		return orderTables.isEmpty();
	}

	public boolean findAnyEmptyTable() {
		return orderTables.stream()
			.anyMatch(OrderTable::isEmpty);
	}
}
