package org.example.inventory.mapper;

import org.example.inventory.dto.CreateVehicleRequest;
import org.example.inventory.dto.UpdateVehicleRequest;
import org.example.inventory.dto.VehicleResponse;
import org.example.inventory.entity.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;


@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface VehicleMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "dealer", ignore = true)
    Vehicle toEntity(CreateVehicleRequest request);

    @Mapping(target = "dealerId", source = "dealer.id")
    VehicleResponse toResponse(Vehicle vehicle);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "dealer", ignore = true)
    void updateEntity(UpdateVehicleRequest request, @MappingTarget Vehicle vehicle);
}
