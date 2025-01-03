package org.example;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

import java.io.IOException;
import java.util.concurrent.Callable;

public class DistributedMatrixMultiplication {

    public static class MatrixMultiplyTask implements Callable<Double[]>, IdentifiedDataSerializable {
        private double[] row;      // Fila de la matriz A
        private double[][] matrixB; // Matriz B completa
        private int size;          // Tamaño de las matrices

        public MatrixMultiplyTask() {
        }

        public MatrixMultiplyTask(double[] row, double[][] matrixB, int size) {
            this.row = row;
            this.matrixB = matrixB;
            this.size = size;
        }

        @Override
        public Double[] call() {
            if (row == null || matrixB == null || size <= 0) {
                throw new IllegalStateException("Datos inválidos para la tarea de multiplicación");
            }

            Double[] resultRow = new Double[size];
            for (int col = 0; col < size; col++) {
                resultRow[col] = 0.0;
                for (int k = 0; k < size; k++) {
                    resultRow[col] += row[k] * matrixB[k][col];
                }
            }
            return resultRow;
        }

        @Override
        public void writeData(ObjectDataOutput out) throws IOException {
            out.writeInt(size);
            out.writeDoubleArray(row);

            if (matrixB == null) {
                out.writeInt(0); // Longitud cero si la matriz es nula
            } else {
                out.writeInt(matrixB.length);
                for (double[] bRow : matrixB) {
                    out.writeDoubleArray(bRow);
                }
            }
        }

        @Override
        public void readData(ObjectDataInput in) throws IOException {
            size = in.readInt();
            row = in.readDoubleArray();

            int matrixBLength = in.readInt();
            if (matrixBLength > 0) {
                matrixB = new double[matrixBLength][];
                for (int i = 0; i < matrixBLength; i++) {
                    matrixB[i] = in.readDoubleArray();
                }
            } else {
                matrixB = null;
            }
        }

        @Override
        public int getFactoryId() {
            return MatrixTaskDataSerializableFactory.FACTORY_ID;
        }

        @Override
        public int getClassId() {
            return MatrixTaskDataSerializableFactory.MATRIX_MULTIPLY_TASK; // Cambiado a 1
        }

        public int getId() {
            return MatrixTaskDataSerializableFactory.MATRIX_MULTIPLY_TASK;
        }
    }
}
