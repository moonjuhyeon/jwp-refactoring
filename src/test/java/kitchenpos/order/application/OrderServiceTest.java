package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

@DisplayName("주문 : 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

	@Mock
	MenuRepository menuRepository;

	@Mock
	OrderRepository orderRepository;

	@Mock
	OrderTableRepository orderTableRepository;

	@Mock
	OrderLineItemRepository orderLineItemRepository;

	@Mock
	Order order;

	@Mock
	OrderTable orderTable;

	private OrderRequest orderRequest;

	private OrderStatusRequest orderStatusRequest;

	@InjectMocks
	private OrderService orderService;

	@DisplayName("주문 생성시 주문 항목이 비어있는 경우 예외처리 테스트")
	@Test
	void createOrderEmptyOrderLineItems() {
		// given
		orderRequest = OrderRequest.of(orderTable.getId(), Collections.emptyList());

		// when // then
		assertThatThrownBy(() -> {
			orderService.create(orderRequest);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 생성시 주문 항목이 메뉴에 존재하지 않은 경우 예외처리 테스트")
	@Test
	void createOrderCompareMenuSize() {
		// given
		orderRequest = OrderRequest.of(orderTable.getId(), Collections.singletonList(any(OrderLineItemRequest.class)));
		
		// when
		when(menuRepository.countByIdIn(anyList())).thenReturn(3L);

		// then
		assertThatThrownBy(() -> {
			orderService.create(orderRequest);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 생성시 주문 테이블이 존재하지 않은 경우 예외처리 테스트")
	@Test
	void createOrderUnknownOrderTable() {
		// given
		given(order.getOrderLineItems()).willReturn(Collections.singletonList(any(OrderLineItem.class)));
		given(menuRepository.countByIdIn(anyList())).willReturn(1L);
		orderRequest = OrderRequest.of(orderTable.getId(), Collections.singletonList(any(OrderLineItemRequest.class)));

		// when
		when(orderTableRepository.findById(anyLong())).thenThrow(IllegalArgumentException.class);

		// then
		assertThatThrownBy(() -> {
			orderService.create(orderRequest);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 생성시 주문 테이블이 비어있는 경우 예외처리 테스트")
	@Test
	void createOrderEmptyOrderTable() {
		// given
		given(order.getOrderLineItems()).willReturn(Collections.singletonList(any(OrderLineItem.class)));
		given(menuRepository.countByIdIn(anyList())).willReturn(1L);
		given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(orderTable));
		orderRequest = OrderRequest.of(orderTable.getId(), Collections.singletonList(any(OrderLineItemRequest.class)));

		// when
		when(orderTable.isEmpty()).thenReturn(true);

		// then
		assertThatThrownBy(() -> {
			orderService.create(orderRequest);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 생성 테스트")
	@Test
	void createOrder() {
		// given
		given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(orderTable));
		given(orderTable.isEmpty()).willReturn(false);
		orderRequest = OrderRequest.of(orderTable.getId(), Collections.singletonList(any(OrderLineItemRequest.class)));

		// when
		when(orderRepository.save(order)).thenReturn(order);

		// then
		assertThat(orderService.create(orderRequest)).isEqualTo(order);
	}

	@DisplayName("주문 목록 조회 테스트")
	@Test
	void getList() {
		// given
		given(orderLineItemRepository.findAllByOrderId(anyLong())).willReturn(
			Collections.singletonList(any(OrderLineItem.class)));

		// when
		when(orderRepository.findAll()).thenReturn(Collections.singletonList(order));

		// then
		assertThat(orderService.list()).containsExactly(order);
	}

	@DisplayName("주문 상태 변경시 존재하지 않는 주문인 경우 예외처리 테스트")
	@Test
	void changeOrderStatusUnknownOrder() {
		// when
		when(orderRepository.findById(anyLong())).thenThrow(IllegalArgumentException.class);
		orderStatusRequest = OrderStatusRequest.from(OrderStatus.MEAL);

		// then
		assertThatThrownBy(() -> {
			orderService.changeOrderStatus(anyLong(), orderStatusRequest);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 상태 변경시 이미 완료 상태인 경우 예외처리 테스트")
	@Test
	void changeOrderCompletionStatus() {
		// given
		given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));
		orderStatusRequest = OrderStatusRequest.from(OrderStatus.MEAL);

		// when
		when(order.getOrderStatus()).thenReturn(OrderStatus.COMPLETION.name());

		// then
		assertThatThrownBy(() -> {
			orderService.changeOrderStatus(anyLong(), orderStatusRequest);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 상태 변경 테스트")
	@Test
	void changeOrderStatus() {
		// given
		given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));
		given(order.getOrderStatus()).willReturn(OrderStatus.MEAL.name());
		orderStatusRequest = OrderStatusRequest.from(OrderStatus.MEAL);

		// when
		when(orderRepository.save(order)).thenReturn(order);

		// then
		assertThat(orderService.changeOrderStatus(anyLong(), orderStatusRequest)).isEqualTo(order);
	}
}
