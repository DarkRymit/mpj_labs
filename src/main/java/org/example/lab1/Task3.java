package org.example.lab1;

import mpi.MPI;

public class Task3 {
    public static void main(String[] args) {
        MPI.Init(args);
        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        int[] message = new int[1];
        message[0] = rank;
        if ((rank % 2) == 0) {
            if ((rank + 1) != size) {
                MPI.COMM_WORLD.Send(message, 0, 1, MPI.INT, rank + 1, 0);
            }
        } else {
            MPI.COMM_WORLD.Recv(message, 0, 1, MPI.INT, rank - 1, 0);
            System.out.printf("Received: %s%n", message[0]);
        }

        MPI.Finalize();
    }
}
