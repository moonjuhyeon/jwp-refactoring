package kitchenpos.table.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import kitchenpos.table.dto.OrderTableResponse;

@Entity
public class OrderTable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "table_group_id", foreignKey = @ForeignKey(name = "fk_order_table_table_group"))
	private TableGroup tableGroup;

	@Column(nullable = false)
	private Integer numberOfGuests;

	@Column(nullable = false)
	private Boolean empty;

	protected OrderTable() {
	}

	private OrderTable(Integer numberOfGuest, Boolean empty) {
		this.numberOfGuests = numberOfGuest;
		this.empty = empty;
	}

	public static OrderTable of(Integer numberOfGuest, Boolean empty) {
		return new OrderTable(numberOfGuest, empty);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TableGroup getTableGroup() {
		return tableGroup;
	}

	public void setTableGroup(TableGroup tableGroup) {
		this.tableGroup = tableGroup;
	}

	public int getNumberOfGuests() {
		return numberOfGuests;
	}

	public void setNumberOfGuests(Integer numberOfGuests) {
		this.numberOfGuests = numberOfGuests;
	}

	public Boolean isEmpty() {
		return empty;
	}

	public void setEmpty(boolean empty) {
		this.empty = empty;
	}

	public OrderTableResponse toResDto() {
		return OrderTableResponse.of(id, numberOfGuests, empty);
	}
}
