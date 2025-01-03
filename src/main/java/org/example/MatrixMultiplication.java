package org.example;

// Clase para manejar la multiplicación secuencial de matrices
public class MatrixMultiplication {

    public double[][] matrixMultiply(double[][] A, double[][] B) {
        int size = A.length;
        double[][] result = new double[size][size];

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                result[row][col] = 0.0;
                for (int k = 0; k < size; k++) {
                    result[row][col] += A[row][k] * B[k][col];
                }
            }
        }
        return result;
    }
}
