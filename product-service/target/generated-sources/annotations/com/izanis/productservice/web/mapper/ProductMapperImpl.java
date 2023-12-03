package com.izanis.productservice.web.mapper;

import com.izanis.model.ProductDto;
import com.izanis.productservice.entity.Product;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-12-03T18:13:53+0000",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 18.0.2 (Oracle Corporation)"
)
@Component
@Primary
public class ProductMapperImpl extends ProductMapperDecorator {

    @Autowired
    @Qualifier("delegate")
    private ProductMapper delegate;

    @Override
    public Product toProduct(ProductDto productDto)  {
        return delegate.toProduct( productDto );
    }

    @Override
    public ProductDto toProductDto(Product product)  {
        return delegate.toProductDto( product );
    }
}
