package me.dave.itemcategories.util;

public record SimplePlaceholder(String placeholder, String content) {

    public String parse(String string) {
        return string.replace(placeholder, content);
    }
}
