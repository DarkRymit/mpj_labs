package org.example.lab3;

import mpi.MPI;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Task1
{

    public static final String SEND_STRING = "Hi, Parallel Programmer!";

    public static void main( String[] args )
    {
        MPI.Init(args);

        int myRank = MPI.COMM_WORLD.Rank();
        int count = SEND_STRING.length();
        char[] data = new char[count];


        if (myRank == 0)
        {
            data = SEND_STRING.toCharArray();
            MPI.COMM_WORLD.Bcast(data,0,count,MPI.CHAR,myRank);
            System.out.printf("Send by %d process: %s%n", myRank, new String(data));
        }
        else
        {
            MPI.COMM_WORLD.Bcast(data,0,count,MPI.CHAR,0);
            System.out.printf("Received from %d process: %s%n", myRank, new String(data));
        }
        MPI.Finalize();
    }


}
