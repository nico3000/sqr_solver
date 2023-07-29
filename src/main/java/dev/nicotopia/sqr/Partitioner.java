package dev.nicotopia.sqr;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Partitioner {
    public static <E> Stream<PartitionedSuperSet<E>> stream(List<E> superSet, int numPartitions) {
        int partitioning[] = new int[superSet.size()];
        Stream.Builder<PartitionedSuperSet<E>> builder = Stream.builder();
        do {
            builder.add(new PartitionedSuperSet<>(superSet, Arrays.copyOf(partitioning, partitioning.length)));
        } while (Partitioner.next(partitioning, numPartitions));
        return builder.build();
    }

    private static boolean next(int partitioning[], int numPartitions) {
        int i = 0;
        while (i != partitioning.length) {
            partitioning[i] = (partitioning[i] + 1) % numPartitions;
            if (partitioning[i] != 0) {
                return true;
            }
            ++i;
        }
        return false;
    }
}
