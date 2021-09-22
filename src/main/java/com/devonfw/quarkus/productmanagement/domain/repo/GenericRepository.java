package com.devonfw.quarkus.productmanagement.domain.repo;

import javax.persistence.Query;

import com.querydsl.jpa.impl.JPAQuery;

public interface GenericRepository<E> {
  JPAQuery<E> newJpaQuery();

  Query newNativeQuery(String query);

  Query newNativeQuery(String query, Class resultClass);
}
