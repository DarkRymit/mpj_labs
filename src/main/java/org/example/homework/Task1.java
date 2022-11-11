package org.example.homework;

import mpi.MPI;

import static java.lang.Math.cos;
import static java.lang.Math.pow;

public class Task1 {
    static double f(double x) {
        return (pow(x, 2) + 4 * x + 3) * cos(x);
    }

    public static void main(String[] args) throws InterruptedException {
        MPI.Init(args);
        double q_global, q_local, x_max = 0.0, x_min = -1.0;
        long start_time, end_time;
        double[] buffer = new double[1];
        double[] xb = new double[2];
        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        int i, m, target, tag = 0;
        start_time = System.nanoTime();
        if (rank == 0) {
            for (i = 1; i < size; i++) {
                xb[0] = ((double) (size - i) * x_min + (double) (i - 1) * x_max) / (double) (size - 1);
                xb[1] = ((double) (size - i - 1) * x_min + (double) (i) * x_max) / (double) (size - 1);
                tag = 1;
                target = i;
                MPI.COMM_WORLD.Send(xb, 0, xb.length, MPI.DOUBLE, target, tag);
            }
        } else {
            tag = 1;
            MPI.COMM_WORLD.Recv(xb, 0, xb.length, MPI.DOUBLE, 0, tag);
        }
        m = 100;

        if (rank != 0) {
            q_local = 0.0;

            System.out.printf("Processor %d is working! \n", rank);

            int mm = m + 1;

            for (i = 1; i <= mm - 1; i++)
                q_local += f(((mm - i) * xb[0] + (i - 1) * xb[1]) / (mm - 1)) + f(((mm - i - 1) * xb[0] + i * xb[1]) / (mm - 1));

            q_local = q_local * ((xb[1] - xb[0]) / m) / 2;

            target = 0;
            buffer[0] = q_local;
            tag = 2;
            MPI.COMM_WORLD.Send(buffer, 0, 1, MPI.DOUBLE, target, tag);
        } else {
            int received = 0;
            q_global = 0.0;

            while (received < size - 1) {
                int source = MPI.ANY_SOURCE;
                tag = 2;
                MPI.COMM_WORLD.Recv(buffer, 0, 1, MPI.DOUBLE, source, tag);
                q_local = buffer[0];
                q_global += q_local;
                received++;
            }

            System.out.printf("Processor %d is working! \n", rank);
            System.out.printf("\nIntegral f(x) = %f\n", q_global);

            end_time = System.nanoTime();

            System.out.printf("\nTime = %s\n", end_time - start_time);
        }
        MPI.Finalize();
    }
}
