package dev.nicotopia.sqr;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class PartitionedSuperSet<E> {
    private final List<E> superSet;
    private final int partioning[];

    public PartitionedSuperSet(List<E> superSet, int partioning[]) {
        if (superSet.size() != partioning.length) {
            throw new RuntimeException("illegal partitioning");
        }
        this.superSet = superSet;
        this.partioning = partioning;
    }

    public Stream<E> getPartition(int partitionIdx) {
        return IntStream.range(0, this.partioning.length).filter(i -> partioning[i] == partitionIdx)
                .mapToObj(this.superSet::get);
    }

    @Override
    public String toString() {
        if (this.partioning.length == 0) {
            return "empty partitioning";
        }
        return String.join(", ", IntStream.rangeClosed(0, Arrays.stream(this.partioning).max().getAsInt())
                .mapToObj(i -> "(" + String.join(", ", this.getPartition(i).map(E::toString).toList()) + ")").toList());
    }
}
