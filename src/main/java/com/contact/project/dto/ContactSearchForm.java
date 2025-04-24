package com.contact.project.dto;

public class ContactSearchForm {

    private String searchField;

    private String searchKeyword ;

    public String getSearchField() {
        return searchField;
    }

    public void setSearchField(String searchField) {
        this.searchField = searchField;
    }

    public String getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }

    @Override
    public String toString() {
        return "ContactSearchForm [searchField=" + searchField + ", searchKeyword=" + searchKeyword
                + "]";
    }



}
