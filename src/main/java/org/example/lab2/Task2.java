package org.example.lab2;

import mpi.MPI;
import mpi.Status;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Task2 {
    public static void main(String[] args) {
        MPI.Init(args);
        int myrank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        int[] message = new int[]{0, 1, 2};
        int TAG = 0;
        int[] data = new int[]{2002};
        int source = 0;
        int count = 0;
        int[] buf = null;
        Status status = null;

        if (myrank == 0) {
            MPI.COMM_WORLD.Send(data, 0, data.length, MPI.INT, 2, TAG);
        } else if (myrank == 1) {
            MPI.COMM_WORLD.Send(message, 0, message.length, MPI.INT, 2, TAG);
        } else {
            int received = 0;
            while (received < 2) {
                status = MPI.COMM_WORLD.Probe(MPI.ANY_SOURCE, TAG);
                source = status.source;
                count = status.count;
                buf = new int[count];
                MPI.COMM_WORLD.Recv(buf, 0, count, MPI.INT, source, TAG);
                System.out.printf("Received: %s%n",
                        Arrays.stream(buf)
                                .mapToObj(String::valueOf)
                                .collect(Collectors.joining(",", "[", "]")));
                received+=1;
            }

        }
        MPI.Finalize();
    }
}
