package com.musala.calendar.services;

import java.util.List;

public interface BaseCRUDService<T> {

    List<T> findAll();

    T findById(Integer id);

    T create(T object);

    T update(Integer id, T object);

    void delete(Integer id);
}
