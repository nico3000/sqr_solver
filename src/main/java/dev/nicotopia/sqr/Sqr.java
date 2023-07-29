package dev.nicotopia.sqr;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Hello world!
 *
 */
public class Sqr {
    private final int arena[][];
    private final int rowSums[];
    private final int colSums[];

    public Sqr(List<Integer> values) {
        int size = (int) Math.sqrt((double) values.size());
        if (size * size != values.size()) {
            throw new RuntimeException("Number of values must be a square number.");
        }
        this.arena = new int[size][];
        this.rowSums = new int[size];
        this.colSums = new int[size];
        for (int r = 0; r < size; ++r) {
            final int tr = r;
            this.arena[r] = IntStream.range(0, this.getSize()).map(c -> values.get(tr * size + c)).toArray();
            this.rowSums[r] = Arrays.stream(this.arena[r]).sum();
        }
        for (int c = 0; c < size; ++c) {
            final int tc = c;
            this.colSums[c] = IntStream.range(0, this.getSize()).map(r -> this.arena[r][tc]).sum();
        }
    }

    public int getSize() {
        return this.arena.length;
    }

    public void add(int row, int col, int delta) {
        this.arena[row][col] += delta;
        this.rowSums[row] += delta;
        this.colSums[col] += delta;
    }

    public int getRowSum(int row) {
        return this.rowSums[row];
    }

    public int getColSum(int col) {
        return this.colSums[col];
    }

    public boolean isSolved() {
        for (int i = 0; i < this.getSize(); ++i) {
            if (this.rowSums[i] != this.colSums[i]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        String result = "";
        for (int r = 0; r < this.getSize(); ++r) {
            result += String.format("%4d |", this.rowSums[r])
                    + Arrays.stream(this.arena[r]).mapToObj(v -> String.format(" %4d", v))
                            .collect(StringBuffer::new, StringBuffer::append, StringBuffer::append).toString()
                    + "\n";
        }
        return result + "     |"
                + IntStream.range(0, this.getSize()).mapToObj(c -> String.format(" %4d", this.colSums[c]))
                        .collect(StringBuffer::new, StringBuffer::append, StringBuffer::append).toString();
    }
}
