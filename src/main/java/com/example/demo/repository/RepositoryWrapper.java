package com.example.demo.repository;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.example.demo.repository.entity.CategoryRepository;
import com.example.demo.repository.entity.CommentRepository;
import com.example.demo.repository.entity.FavoriteRepository;
import com.example.demo.repository.entity.OrderDetailRepository;
import com.example.demo.repository.entity.OrderRepository;
import com.example.demo.repository.entity.RoleRepository;
import com.example.demo.repository.entity.UserRepository;

import jakarta.persistence.Entity;
import lombok.Getter;

@Getter
@Component
public class RepositoryWrapper {
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    FavoriteRepository favoriteRepository;

    @Autowired
    OrderDetailRepository orderDetailRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    public <TEntity, TKey> JpaRepository<TEntity, TKey> repository(Class<?> type) {
        if (!checkEntity(type))
            throw new IllegalArgumentException("Object is not @Entity");

        List<Field> declaredFields = List.of(this.getClass().getDeclaredFields());

        List<Field> filter = declaredFields
                .stream()
                .filter(field -> {
                    return checkEntityRepository(field, type);
                }).toList();

        if (!filter.isEmpty()) {
            try {
                JpaRepository<TEntity, TKey> repository = (JpaRepository<TEntity, TKey>) filter.get(0).get(this);
                return repository;
            } catch (IllegalArgumentException | IllegalAccessException e) {
                System.out.println("Can not get repository");
                return null;
            }
        }
        return null;
    }

    private boolean checkEntity(Class<?> eType) {
        List<Annotation> filter = List.of(eType.getAnnotations()).stream()
                .filter(a -> {
                    Class<? extends Annotation> annotationType = a.annotationType();
                    return annotationType == Entity.class;
                })
                .toList();
        return !filter.isEmpty();
    }

    private boolean checkEntityRepository(Field field, Class<?> type) {
        boolean result = checkExtendsJpaRepository(field.getType()) && checkTypeParameter(field.getType(), type);
        return result;
    }

    private boolean checkExtendsJpaRepository(Class<?> Rtype) {
        Class<?>[] interfaces = Rtype.getInterfaces();
        return List.of(interfaces).stream()
                .filter(i -> {
                    boolean result = i == JpaRepository.class;
                    return result;
                })
                .findFirst()
                .isPresent();
    }

    private boolean checkTypeParameter(Class<?> Rtype, Class<?> EType) {
        List<Type> filter = List.of(Rtype.getGenericInterfaces())
                .stream()
                .filter(t -> {
                    ParameterizedType pType = (ParameterizedType) t;
                    Type rawType = pType.getRawType();
                    Type[] actualTypeArguments = pType.getActualTypeArguments();

                    if (rawType == JpaRepository.class) {
                        Type entityType = actualTypeArguments[0];
                        return entityType == EType;
                    }
                    return false;
                })
                .toList();
        return !filter.isEmpty();
    }
}
