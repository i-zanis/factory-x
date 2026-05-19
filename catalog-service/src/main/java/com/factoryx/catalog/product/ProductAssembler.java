package com.factoryx.catalog.product;

public final class ProductAssembler {

    private ProductAssembler() {
    }

    public static com.factoryx.catalog.model.Product toDto(Product entity) {
        if (entity == null) {
            return null;
        }
        com.factoryx.catalog.model.Product dto = new com.factoryx.catalog.model.Product();
        dto.setId(entity.getId().value());
        dto.setSku(entity.getSku().value());
        dto.setName(entity.getName());
        dto.setPrice(entity.getPrice().doubleValue());
        return dto;
    }
}
