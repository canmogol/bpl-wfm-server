package com.bplsoft.common.repository;

import com.bplsoft.common.property.Property;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerFactoryProducer {

    @Inject
    @Property("persistence.unit")
    private String persistenceUnit;

    private static EntityManagerFactory entityManagerFactory;

    @Produces
    public EntityManagerFactory produceEntityManager() {
        if (entityManagerFactory == null) {
            entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnit);
        }
        return entityManagerFactory;
    }

}
