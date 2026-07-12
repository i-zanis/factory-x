package com.factoryx.catalog.product;

public final class ProductAssembler {

    private ProductAssembler() {
    }

    public static com.factoryx.catalog.model.Product toDto(Product entity) {
        com.factoryx.catalog.model.Product dto = new com.factoryx.catalog.model.Product();
        dto.setId(entity.getId().value());
        dto.setSku(entity.getSku().value());
        dto.setName(entity.getName().value());
        dto.setPrice(entity.getPrice().amount().doubleValue());
        return dto;
    }

    public static com.factoryx.catalog.model.Product toDto(ProductProjection projection) {
        com.factoryx.catalog.model.Product dto = new com.factoryx.catalog.model.Product();
        dto.setId(projection.getId());
        dto.setSku(projection.getSku().value());
        dto.setName(projection.getName());
        dto.setPrice(projection.getPrice().amount().doubleValue());
        return dto;
    }
}
