package org.example.inventory.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.example.inventory.dto.*;
import org.example.inventory.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/vehicles")
@RequiredArgsConstructor
@Tag(name = "Vehicles", description = "Manage vehicles within your tenant")
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new vehicle")
    public VehicleResponse create(@Valid @RequestBody CreateVehicleRequest request) {
        return vehicleService.create(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get vehicle by id")
    public VehicleResponse findById(@PathVariable UUID id) {
        return vehicleService.findById(id);
    }

    @GetMapping
    @Operation(summary = "List vehicles with optional filters and pagination")
    public PagedResponse<VehicleResponse> findAll(
            @Valid @ModelAttribute VehicleFilterRequest filter,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") @Max(100) @Min(1) int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "model") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        return vehicleService.findAll(filter, PageRequest.of(page, size, sort));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Partially update a vehicle")
    public VehicleResponse update(@PathVariable UUID id,
                                  @Valid @RequestBody UpdateVehicleRequest request) {
        return vehicleService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a vehicle")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        vehicleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
