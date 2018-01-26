package com.software.dzungvu.model;

import java.lang.annotation.ElementType;
import java.util.List;

public class item {
    private List<NewsElements> elementsList;

    public item(List<NewsElements> elementsList) {
        this.elementsList = elementsList;
    }

    public item() {
    }

    public List<NewsElements> getElementsList() {
        return elementsList;
    }

    public void setElementsList(List<NewsElements> elementsList) {
        this.elementsList = elementsList;
    }
}
