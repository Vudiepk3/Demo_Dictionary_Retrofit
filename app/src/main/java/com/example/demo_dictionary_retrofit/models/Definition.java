package com.example.demo_dictionary_retrofit.models;


// Lớp đại diện cho định nghĩa của từ
public class Definition {
    private String definition;

    // Constructor
    public Definition(String definition) {
        this.definition = definition;
    }

    // Getter và Setter
    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }
}
