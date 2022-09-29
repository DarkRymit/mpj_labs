package org.example.lab3;

import mpi.Group;
import mpi.Intracomm;
import mpi.MPI;

public class Task3
{

    public static final String SEND_STRING = "Hi, Parallel Programmer!";

    public static void main( String[] args )
    {
        MPI.Init(args);

        int myRank = MPI.COMM_WORLD.Rank();
        int rank_in_group;
        int size = MPI.COMM_WORLD.Size();
        int count = SEND_STRING.length();
        char[] data = new char[count];
        int[] process_ranks = new int[size];
        Group MPI_GROUP_WORLD;
        Group group;
        Intracomm fcomm;

        for (int i = 0; i < size; i++)
        {
            process_ranks[i] = i;
        }

        MPI_GROUP_WORLD = MPI.COMM_WORLD.Group();
        group = MPI_GROUP_WORLD.Incl(process_ranks);
        fcomm = MPI.COMM_WORLD.Create(group);
        if (fcomm != MPI.COMM_NULL) {
            rank_in_group = fcomm.Rank();
            if (rank_in_group == 0) {
                data = SEND_STRING.toCharArray();
                fcomm.Bcast(data,0,count,MPI.CHAR,myRank);
                System.out.printf("Send by %d process: %s%n", myRank, new String(data));
            }
            else
            {
                fcomm.Bcast(data,0,count,MPI.CHAR,0);
                System.out.printf("Received from %d process: %s%n", myRank, new String(data));
            }
            fcomm.Free();
            group.free();
        }
        MPI.Finalize();
    }
}
