package kitchenpos.table.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import kitchenpos.table.dto.OrderTableResponse;

@Entity
public class OrderTable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private Long tableGroupId;

	@Column(nullable = false)
	private Integer numberOfGuests;

	@Column(nullable = false)
	private Boolean empty;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTableGroupId() {
		return tableGroupId;
	}

	public void setTableGroupId(Long tableGroupId) {
		this.tableGroupId = tableGroupId;
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
