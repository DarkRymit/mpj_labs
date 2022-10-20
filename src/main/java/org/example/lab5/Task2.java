package org.example.lab5;

import mpi.Cartcomm;
import mpi.MPI;

import static mpi.Cartcomm.*;

public class Task2 {

    public static void main(String[] args) {
        MPI.Init(args);

        // Size of the default communicator
        int size = MPI.COMM_WORLD.Size();
        if (size != 4)
        {
            System.out.printf("This application is meant to be run with 4 processes, not %d.\n", size);
        }

        // Ask MPI to decompose our processes in a 2D cartesian grid for us
        int dims[] = { 0, 0 };
        Dims_create(size,  dims);

        // Make both dimensions non-periodic
        boolean periods[] = { false, false };

        // Let MPI assign arbitrary ranks if it deems it necessary
        boolean reorder = true;
        Cartcomm comm_cart = MPI.COMM_WORLD.Create_cart(dims, periods, reorder);



        // Declare our neighbours
        String neighbours_names[] = { "down", "up", "left", "right" };
        int neighbours_ranks[]= new int[4];

        // Let consider dims[0] = X, so the shift tells us our left and right neighbours
        comm_cart.Shift(0, 1);


        // Let consider dims[1] = Y, so the shift tells us our up and down neighbours
        comm_cart.Shift(1, 1);

        // Get my rank in the new communicator
        int my_rank = comm_cart.Rank();
        int coordinates[] = comm_cart.Coords(my_rank);
        System.out.printf("Process rank %d has coordinates %d %d\n",
                my_rank, coordinates[0], coordinates[1]);

        for (int i = 0; i < 4; i++)
        {
            if (neighbours_ranks[i] == MPI.PROC_NULL)
                System.out.printf("[MPI process %d] I have no %s neighbour.\n", my_rank, neighbours_names[i]);
            else
                System.out.printf("[MPI process %d] I have a %s neighbour: process %d.\n", my_rank, neighbours_names[i], neighbours_ranks[i]);
        }

        MPI.Finalize();

    }
}
