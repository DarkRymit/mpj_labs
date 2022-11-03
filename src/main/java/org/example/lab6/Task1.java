package org.example.lab6;

import mpi.Cartcomm;
import mpi.MPI;
import mpi.Status;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Random;

import static mpi.Cartcomm.Dims_create;

public class Task1 {
    public static void main(String[] args) throws InterruptedException {
        Random random = new Random(LocalTime.now().getSecond());
        int m = 4;
        int n = 10+random.nextInt(20);
        int[][] matrix = new int[m][n];
        for (int i=0;i<m;i++){
            int[] row = new int[n];
            for (int j=0;j<n;j++){
                row[j]=-100 + random.nextInt(200);
            }
            matrix[i]=row;
        }

        MPI.Init(args);
        int id = MPI.COMM_WORLD.Rank();

        if (id==0) {
            System.out.println("Lab-6 SP-435 Hurbanov Tamerlan variant 3");
            System.out.println("Matrix");
            for (int[] arr : matrix) {
                System.out.println(Arrays.toString(arr));
            }
        }
        Thread.sleep(100);

        long start = System.nanoTime();
        int[] array = matrix[id];
        int result=0;

        for (int el:array){
            if(el > 0){
                result++;
            }
        }
        long finish = System.nanoTime();
        int time = (int)(finish - start);
        System.out.printf("rank %s result %s time spend %s%n",id,result,time);

        int[] resB = new int[m];
        if (id==0) {
            int source = 0;
            int count = 0;
            int[] buf = null;
            Status status = null;
            int received = 0;
            resB[0] = result;
            int allTime = time;
            while (received < m-1) {
                status = MPI.COMM_WORLD.Probe(MPI.ANY_SOURCE, 0);
                source = status.source;
                count = status.count;
                buf = new int[count];
                MPI.COMM_WORLD.Recv(buf, 0, count, MPI.INT, source, 0);
                resB[source]=buf[0];
                allTime+=buf[1];
                received += 1;
            }
            System.out.printf("Result par %s time spend %s%n", Arrays.toString(resB),allTime);
        }else {
            int[] data = new int[]{result,time};
            MPI.COMM_WORLD.Send(data, 0, data.length, MPI.INT, 0, 0);
        }

        Thread.sleep(100);
        if (id==0) {
            start = System.nanoTime();

            int[] resC = new int[m];

            for (int i=0; i< matrix.length;i++) {
                for (int el : matrix[i]) {
                    if (el > 0) {
                        resC[i]++;
                    }
                }
            }
            finish = System.nanoTime();
            System.out.printf("Result seq %s time spend %s%n", Arrays.toString(resC),finish - start);
        }

        MPI.Finalize();
    }
}
