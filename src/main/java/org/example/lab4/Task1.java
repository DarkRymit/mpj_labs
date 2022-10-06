package org.example.lab4;

import mpi.Datatype;
import mpi.MPI;

import java.io.Serializable;

public class Task1
{

    public static class NewType implements Serializable {

        public float a;

        public float b;

        public float n;

        public NewType(float a, float b, float n) {
            this.a = a;
            this.b = b;
            this.n = n;
        }

        public NewType() {
        }

        @Override
        public String toString() {
            return "NewType{" +
                    "a=" + a +
                    ", b=" + b +
                    ", n=" + n +
                    '}';
        }
        static float [] wrap(NewType type){
            return new float[]{type.a,type.b,type.n};
        }
        static NewType unWrap(float[]  buff){
            return new NewType(buff[0],buff[1],buff[2]);
        }
    }

    public static void main( String[] args )
    {
        MPI.Init(args);
        int myRank = MPI.COMM_WORLD.Rank();
        int tag = 0;

        int[] blockLengths = new int[]{2, 1};
        Datatype[] types = new Datatype[]{MPI.FLOAT, MPI.FLOAT};
        int[] displacements = new int[]{0, 2};

        final Datatype NEW_MESSAGE_TYPE = Datatype.Struct(blockLengths,displacements,types);
        NEW_MESSAGE_TYPE.Commit();
        NewType type = null;
        float[] buffer = new float[3];
        Object[] objBuffer = new Object[1];

        if (myRank == 0)
        {
            type = new NewType();
            type.a = 3.14159f;
            type.b = 2.71828f;
            type.n = 2002;
            objBuffer[0]=type;
            buffer = NewType.wrap(type);
            MPI.COMM_WORLD.Send(buffer, 0,1,NEW_MESSAGE_TYPE, 1, tag);
            MPI.COMM_WORLD.Send(objBuffer, 0,1,MPI.OBJECT, 1, tag);
            System.out.printf("Process %d send as NEW_MESSAGE_TYPE : %s %n", myRank, NewType.unWrap(buffer));
            System.out.printf("Process %d send as Object : %s %n", myRank, objBuffer[0]);
        }
        else {
            MPI.COMM_WORLD.Recv(buffer, 0,1, NEW_MESSAGE_TYPE, 0, tag);
            MPI.COMM_WORLD.Recv(objBuffer, 0,1, MPI.OBJECT, 0, tag);
            System.out.printf("Process %d received as NEW_MESSAGE_TYPE : %s %n", myRank, NewType.unWrap(buffer));
            System.out.printf("Process %d received as Object : %s %n", myRank, objBuffer[0]);
        }
        MPI.Finalize();
    }


}
