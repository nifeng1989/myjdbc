package net.fengni.jdbc.query;

/**
 * Created by fengni on 2015/10/27.
 */
public class ColumnNotFoundException extends Exception {
    public ColumnNotFoundException() {
    }

    public ColumnNotFoundException(String message) {
        super(message);
    }
}
