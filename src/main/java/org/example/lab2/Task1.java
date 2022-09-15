package org.example.lab2;

import mpi.MPI;

import java.nio.ByteBuffer;

public class Task1
{
    public static void main( String[] args )
    {
        MPI.Init(args);
        byte[] buffer = null;
        ByteBuffer byteBuffer = null;
        int myrank = MPI.COMM_WORLD.Rank();
        int buffsize = 1;
        int TAG = 0;
        if (myrank == 0)
        {
            byteBuffer = ByteBuffer.allocate(MPI.COMM_WORLD.Pack_size(1,MPI.BYTE) + MPI.BSEND_OVERHEAD);
            MPI.Buffer_attach(byteBuffer);
            buffer = new byte[]{10};
            MPI.COMM_WORLD.Bsend(buffer,0,buffer.length,MPI.BYTE,myrank + 1, TAG);
            MPI.Buffer_detach();
        }
        else
        {
            buffer = new byte[10];
            MPI.COMM_WORLD.Recv(buffer, 0, buffsize, MPI.BYTE, myrank - 1, TAG);
            System.out.printf("Received: %s%n", buffer[0]);
        }

        MPI.Finalize();
    }
}
