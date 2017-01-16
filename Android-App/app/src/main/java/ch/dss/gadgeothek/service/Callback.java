package ch.dss.gadgeothek.service;

public interface Callback<T> {
    void onCompletion(T input);
    void onError(String message);
}
