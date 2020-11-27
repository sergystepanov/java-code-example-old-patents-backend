package ru.ineureka.patents.dto.type;

public enum PropertyTypeName {
    P("p"),
    U("u"),
    D("d"),
    TM("tm");

    private final String text;

    PropertyTypeName(String text) {
        this.text = text;
    }

    public boolean is(String that) {
        return text.equalsIgnoreCase(that);
    }

    @Override
    public String toString() {
        return text;
    }
}
