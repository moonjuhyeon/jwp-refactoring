package kitchenpos.table.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupDao;

@Service
public class TableGroupService {
	private final OrderRepository orderRepository;
	private final OrderTableRepository orderTableRepository;
	private final TableGroupDao tableGroupDao;

	public TableGroupService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository,
		final TableGroupDao tableGroupDao) {
		this.orderRepository = orderRepository;
		this.orderTableRepository = orderTableRepository;
		this.tableGroupDao = tableGroupDao;
	}

	@Transactional
	public TableGroup create(final TableGroup tableGroup) {
		final List<OrderTable> orderTables = tableGroup.getOrderTables();

		if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
			throw new IllegalArgumentException();
		}

		final List<Long> orderTableIds = orderTables.stream()
			.map(OrderTable::getId)
			.collect(Collectors.toList());

		final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);

		if (orderTables.size() != savedOrderTables.size()) {
			throw new IllegalArgumentException();
		}

		for (final OrderTable savedOrderTable : savedOrderTables) {
			if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroupId())) {
				throw new IllegalArgumentException();
			}
		}

		tableGroup.setCreatedDate(LocalDateTime.now());

		final TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

		final Long tableGroupId = savedTableGroup.getId();
		for (final OrderTable savedOrderTable : savedOrderTables) {
			savedOrderTable.setTableGroupId(tableGroupId);
			savedOrderTable.setEmpty(false);
			orderTableRepository.save(savedOrderTable);
		}
		savedTableGroup.setOrderTables(savedOrderTables);

		return savedTableGroup;
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

		for (final OrderTable orderTable : orderTables) {
			orderTable.setTableGroupId(null);
			orderTableRepository.save(orderTable);
		}
	}
}
