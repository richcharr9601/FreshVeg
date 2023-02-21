package com.example.demo.service.imp;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.example.demo.repository.RepositoryWrapper;
import com.example.demo.service.contract.IService;

import jakarta.persistence.Id;

public class EntityService<TEntity, TKey> implements IService<TEntity, TKey> {

    private Class<TEntity> entityType;

    @Autowired
    private RepositoryWrapper repositoryWrapper;

    public EntityService(Class<TEntity> entityType) {
        this.entityType = entityType;
    }

    @Override
    public List<TEntity> findAll() {
        return repositoryWrapper.<TEntity, TKey>repository(entityType).findAll();
    }

    @Override
    public Optional<TEntity> findByID(TKey key) {
        return repositoryWrapper.<TEntity, TKey>repository(entityType).findById(key);
    }

    @Override
    public boolean existsByID(TKey key) {
        return repositoryWrapper.<TEntity, TKey>repository(entityType).existsById(key);
    }

    @Override
    public TEntity create(TEntity entity) {
        return repositoryWrapper.<TEntity, TKey>repository(entityType).save(entity);

    }

    @Override
    public Optional<TEntity> update(TKey key, TEntity entity) {
        boolean existsById = repositoryWrapper.<TEntity, TKey>repository(entityType).existsById(key);

        if (!existsById)
            return null;

        Reflections r = new Reflections(entityType);
        List<Field> IdFields = List.copyOf(r.getFieldsAnnotatedWith(Id.class));
        try {
            IdFields.get(0).set(entity, key);
            return Optional.of(repositoryWrapper.<TEntity, TKey>repository(entityType).save(entity));
        } catch (IllegalArgumentException | IllegalAccessException e) {
            System.out.println("update fail: " + e.getMessage());
            return Optional.empty();
        }

    }

    @Override
    public void delete(TKey key, TEntity entity) {
        repositoryWrapper.<TEntity, TKey>repository(entityType).delete(entity);
    }
}
