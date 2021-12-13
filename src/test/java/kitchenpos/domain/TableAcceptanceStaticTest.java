package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class TableAcceptanceStaticTest {

	public static final String TABLES_PATH = "/api/tables";

	public static OrderTable 테이블이_생성_되어있음(OrderTable params) {
		return 테이블_생성_요청(params).as(OrderTable.class);
	}

	public static ExtractableResponse<Response> 테이블_목록을_조회함() {
		return RestAssured.given().log().all()
			.when()
			.get(TABLES_PATH)
			.then().log().all()
			.extract();
	}

	public static void 테이블_목록이_조회됨(ExtractableResponse<Response> response, List<Long> idList) {
		List<Long> responseIdList = response.body()
			.jsonPath().getList(".", OrderTable.class)
			.stream()
			.map(OrderTable::getId)
			.collect(Collectors.toList());

		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(responseIdList).containsAnyElementsOf(idList);
	}

	public static OrderTable 테이블_요청값_생성(int numberOfGuest, boolean empty) {
		OrderTable emptyTable = new OrderTable();
		emptyTable.setNumberOfGuests(numberOfGuest);
		emptyTable.setEmpty(empty);
		return emptyTable;
	}

	public static ExtractableResponse<Response> 테이블_생성_요청(OrderTable params) {
		return RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(params)
			.when()
			.post(TABLES_PATH)
			.then().log().all()
			.extract();
	}

	public static void 테이블_생성됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
	}
}
