package api;

import api.exceptions.LoaderException;

import java.util.Map;

public interface Loader {
    Map<String, String> load() throws LoaderException;
    void save(Map<String, String> accounts) throws LoaderException;
}
