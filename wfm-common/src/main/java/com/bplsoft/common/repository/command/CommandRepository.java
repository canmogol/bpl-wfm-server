package com.bplsoft.common.repository.command;


import com.bplsoft.common.model.BaseModel;
import com.bplsoft.common.model.Model;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

public abstract class CommandRepository<T extends Model<PK>, PK extends Serializable> implements CRepository<T, PK> {

    private Class<T> entityClass;

    @Inject
    private EntityManagerFactory entityManagerFactory;

    protected EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    @SuppressWarnings("unchecked")
    public CommandRepository() {
        entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public void create(T t) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(t);
        entityManager.flush();
        entityManager.getTransaction().commit();
    }

    @Override
    public void update(T t) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge(t);
        entityManager.flush();
        entityManager.getTransaction().commit();
    }

    @Override
    public void delete(T t) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        if (!entityManager.contains(t)) {
            t = entityManager.merge(t);
        }
        if (t instanceof BaseModel) {
            ((BaseModel) t).setDeleted(Boolean.TRUE);
            entityManager.merge(t);
        } else {
            entityManager.remove(t);
        }
        entityManager.flush();
        entityManager.getTransaction().commit();
    }

    @Override
    public void delete(PK id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        T t = entityManager.getReference(entityClass, id);
        if (t instanceof BaseModel) {
            ((BaseModel) t).setDeleted(Boolean.TRUE);
            entityManager.merge(t);
        } else {
            entityManager.remove(t);
        }
        entityManager.flush();
        entityManager.getTransaction().commit();

    }

}
