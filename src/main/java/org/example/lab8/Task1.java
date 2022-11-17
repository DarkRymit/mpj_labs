package org.example.lab8;

import mpi.MPI;

import java.util.Random;

public class Task1 {
    public static void main(String[] args) throws InterruptedException {
        MPI.Init(args);
//        Random random = new Random(LocalTime.now().getSecond());
        Random random = new Random(1);
        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        int n = 2;
        int m = 3;
        int[] a = new int[m*n];
        int[] b = new int[n];
        int[] c = new int[m];
        int[] map = new int[m];

        if(m % size == 0){
            if (rank == 0) {
                System.out.printf("\n Wrong m or size cause m %s div by size %s", m, size);
            }
            MPI.Finalize();
            return;
        }

        if (rank == 0) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++)
                    a[i+j*n] = -10 + random.nextInt(20);
                b[i] = -10 + random.nextInt(20);
            }

            System.out.print("\n Matrix A:\n");
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++)
                    System.out.printf("%d\t\t", a[i*n+j]);
                System.out.println();
            }

            System.out.print("\n Vector B:\n");
            for (int i = 0; i < n; i++)
                System.out.printf("%d\t\t", b[i]);
            System.out.println();
        }

        MPI.COMM_WORLD.Bcast(a,0,m*n,MPI.INT,0);
        MPI.COMM_WORLD.Bcast(b,0,n,MPI.INT,0);

        for (int i = 0; i < m; i++)
            map[i] = i % size;


        for (int k = 0; k < m; k++)
        {
            if (map[k] == rank) {
                c[k]=0;
                for (int i=0;i<n;i++){
                    c[k]+=a[k*n+i]*b[i];
                }
                System.out.printf("\nRank %s for %s result sum: %s",rank,k,c[k]);
            }
            MPI.COMM_WORLD.Bcast(c,k,1,MPI.INT,map[k]);
        }
        if (rank == 0)
        {
            System.out.print("\nResult Vector:\n");
            for (int i = 0; i < m; i++)
                System.out.printf("%d\t\t", c[i]);
            System.out.println();
        }

        MPI.Finalize();
    }
}
