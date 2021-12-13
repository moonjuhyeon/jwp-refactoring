package kitchenpos.domain;

import static kitchenpos.domain.TableAcceptanceStaticTest.*;

import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;

@DisplayName("인수테스트 : 테이블 관련")
class TableAcceptanceTest extends AcceptanceTest {

	@Test
	void 빈_테이블을_생성한다() {
		// given
		OrderTable 빈_테이블_생성_요청값 = 테이블_요청값_생성(0, true);

		// when
		ExtractableResponse<Response> response = 테이블_생성_요청(빈_테이블_생성_요청값);

		// then
		테이블_생성됨(response);
	}

	@Test
	void 테이블_목록을_조회한다() {
		// given
		OrderTable 생성된_테이블 = 테이블이_생성_되어있음(테이블_요청값_생성(0, true));

		// when
		ExtractableResponse<Response> response = 테이블_목록을_조회함();

		// then
		테이블_목록이_조회됨(response, Collections.singletonList(생성된_테이블.getId()));
	}

}