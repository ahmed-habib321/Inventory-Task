package org.example.inventory.mapper;

import org.example.inventory.dto.CreateDealerRequest;
import org.example.inventory.dto.DealerResponse;
import org.example.inventory.dto.UpdateDealerRequest;
import org.example.inventory.entity.Dealer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;


@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface DealerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "vehicles", ignore = true)
    Dealer toEntity(CreateDealerRequest request);

    DealerResponse toResponse(Dealer dealer);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "vehicles", ignore = true)
    void updateEntity(UpdateDealerRequest request, @MappingTarget Dealer dealer);
}