package org.example.lab3;

import mpi.MPI;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

public class Task2
{

    public static void main( String[] args )
    {
        MPI.Init(args);

        int myRank = MPI.COMM_WORLD.Rank();
        int count = 1;
        float[] bufA = new float[1];
        float[] bufB = new float[1];
        int[] bufN = new int[1];
        if (myRank == 0)
        {
            bufA[0] = Float.parseFloat(args[3]);
            bufB[0] = Float.parseFloat(args[4]);
            bufN[0] = Integer.parseInt(args[5]);
            MPI.COMM_WORLD.Bcast(bufA,0,count,MPI.FLOAT,myRank);
            MPI.COMM_WORLD.Bcast(bufB,0,count,MPI.FLOAT,myRank);
            MPI.COMM_WORLD.Bcast(bufN,0,count,MPI.INT,myRank);
        }
        else
        {
            MPI.COMM_WORLD.Bcast(bufA, 0,count,MPI.FLOAT, 0);
            MPI.COMM_WORLD.Bcast(bufB, 0,count,MPI.FLOAT, 0);
            MPI.COMM_WORLD.Bcast(bufN, 0,count,MPI.INT, 0);
            System.out.printf("%d Process got %f %f %d%n", myRank, bufA[0], bufB[0], bufN[0]);
        }
        MPI.Finalize();
    }


}
