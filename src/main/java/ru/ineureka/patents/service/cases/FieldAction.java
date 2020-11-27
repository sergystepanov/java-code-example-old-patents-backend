package ru.ineureka.patents.service.cases;

/**
 * Interface for specifying what to do with the field.
 */
public interface FieldAction<T> {
    T action(T value);
}
