package controllers;

import pages.Page;

import java.util.Set;

public interface IPDFParser {
    public Set<Page> run(String path);
}
