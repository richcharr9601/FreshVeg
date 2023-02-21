package com.example.demo.service.contract;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public interface IService<TEntity, TKey> {
    List<TEntity> findAll();

    Optional<TEntity> findByID(TKey key);

    boolean existsByID(TKey key);

    TEntity create(TEntity entity);

    Optional<TEntity> update(TKey key, TEntity entity);

    void delete(TKey key, TEntity entity);
}
