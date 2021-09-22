package com.devonfw.quarkus.productmanagement.domain.repo;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.querydsl.jpa.impl.JPAQuery;

public class GenericRepositoryImpl<E> implements GenericRepository<E> {

  @Inject
  EntityManager entityManager;

  @Override
  public JPAQuery<E> newJpaQuery() {

    return new JPAQuery<>(this.entityManager);
  }

  @Override
  public Query newNativeQuery(String query) {

    return this.entityManager.createNativeQuery(query);
  }

  @Override
  public Query newNativeQuery(String query, Class resultClass) {

    return this.entityManager.createNativeQuery(query, resultClass);
  }

}
