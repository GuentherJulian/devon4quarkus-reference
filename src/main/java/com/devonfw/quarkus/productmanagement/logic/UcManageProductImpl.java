package com.devonfw.quarkus.productmanagement.logic;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;

import com.devonfw.quarkus.general.service.exception.InvalidParameterException;
import com.devonfw.quarkus.productmanagement.domain.model.ProductEntity;
import com.devonfw.quarkus.productmanagement.domain.repo.ProductRepository;
import com.devonfw.quarkus.productmanagement.service.v1.mapper.ProductMapper;
import com.devonfw.quarkus.productmanagement.service.v1.model.NewProductDto;
import com.devonfw.quarkus.productmanagement.service.v1.model.ProductDto;

@Named
@Transactional
public class UcManageProductImpl implements UcManageProduct {
  @Inject
  ProductRepository productRepository;

  @Inject
  ProductMapper mapper;

  @Override
  public ProductDto saveProduct(NewProductDto dto) {

    ProductEntity created = this.productRepository.save(this.mapper.create(dto));
    return this.mapper.map(created);
  }

  @Override
  public void deleteProduct(String id) {

    if (!StringUtils.isNumeric(id)) {
      throw new InvalidParameterException("Unable to parse ID: " + id);
    }

    this.productRepository.deleteById(Long.valueOf(id));
  }
}
