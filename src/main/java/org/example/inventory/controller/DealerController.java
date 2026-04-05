package org.example.inventory.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.example.inventory.dto.CreateDealerRequest;
import org.example.inventory.dto.DealerResponse;
import org.example.inventory.dto.PagedResponse;
import org.example.inventory.dto.UpdateDealerRequest;
import org.example.inventory.service.DealerService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/dealers")
@RequiredArgsConstructor
@Tag(name = "Dealers", description = "Manage dealers within your tenant")
public class DealerController {

    private final DealerService dealerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new dealer")
    public DealerResponse create(@Valid @RequestBody CreateDealerRequest request) {
        return dealerService.create(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get dealer by id")
    public DealerResponse findById(@PathVariable UUID id) {
        return dealerService.findById(id);
    }

    @GetMapping
    @Operation(summary = "List dealers (paginated and sorted)")
    public PagedResponse<DealerResponse> findAll(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Max(100) @Min(1) int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        return dealerService.findAll(PageRequest.of(page, size, sort));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Partially update a dealer")
    public DealerResponse update(@PathVariable UUID id,
                                 @Valid @RequestBody UpdateDealerRequest request) {
        return dealerService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a dealer")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        dealerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
