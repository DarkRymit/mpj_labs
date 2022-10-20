package org.example.lab5;

import mpi.Cartcomm;
import mpi.MPI;

import static mpi.Cartcomm.Dims_create;

public class Task1 {
    public static void main(String[] args) {

        int my_grid_rank;
        int coords[] = new int[2];


        coords[0] = 0;
        coords[1] = 1;
        MPI.Init(args);
        int size = MPI.COMM_WORLD.Size();
        int dims[] = { 0, 0 };
        Dims_create(size,  dims);
        boolean periods[] = { true, true };
        boolean reorder = true;
        Cartcomm comm_cart = MPI.COMM_WORLD.Create_cart(dims, periods, reorder);
        my_grid_rank = comm_cart.Rank();
        int coordinates[] = comm_cart.Coords(my_grid_rank);
        System.out.printf("Process rank %d has coordinates %d %d\n",
                my_grid_rank, coordinates[0], coordinates[1]);
        MPI.Finalize();
    }
}
