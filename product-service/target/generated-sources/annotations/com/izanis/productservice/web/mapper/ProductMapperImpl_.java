package com.izanis.productservice.web.mapper;

import com.izanis.model.ProductDto;
import com.izanis.productservice.entity.Product;
import java.math.BigDecimal;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-12-03T18:13:53+0000",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 18.0.2 (Oracle Corporation)"
)
@Component
@Qualifier("delegate")
public class ProductMapperImpl_ implements ProductMapper {

    @Override
    public Product toProduct(ProductDto productDto) {
        if ( productDto == null ) {
            return null;
        }

        Product product = new Product();

        return product;
    }

    @Override
    public ProductDto toProductDtoWithInventory(Product product) {
        if ( product == null ) {
            return null;
        }

        UUID id = null;
        String name = null;
        BigDecimal price = null;
        Long upc = null;
        Integer totalInventory = null;
        Product.Category category = null;
        Integer availableInventory = null;

        ProductDto productDto = new ProductDto( id, name, price, upc, totalInventory, category, availableInventory );

        return productDto;
    }

    @Override
    public ProductDto toProductDto(Product product) {
        if ( product == null ) {
            return null;
        }

        UUID id = null;
        String name = null;
        BigDecimal price = null;
        Long upc = null;
        Integer totalInventory = null;
        Product.Category category = null;
        Integer availableInventory = null;

        ProductDto productDto = new ProductDto( id, name, price, upc, totalInventory, category, availableInventory );

        return productDto;
    }
}
