package kitchenpos.order.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;

@Service
@Transactional(readOnly = true)
public class OrderService {
	private final MenuRepository menuRepository;
	private final OrderRepository orderRepository;
	private final OrderLineItemRepository orderLineItemRepository;
	private final OrderTableRepository orderTableRepository;

	public OrderService(
		final MenuRepository menuRepository,
		final OrderRepository orderRepository,
		final OrderLineItemRepository orderLineItemRepository,
		final OrderTableRepository orderTableRepository
	) {
		this.menuRepository = menuRepository;
		this.orderRepository = orderRepository;
		this.orderLineItemRepository = orderLineItemRepository;
		this.orderTableRepository = orderTableRepository;
	}

	@Transactional
	public Order create(final OrderRequest orderRequest) {
		final List<OrderLineItemRequest> orderLineItems = orderRequest.getOrderLineItems();

		if (CollectionUtils.isEmpty(orderLineItems)) {
			throw new IllegalArgumentException();
		}

		final List<Long> menuIds = orderLineItems.stream()
			.map(OrderLineItemRequest::getMenuId)
			.collect(Collectors.toList());

		if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
			throw new IllegalArgumentException();
		}

		final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
			.orElseThrow(IllegalArgumentException::new);

		if (orderTable.isEmpty()) {
			throw new IllegalArgumentException();
		}
		;

		final Order savedOrder = orderRepository.save(orderRequest.toEntity(orderTable, OrderStatus.COOKING));

		final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
		for (final OrderLineItemRequest orderLineItemRequest : orderLineItems) {
			Menu menu = menuRepository.findById(orderLineItemRequest.getMenuId())
				.orElseThrow(IllegalArgumentException::new);
			OrderLineItem orderLineItem = OrderLineItem.of(savedOrder, menu, orderLineItemRequest.getQuantity());
			savedOrderLineItems.add(orderLineItemRepository.save(orderLineItem));
		}
		savedOrder.setOrderLineItems(savedOrderLineItems);

		return savedOrder;
	}

	public List<Order> list() {
		return orderRepository.findAll();
	}

	@Transactional
	public Order changeOrderStatus(final Long orderId, final OrderStatusRequest orderStatusRequest) {
		final Order savedOrder = orderRepository.findById(orderId)
			.orElseThrow(IllegalArgumentException::new);

		if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
			throw new IllegalArgumentException();
		}

		final OrderStatus orderStatus = orderStatusRequest.getOrderStatus();
		savedOrder.setOrderStatus(orderStatus);

		orderRepository.save(savedOrder);

		savedOrder.setOrderLineItems(orderLineItemRepository.findAllByOrderId(orderId));

		return savedOrder;
	}
}
