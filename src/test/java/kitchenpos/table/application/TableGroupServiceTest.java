package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;

@DisplayName("테이블 그룹(단체지정) : 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

	@Mock
	OrderRepository orderRepository;

	@Mock
	OrderTableRepository orderTableRepository;

	@Mock
	TableGroupRepository tableGroupRepository;

	@Mock
	OrderTable orderTable;

	@Mock
	TableGroup tableGroup;

	@InjectMocks
	private TableGroupService tableGroupService;

	@DisplayName("테이블 그룹을 생성할 때 주문 테이블이 비어있는 경우 예외처리 테스트")
	@Test
	void createTableGroupEmptyOrderTables() {
		// when
		when(tableGroup.getOrderTables()).thenReturn(Collections.emptyList());

		// then
		assertThatThrownBy(() -> {
			tableGroupService.create(tableGroup);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("테이블 그룹을 생성할 떄 주문 테이블의 갯수가 1개인 경우 예외처리 테스트")
	@Test
	void createTableGroupSizeOneOrderTableList() {
		// when
		when(tableGroup.getOrderTables()).thenReturn(Collections.singletonList(orderTable));

		// then
		assertThatThrownBy(() -> {
			tableGroupService.create(tableGroup);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("테이블 그룹을 생성할 때 존재하지 않는 주문 테이블일 경우 예외처리 테스트")
	@Test
	void createTableGroupUnknownOrderTable() {
		// given
		given(tableGroup.getOrderTables()).willReturn(Arrays.asList(orderTable, orderTable));

		// when
		when(orderTableRepository.findAllByIdIn(anyList())).thenReturn(Collections.emptyList());

		// then
		assertThatThrownBy(() -> {
			tableGroupService.create(tableGroup);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("테이블 그룹을 생성할 때 주문 테이블이 비어있는 상태인 경우 예외처리 테스트")
	@Test
	void createTableGroupEmptyOrderTable() {
		// given
		given(tableGroup.getOrderTables()).willReturn(Arrays.asList(orderTable, orderTable));
		given(orderTableRepository.findAllByIdIn(anyList())).willReturn(Arrays.asList(orderTable, orderTable));

		// when
		when((orderTable.isEmpty())).thenReturn(false);

		// then
		assertThatThrownBy(() -> {
			tableGroupService.create(tableGroup);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("테이블 그룹을 생성하는 테스트")
	@Test
	void createTableGroup() {
		// given
		given(tableGroup.getOrderTables()).willReturn(Arrays.asList(orderTable, orderTable));
		given(orderTableRepository.findAllByIdIn(anyList())).willReturn(Arrays.asList(orderTable, orderTable));
		given(orderTable.isEmpty()).willReturn(true);
		given(orderTable.getTableGroup()).willReturn(null);

		// when
		when(tableGroupRepository.save(tableGroup)).thenReturn(tableGroup);

		// then
		assertThat(tableGroupService.create(tableGroup)).isEqualTo(tableGroup);
	}

	@DisplayName("테이블 그룹을 해체할 때 주문 테이블의 상태가 완료가 아닌 경우 예외처리 테스트")
	@Test
	void unGroupOrderStatusNotComplement() {
		// when
		when(orderRepository.existsByOrderTableIdInAndOrderStatusIn(anyList(),
			anyList())).thenReturn(true);

		// then
		assertThatThrownBy(() -> {
			tableGroupService.ungroup(tableGroup.getId());
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("테이블 그룹을 해체하는 기능 테스트")
	@Test
	void unGroup() {
		// given
		given(orderTableRepository.findAllByTableGroupId(anyLong())).willReturn(Arrays.asList(orderTable, orderTable));
		given(orderTable.getTableGroup()).willReturn(null);
		given(orderTableRepository.save(orderTable)).willReturn(orderTable);

		// when
		tableGroupService.ungroup(tableGroup.getId());

		// then
		assertThat(orderTable.getTableGroup()).isNull();
	}
}
