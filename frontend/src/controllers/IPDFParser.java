package controllers;

import pages.Page;

import java.util.Set;
import java.util.function.Consumer;

public interface IPDFParser {
    public Set<Page> run(String path, Consumer<String> setLoadingText);
}
