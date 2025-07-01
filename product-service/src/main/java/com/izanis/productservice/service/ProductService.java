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
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

// TODO(i-zanis): need to use DTO in the service layer

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {
  private final ProductRepository productRepository;
  private final ProductMapper productMapper;

  private static void validateIdForProductUpdate(UUID id, Product product) {
    Validate.notNull(id, "Product ID cannot be null");
    Validate.notNull(product, "Product cannot be null");
    Validate.isTrue(
        id.equals(product.getId()), "ID {} must match the product ID {}", id, product.getId());
  }

  @Cacheable(cacheNames = "productListCache", condition = "!#showAvailableInventory")
  public ProductPagedList findProducts(
      String name, Product.Category category, Pageable pageable, Boolean showAvailableInventory) {
    Page<Product> page = queryProducts(name, category, pageable);

    return new ProductPagedList(
        page.getContent().stream().map(p -> mapToProduct(p, showAvailableInventory)).toList(),
        PageRequest.of(page.getPageable().getPageNumber(), page.getPageable().getPageSize()),
        page.getTotalElements());
  }

  private ProductDto mapToProduct(Product product, Boolean showAvailableInventory) {
    return Boolean.TRUE.equals(showAvailableInventory)
        ? productMapper.toProductDtoWithInventory(product)
        : productMapper.toProductDto(product);
  }

  private Page<Product> queryProducts(String name, Product.Category category, Pageable pageable) {
    if (StringUtils.hasText(name) && category != null) {
      return productRepository.findAllByNameAndCategory(name, category, pageable);
    } else if (StringUtils.hasText(name)) {
      return productRepository.findAllByName(name, pageable);
    } else if (category != null) {
      return productRepository.findAllByCategory(category, pageable);
    } else {
      return productRepository.findAll(pageable);
    }
  }

  public List<Product> findAll() {
    return productRepository.findAll();
  }

  @Cacheable(cacheNames = "productCache", key = "#id", condition = "!#showAvailableInventory")
  public ProductDto findById(@NonNull UUID id) {
    return findById(id, null);
  }

  @Cacheable(cacheNames = "productCache", key = "#id", condition = "!#showAvailableInventory")
  public ProductDto findById(@NonNull UUID id, Boolean showAvailableInventory) {
    Product productDto = productRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    return mapToProduct(productDto, showAvailableInventory);
  }

  public Product save(@NonNull Product product) {
    return productRepository.save(product);
  }

  public ProductDto update(UUID id, Product product) {
    validateIdForProductUpdate(id, product);
    return productMapper.toProductDto(productRepository.save(product));
  }

  public void deleteById(UUID id) {
    Validate.notNull(id, "Product ID cannot be null");
    productRepository.deleteById(id);
  }

  @Cacheable(cacheNames = "productUpcCache")
  public Optional<Product> findByUpc(String upc) {
    // TODO(i-zanis): check upc annotation @ValidUpc
    Validate.notNull(upc, "UPC cannot be null");
    return productRepository.findByUpc(upc);
  }

  public List<Product> findByInventoryLessThan(@NonNull Integer inventory) {
    Validate.notNull(inventory, "Inventory cannot be null");
    Validate.isTrue(inventory >= 0, "Inventory cannot be negative");
    return productRepository.findByTotalInventoryLessThan(inventory);
  }

  public long countByUpcIn(Collection<String> upcs) {
    if (CollectionUtils.isEmpty(upcs)) return 0;
    List<String> nonNullUpcs = upcs.stream().filter(Objects::nonNull).toList();
    return productRepository.countByUpcIn(nonNullUpcs);
  }
}
