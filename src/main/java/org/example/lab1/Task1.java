package org.example.lab1;

import mpi.MPI;
public class Task1
{
    public static void main( String[] args )
    {
        MPI.Init(args);
        int id = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        String name = MPI.Get_processor_name();
        System.out.printf("Name %s rank %s size %s%n",name,id,size);
        MPI.Finalize();
    }
}
