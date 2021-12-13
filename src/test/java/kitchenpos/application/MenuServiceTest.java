package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

@DisplayName("메뉴 : 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

	@Mock
	MenuDao menuDao;

	@Mock
	MenuGroupDao menuGroupDao;

	@Mock
	MenuProductDao menuProductDao;

	@Mock
	ProductDao productDao;

	@Mock
	MenuProduct menuProduct;

	@Mock
	MenuGroup menuGroup;

	@Mock
	Product product;

	@InjectMocks
	private MenuService menuService;

	private Menu menu;

	@BeforeEach
	void setup() {
		menu = new Menu();
	}

	@DisplayName("메뉴 생성 테스트")
	@Test
	void createMenu() {
		// given
		given(menuProduct.getProductId()).willReturn(1L);
		given(menuProduct.getQuantity()).willReturn(2L);
		given(menuGroup.getId()).willReturn(1L);
		given(product.getPrice()).willReturn(BigDecimal.valueOf(16000));
		given(menuGroupDao.existsById(menuGroup.getId())).willReturn(true);
		given(productDao.findById(menuProduct.getProductId())).willReturn(Optional.of(product));
		given(menuProductDao.save(menuProduct)).willReturn(menuProduct);

		menu.setName("불닭메뉴");
		menu.setMenuGroupId(menuGroup.getId());
		menu.setMenuProducts(Collections.singletonList(menuProduct));
		menu.setPrice(new BigDecimal(21000));

		// when
		when(menuDao.save(menu)).thenReturn(menu);

		// then
		assertThat(menuService.create(menu)).isEqualTo(menu);
	}

	@DisplayName("메뉴의 가격이 존재하지 않을 경우 생성시 예외처리 테스트")
	@Test
	void createMenuNullPrice() {
		// given
		menu.setName("불닭메뉴");

		// when // then
		assertThatThrownBy(() -> {
			menuService.create(menu);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("메뉴의 가격이 0이하일 경우 생성시 예외처리 테스트")
	@Test
	void createUnderZeroPrice() {
		// given
		menu.setName("불닭메뉴");
		menu.setPrice(BigDecimal.valueOf(-1000));

		// when // then
		assertThatThrownBy(() -> {
			menuService.create(menu);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("메뉴에 등록할 메뉴 그룹이 존재하지 않을 경우 생성시 예외처리 테스트")
	@Test
	void createMenuUnknownMenuGroup() {
		// given
		given(menuGroup.getId()).willReturn(1L);
		given(menuGroupDao.existsById(menuGroup.getId())).willReturn(false);
		menu.setName("불닭메뉴");
		menu.setPrice(BigDecimal.valueOf(16000));
		menu.setMenuGroupId(menuGroup.getId());

		// when // then
		assertThatThrownBy(() -> {
			menuService.create(menu);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("메뉴에 등록할 상품이 존재하지 않을 경우 생성시 예외처리 테스트")
	@Test
	void createMenuUnknownProduct() {
		// given
		given(menuGroup.getId()).willReturn(1L);
		given(menuGroupDao.existsById(menuGroup.getId())).willReturn(true);

		menu.setName("불닭메뉴");
		menu.setMenuGroupId(menuGroup.getId());
		menu.setPrice(BigDecimal.valueOf(16000));
		menu.setMenuProducts(Collections.singletonList(menuProduct));

		// when // then
		assertThatThrownBy(() -> {
			menuService.create(menu);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("메뉴의 가격이 메뉴 상품들의 가격을 모두 더한 값보다 클 경우 생성시 예외처리 테스트")
	@Test
	void createMenuPriceUnderSumPrice() {
		// given
		given(menuProduct.getProductId()).willReturn(1L);
		given(menuProduct.getQuantity()).willReturn(2L);
		given(menuGroup.getId()).willReturn(1L);
		given(product.getPrice()).willReturn(BigDecimal.valueOf(16000));
		given(menuGroupDao.existsById(menuGroup.getId())).willReturn(true);
		given(productDao.findById(menuProduct.getProductId())).willReturn(Optional.of(product));

		menu.setName("불닭메뉴");
		menu.setMenuGroupId(menuGroup.getId());
		menu.setPrice(BigDecimal.valueOf(50000));
		menu.setMenuProducts(Collections.singletonList(menuProduct));

		// when // then
		assertThatThrownBy(() -> {
			menuService.create(menu);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("메뉴 목록 조회 테스트")
	@Test
	void getList() {
		// given // when
		when(menuDao.findAll()).thenReturn(Collections.singletonList(menu));

		// then
		assertThat(menuService.list()).containsExactly(menu);
	}
}
