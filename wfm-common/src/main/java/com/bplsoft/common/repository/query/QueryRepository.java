package com.bplsoft.common.repository.query;


import com.bplsoft.common.model.BaseModel;
import com.bplsoft.common.model.Model;
import com.bplsoft.common.property.Property;
import com.bplsoft.common.repository.builder.QueryBuilder;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class QueryRepository<T extends Model<PK>, PK extends Serializable> implements QRepository<T, PK> {

    @Inject
    @Property("persistence.unit")
    private String persistenceUnit;

    @Inject
    private EntityManagerFactory entityManagerFactory;

    protected EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    private Class<T> entityClass;

    @SuppressWarnings("unchecked")
    public QueryRepository() {
        entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public T findById(PK id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        T t = entityManager.find(entityClass, id);
        if (t != null && t instanceof BaseModel && ((BaseModel) t).isDeleted()) {
            // this entity is marked as deleted, return null
            return null;
        }
        return t;
    }

    @Override
    public List<T> findAll() {
        // What kind of a method is this "findAll" method? shouldn't it has some sort of a limit?
        // Hibernate implementation also thinks in the same way:
        // org.hibernate.ejb.AbstractQueryImpl   getMaxResults(){
        //      return maxResults == -1
        //          ? Integer.MAX_VALUE // stupid spec... MAX_VALUE??
        //          : maxResults;
        // }
        return findAll(0, Integer.MAX_VALUE);
    }

    @Override
    public List<T> findAll(Integer index, Integer numberOfRecords) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        Root<T> root = criteriaQuery.from(entityClass);
        if (BaseModel.class.isAssignableFrom(entityClass)) {
            criteriaQuery.where(criteriaBuilder.equal(root.get("deleted"), Boolean.FALSE));
        }
        criteriaQuery.select(root);
        List<T> list = entityManager.createQuery(criteriaQuery).setFirstResult(index).setMaxResults(numberOfRecords)
            .getResultList();
        return list;
    }

    public QueryBuilder<T> query() {
        return new QueryBuilder<>(entityClass, entityManagerFactory);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Long count() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<T> root = criteriaQuery.from(entityClass);
        if (BaseModel.class.isAssignableFrom(entityClass)) {
            criteriaQuery.where(criteriaBuilder.equal(root.get("deleted"), Boolean.FALSE));
        }
        criteriaQuery.select(criteriaBuilder.count(root));
        TypedQuery<Long> query = entityManager.createQuery(criteriaQuery);
        Long count = query.getSingleResult();
        return count;
    }

}
