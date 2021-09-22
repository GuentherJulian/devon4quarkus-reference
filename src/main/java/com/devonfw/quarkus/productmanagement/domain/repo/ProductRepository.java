package com.devonfw.quarkus.productmanagement.domain.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.devonfw.quarkus.productmanagement.domain.model.ProductEntity;
import com.devonfw.quarkus.productmanagement.domain.model.QProductEntity;
import com.devonfw.quarkus.productmanagement.service.v1.model.ProductSearchCriteriaDto;
import com.querydsl.jpa.impl.JPAQuery;

public interface ProductRepository extends JpaRepository<ProductEntity, Long>, GenericRepository<ProductEntity> {

  @Query("select a from ProductEntity a where title = :title")
  ProductEntity findByTitle(@Param("title") String title);

  Page<ProductEntity> findAllByOrderByTitle();

  default Page<ProductEntity> findAllQueryDsl(ProductSearchCriteriaDto dto) {

    QProductEntity product = QProductEntity.productEntity;
    JPAQuery<ProductEntity> query = newJpaQuery();
    query.from(product);
    if (dto.getTitle() != null && !dto.getTitle().isEmpty()) {
      query.where(product.title.eq(dto.getTitle()));
    }

    if (dto.getPrice() != null) {
      query.where(product.price.eq(dto.getPrice()));
    } else if (dto.getPriceMin() != null || dto.getPriceMax() != null) {
      if (dto.getPriceMin() != null) {
        query.where(product.price.gt(dto.getPriceMin()));
      }
      if (dto.getPriceMax() != null) {
        query.where(product.price.lt(dto.getPriceMax()));
      }
    }

    // Order by title
    query.orderBy(product.title.desc());

    List<ProductEntity> products = query.limit(dto.getPageSize()).offset(dto.getPageNumber() * dto.getPageSize())
        .fetch();
    return new PageImpl<>(products, PageRequest.of(dto.getPageNumber(), dto.getPageSize()), products.size());
  }

  default Page<ProductEntity> findByTitleNativeQuery(ProductSearchCriteriaDto dto) {

    javax.persistence.Query query = newNativeQuery("select * from Product where title = :title", ProductEntity.class);
    query.setParameter("title", dto.getTitle());
    List<ProductEntity> products = query.getResultList();
    return new PageImpl<>(products, PageRequest.of(dto.getPageNumber(), dto.getPageSize()), products.size());
  }
}