package kitchenpos.menu.ui;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;

@RequestMapping("/api/menus")
@RestController
public class MenuRestController {
	private final MenuService menuService;

	public MenuRestController(final MenuService menuService) {
		this.menuService = menuService;
	}

	@PostMapping
	public ResponseEntity<MenuResponse> create(@RequestBody final MenuRequest menuRequest) {
		final Menu created = menuService.create(menuRequest);
		final URI uri = URI.create("/api/menus/" + created.getId());
		return ResponseEntity.created(uri)
			.body(MenuResponse.from(created));
	}

	@GetMapping
	public ResponseEntity<List<MenuResponse>> list() {
		return ResponseEntity.ok()
			.body(MenuResponse.ofList(menuService.list()));
	}
}
