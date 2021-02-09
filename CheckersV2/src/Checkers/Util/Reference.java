package Checkers.Util;

public class Reference <T> {
    private T value;

    public Reference(T v) {
        value = v;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T v) {
        value = v;
    }
}
