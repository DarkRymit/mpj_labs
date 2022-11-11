package org.example.lab7;

import mpi.MPI;

import java.time.LocalTime;
import java.util.Random;

public class Task1 {
    public static void main(String[] args) throws InterruptedException {
        MPI.Init(args);
//        Random random = new Random(LocalTime.now().getSecond());
        Random random = new Random(1);
        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        int n = 5;
        double[] A = new double[n*n];
        double[] b = new double[n], c = new double[n], x = new double[n];
        int[] map = new int[n];
        double sum;
        long start_time, end_time;
        long calc_start_time, calc_end_time;

        if (rank == 0) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++)
                    A[i*n+j] = -10 + random.nextDouble()*20;
                b[i] = -10 + random.nextDouble()*20;
            }

            System.out.print("\n Matrix A:\n");
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++)
                    System.out.printf("%1.1f \t", A[i*n+j]);
                System.out.println();
            }

            System.out.print("\n Vector B:\n");
            for (int i = 0; i < n; i++)
                System.out.printf("%1.1f \t", b[i]);
            System.out.println();
        }
        start_time = System.nanoTime();
        MPI.COMM_WORLD.Bcast(A,0,n*n,MPI.DOUBLE,0);
        MPI.COMM_WORLD.Bcast(b,0,n,MPI.DOUBLE,0);
        end_time = System.nanoTime();
        System.out.printf("\nSend from 0 process time = %s\n", end_time - start_time);
        for (int i = 0; i < n; i++)
            map[i] = i % size;

        calc_start_time = System.nanoTime();

        for (int k = 0; k < n; k++)
        {
            start_time = System.nanoTime();
            MPI.COMM_WORLD.Bcast(A, k*n+k,n-k, MPI.DOUBLE, map[k]);
            MPI.COMM_WORLD.Bcast(b, k,1, MPI.DOUBLE, map[k]);
            end_time = System.nanoTime();

            for (int i = k + 1; i < n; i++)
                if (map[i] == rank) {
                    c[i] = A[i*n+k] / A[k*n+k];

                    for (int j = 0; j < n; j++)
                        A[i*n+j] = A[i*n+j] - (c[i] * A[k*n+j]);
                    b[i] = b[i] - (c[i] * b[k]);
                }

        }
        System.out.printf("\nReceive by %d process time = %s\n", rank, end_time - start_time);
        if (rank == 0)
        {
            x[n - 1] = b[n - 1] / A[(n - 1)*n+n - 1];

            for (int i = n - 2; i >= 0; i--)
            {
                sum = 0;
                for (int j = i + 1; j < n; j++)
                    sum = sum + A[i*n+j] * x[j];
                x[i] = (b[i] - sum) / A[i*n+i];
            }
            calc_end_time = System.nanoTime();
            System.out.printf("Calculation time = %s\n", calc_end_time - (end_time - start_time) - calc_start_time);

            System.out.print("\nResult:");
            for (int i = 0; i < n; i++)
                System.out.printf("\nx%d=%f\t", i+1, x[i]);
        }

        MPI.Finalize();
    }
}
