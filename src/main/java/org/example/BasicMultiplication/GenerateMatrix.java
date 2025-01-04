package org.example.BasicMultiplication;

import java.util.Random;

public class GenerateMatrix {

    public double[][] initializeMatrix(int size) {
        double[][] matrix = new double[size][size];
        Random random = new Random();

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                matrix[row][col] = random.nextDouble();
            }
        }
        return matrix;
    }
}
