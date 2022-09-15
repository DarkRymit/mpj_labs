package org.example.lab1;

import mpi.MPI;

public class Task2
{
    public static void main( String[] args )
    {
        MPI.Init(args);
        int rank = MPI.COMM_WORLD.Rank();
        char[] buf = new char[20];
        int tag = 0;
        if (rank == 0){
            String message = "Hi, Second Processor!";
            System.arraycopy(message.toCharArray(), 0, buf, 0,message.length()-1);
            MPI.COMM_WORLD.Send(buf,0,buf.length,MPI.CHAR,1,tag);
        }else {
            MPI.COMM_WORLD.Recv(buf,0,buf.length,MPI.CHAR,0,0);
            String received = String.copyValueOf(buf);
            System.out.printf("Received: %s", received);
        }
        MPI.Finalize();
    }
}
