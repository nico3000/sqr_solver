package dev.nicotopia.sqr;

import java.util.OptionalInt;

public class FreeValue {
    private final int value;
    private OptionalInt row = OptionalInt.empty();
    private OptionalInt col = OptionalInt.empty();

    public FreeValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public OptionalInt getRow() {
        return this.row;
    }

    public OptionalInt getCol() {
        return this.col;
    }

    public void setRow(int row) {
        this.row = OptionalInt.of(row);
    }

    public void setCol(int col) {
        this.col = OptionalInt.of(col);
    }

    public void resetRow() {
        this.row = OptionalInt.empty();
    }

    public void resetCol() {
        this.col = OptionalInt.empty();
    }

    public boolean applyIfPossible(Sqr sqr) {
        if (this.row.isPresent() && this.col.isPresent()) {
            sqr.add(this.row.getAsInt(), this.col.getAsInt(), this.value);
            return true;
        }
        return false;
    }

    public void revertIfPossible(Sqr sqr) {
        if (this.row.isPresent() && this.col.isPresent()) {
            sqr.add(this.row.getAsInt(), this.col.getAsInt(), -this.value);
        }
    }

    @Override
    public String toString() {
        return Integer.toString(this.value);
    }
}
