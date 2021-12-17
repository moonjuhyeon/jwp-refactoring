package kitchenpos.table.dto;

public class OrderTableResponse {

	private Long id;
	private Integer numberOfGuest;
	private Boolean empty;

	protected OrderTableResponse() {
	}

	private OrderTableResponse(Long id, Integer numberOfGuest, Boolean empty) {
		this.id = id;
		this.numberOfGuest = numberOfGuest;
		this.empty = empty;
	}

	public static OrderTableResponse of(Long id, Integer numberOfGuest, Boolean empty) {
		return new OrderTableResponse(id, numberOfGuest, empty);
	}

	public Long getId() {
		return id;
	}

	public Integer getNumberOfGuest() {
		return numberOfGuest;
	}

	public Boolean getEmpty() {
		return empty;
	}
}
