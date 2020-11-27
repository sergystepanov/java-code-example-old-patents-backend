package ru.ineureka.patents.reader;

/**
 * Interface for reading various data sources of the type {@code T}.
 */
public interface Reader<T> {
    DataTable read(T in);
}
