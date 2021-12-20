package kitchenpos.tableGroup.ui;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.tableGroup.application.TableGroupService;
import kitchenpos.tableGroup.domain.TableGroup;
import kitchenpos.tableGroup.dto.TableGroupRequest;
import kitchenpos.tableGroup.dto.TableGroupResponse;

@RequestMapping("/api/table-groups")
@RestController
public class TableGroupRestController {
	private final TableGroupService tableGroupService;

	public TableGroupRestController(final TableGroupService tableGroupService) {
		this.tableGroupService = tableGroupService;
	}

	@PostMapping
	public ResponseEntity<TableGroupResponse> create(@RequestBody final TableGroupRequest tableGroupRequest) {
		final TableGroup created = tableGroupService.create(tableGroupRequest);
		final URI uri = URI.create("/api/table-groups/" + created.getId());
		return ResponseEntity.created(uri)
			.body(TableGroupResponse.from(created));
	}

	@DeleteMapping("/{tableGroupId}")
	public ResponseEntity<Void> ungroup(@PathVariable final Long tableGroupId) {
		tableGroupService.ungroup(tableGroupId);
		return ResponseEntity.noContent()
			.build();
	}
}
