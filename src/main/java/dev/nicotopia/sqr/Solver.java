package dev.nicotopia.sqr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

public class Solver {
    public static void main(String[] args) {
        Sqr sqr = null;
        List<Integer> values = new LinkedList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.print("Row #0: ");
            var input = new LinkedList<>(Arrays.stream(br.readLine().split("\s+")).map(Integer::valueOf).toList());
            int size = input.size();
            for (int i = 1; i < size; ++i) {
                try {
                    List<Integer> row;
                    do {
                        System.out.printf("Row #%d: ", i);
                        row = Arrays.stream(br.readLine().split("\s+")).map(Integer::valueOf).toList();
                    } while (row.size() != size);
                    input.addAll(row);
                } catch (NumberFormatException ex) {
                    System.err.println(ex.getMessage());
                    --i;
                }
            }
            sqr = new Sqr(input);
            System.out.print("Values: ");
            values = Arrays.stream(br.readLine().split("\s+")).map(Integer::valueOf).toList();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (sqr != null) {
            System.out.println("Original:\n" + sqr + "\n");
            if (new Solver().solve(sqr, values)) {
                System.out.println("\nFinal state:\n" + sqr);
            } else {
                System.out.println("Not solvable");
            }
        }
    }

    public boolean solve(Sqr sqr, List<Integer> values) {
        var freeValues = new ArrayList<>(values.stream().map(FreeValue::new).toList());
        if (!this.solve(sqr, 0, freeValues)) {
            return false;
        }
        for (var v : freeValues) {
            if (v.getRow().isPresent() && v.getCol().isPresent()) {
                System.out.printf("Add %d to cell (%d,%d).\n", v.getValue(), v.getRow().getAsInt(),
                        v.getCol().getAsInt());
            } else {
                System.out.printf("Add %d to any cell on the diagonal.\n", v.getValue());
            }
        }
        return true;
    }

    private boolean solve(Sqr sqr, int n, List<FreeValue> freeValues) {
        if (n == sqr.getSize() || freeValues.isEmpty()) {
            return sqr.isSolved();
        }
        final int diff = sqr.getRowSum(n) - sqr.getColSum(n);
        Predicate<PartitionedSuperSet<FreeValue>> accept = p -> p.getPartition(2).mapToInt(FreeValue::getValue).sum()
                - p.getPartition(1).mapToInt(FreeValue::getValue).sum() == diff;
        return Partitioner.stream(freeValues, 3).parallel().filter(accept).sequential()
                .anyMatch(p -> this.checkPartitioning(sqr, n, p));
    }

    private boolean checkPartitioning(Sqr sqr, int n, PartitionedSuperSet<FreeValue> partitioning) {
        if (partitioning.getPartition(1).parallel().anyMatch(v -> v.getRow().isPresent())
                || partitioning.getPartition(2).parallel().anyMatch(v -> v.getCol().isPresent())) {
            return false;
        }
        var remaining = new ArrayList<>(partitioning.getPartition(0).toList());
        partitioning.getPartition(1).forEach(v -> {
            v.setRow(n);
            if (!v.applyIfPossible(sqr)) {
                remaining.add(v);
            }
        });
        partitioning.getPartition(2).forEach(v -> {
            v.setCol(n);
            if (!v.applyIfPossible(sqr)) {
                remaining.add(v);
            }
        });
        if (!this.solve(sqr, n + 1, remaining)) {
            partitioning.getPartition(1).forEach(v -> {
                v.revertIfPossible(sqr);
                v.resetRow();
            });
            partitioning.getPartition(2).forEach(v -> {
                v.revertIfPossible(sqr);
                v.resetCol();
            });
            return false;
        }
        return true;
    }
}
