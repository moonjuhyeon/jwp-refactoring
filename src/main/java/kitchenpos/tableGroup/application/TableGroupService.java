package kitchenpos.tableGroup.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.common.ErrorCode;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.exception.OrderException;
import kitchenpos.tableGroup.domain.TableGroup;
import kitchenpos.tableGroup.domain.TableGroupRepository;
import kitchenpos.tableGroup.dto.TableGroupRequest;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
	private final OrderRepository orderRepository;
	private final OrderTableRepository orderTableRepository;
	private final TableGroupRepository tableGroupRepository;

	public TableGroupService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository,
		final TableGroupRepository tableGroupRepository) {
		this.orderRepository = orderRepository;
		this.orderTableRepository = orderTableRepository;
		this.tableGroupRepository = tableGroupRepository;
	}

	@Transactional
	public TableGroup create(final TableGroupRequest tableGroupRequest) {
		return tableGroupRepository.save(
			tableGroupRequest.toEntity(orderTablesFindIds(tableGroupRequest.getOrderTables())));
	}

	private List<OrderTable> orderTablesFindIds(List<Long> ids) {
		return ids.stream()
			.map(this::orderTableFindById)
			.collect(Collectors.toList());
	}

	private OrderTable orderTableFindById(Long id) {
		return orderTableRepository.findById(id)
			.orElseThrow(() -> {
				throw new OrderException(ErrorCode.ORDER_TABLE_IS_NULL);
			});
	}

	@Transactional
	public void ungroup(final Long tableGroupId) {
		final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

		final List<Long> orderTableIds = orderTables.stream()
			.map(OrderTable::getId)
			.collect(Collectors.toList());

		if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
			orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
			throw new IllegalArgumentException();
		}
	}
}
