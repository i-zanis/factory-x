package com.izanis.productservice.service;

import com.izanis.model.ProductDto;
import com.izanis.model.ProductPagedList;
import com.izanis.productservice.entity.Product;
import com.izanis.productservice.repository.ProductRepository;
import com.izanis.productservice.web.mapper.ProductMapper;
import jakarta.persistence.EntityNotFoundException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

// TODO(i-zanis): need to use DTO in the service layer

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
  private final ProductRepository productRepository;
  private final ProductMapper productMapper;

  private static void validateIdForProductUpdate(UUID id, Product product) {
    Validate.notNull(id, "Product ID cannot be null");
    Validate.notNull(product, "Product cannot be null");
    validateIdsMatch(id, product);
  }

  private static void validateIdsMatch(UUID id, Product product) {
    Validate.isTrue(
        id.equals(product.getId()), "ID {} must match the product ID {}", id, product.getId());
  }

  @Cacheable(cacheNames = "productListCache", condition = "!#showAvailableInventory")
  @Override
  public ProductPagedList findProducts(
      String name, Product.Category category, Pageable pageable, Boolean showAvailableStock) {
    Page<Product> page;

    if (StringUtils.hasText(name) && category != null) {
      page = productRepository.findAllByNameAndCategory(name, category, pageable);
    } else if (StringUtils.hasText(name)) {
      page = productRepository.findAllByName(name, pageable);
    } else if (category != null) {
      page = productRepository.findAllByCategory(category, pageable);
    } else {
      page = productRepository.findAll(pageable);
    }

    return new ProductPagedList(
        page.getContent().stream()
            .map(
                showAvailableStock
                    ? productMapper::toProductDtoWithInventory
                    : productMapper::toProductDto)
            .toList(),
        PageRequest.of(page.getPageable().getPageNumber(), page.getPageable().getPageSize()),
        page.getTotalElements());
  }

  @Override
  public List<Product> findAll() {
    return productRepository.findAll();
  }

  @Override
  @Cacheable(cacheNames = "productCache", key = "#id", condition = "!#val")
  public ProductDto findById(UUID id, Boolean val) {
    Validate.notNull(id, "Product ID cannot be null");
    Product productDto = productRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    if (Boolean.TRUE.equals(val)) return productMapper.toProductDtoWithInventory(productDto);
    return productMapper.toProductDto(productDto);
  }

  @Override
  @NonNull
  public Product save(Product product) {
    Validate.notNull(product, "Product cannot be null");
    return productRepository.save(product);
  }

  @Override
  public void update(@NonNull UUID id, @NonNull Product product) {
    validateIdForProductUpdate(id, product);
    productRepository.save(product);
  }

  @Override
  public void deleteById(UUID id) {
    Validate.notNull(id, "Product ID cannot be null");
    productRepository.deleteById(id);
  }

  @Override
  public Optional<Product> findByUpc(String upc) {
    // TODO(i-zanis): check upc annotation @ValidUpc
    Validate.notNull(upc, "UPC cannot be null");
    return productRepository.findByUpc(upc);
  }

  @Override
  public List<Product> findByInventoryLessThan(Integer inventory) {
    Validate.notNull(inventory, "Inventory cannot be null");
    Validate.isTrue(inventory >= 0, "Inventory cannot be negative");
    return productRepository.findByTotalInventoryLessThan(inventory);
  }

  @Override
  public long countByUpcIn(Collection<String> upcs) {
    if (CollectionUtils.isEmpty(upcs)) return 0;
    List<String> nonNullUpcs = upcs.stream().filter(Objects::nonNull).toList();
    return productRepository.countByUpcIn(nonNullUpcs);
  }
}
