package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import kitchenpos.common.ErrorCode;
import kitchenpos.order.dto.OrderLineItemResponse;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.exception.OrderException;
import kitchenpos.table.domain.OrderTable;

@Entity(name = "orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "order_table_id")
	private OrderTable orderTable;

	@Enumerated(EnumType.STRING)
	@Column
	private OrderStatus orderStatus;

	@Column
	private LocalDateTime orderedTime;

	@Embedded
	private OrderLineItems orderLineItems = OrderLineItems.EMPTY_ITEMS;

	protected Order() {
	}

	private Order(OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime) {
		this.orderTable = orderTable;
		this.orderStatus = orderStatus;
		this.orderedTime = orderedTime;
	}

	private Order(OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime,
		OrderLineItems orderLineItems) {
		this.orderTable = orderTable;
		this.orderStatus = orderStatus;
		this.orderedTime = orderedTime;
		this.orderLineItems = orderLineItems;
	}

	public static Order of(OrderTable orderTable, OrderStatus orderStatus) {
		validateOrderTable(orderTable);
		return new Order(orderTable, orderStatus, LocalDateTime.now());
	}

	public static Order of(OrderTable orderTable, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
		return new Order(orderTable, orderStatus, LocalDateTime.now(), OrderLineItems.of(orderLineItems));
	}

	private static void validateOrderTable(OrderTable orderTable) {
		validateEmptyOrderTable(orderTable);
	}

	private static void validateEmptyOrderTable(OrderTable orderTable) {
		if (orderTable.isEmpty()) {
			throw new OrderException(ErrorCode.ORDER_TABLE_IS_NO_EMPTY);
		}
	}

	public void updateOrderStatus(OrderStatus orderStatus) {
		isCompletion();
		this.orderStatus = orderStatus;
	}

	private void isCompletion() {
		if (orderStatus.equals(OrderStatus.COMPLETION)) {
			throw new OrderException(ErrorCode.ORDER_IS_COMPLETION);
		}
	}

	public void addOrderLineItems(List<OrderLineItem> savedOrderLineItems) {
		orderLineItems = OrderLineItems.of(savedOrderLineItems);
	}

	public Long getId() {
		return id;
	}

	public OrderTable getOrderTable() {
		return orderTable;
	}

	public String getOrderStatus() {
		return orderStatus.name();
	}

	public LocalDateTime getOrderedTime() {
		return orderedTime;
	}

	public List<OrderLineItem> getOrderLineItems() {
		return orderLineItems.getOrderLineItems();
	}

	public OrderResponse toResDto() {
		return OrderResponse.of(id, orderTable.toResDto(), orderStatus,
			OrderLineItemResponse.ofList(orderLineItems.getOrderLineItems()));
	}
}
