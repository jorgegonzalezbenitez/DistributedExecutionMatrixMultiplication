package org.example;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;

import java.util.concurrent.ExecutionException;

public class Main {

    private static final int MATRIX_SIZE = 1000;
    private static final GenerateMatrix generateMatrix = new GenerateMatrix();
    private static final MatrixMultiplication matrixMultiplication = new MatrixMultiplication();

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // Generar matrices A y B
        double[][] matrixA = generateMatrix.initializeMatrix(MATRIX_SIZE);
        double[][] matrixB = generateMatrix.initializeMatrix(MATRIX_SIZE);

        // Multiplicación secuencial
        System.out.println("Starting sequential matrix multiplication...");
        long sequentialStart = System.nanoTime();
        double[][] sequentialResult = matrixMultiplication.matrixMultiply(matrixA, matrixB);
        long sequentialEnd = System.nanoTime();
        double sequentialTime = (sequentialEnd - sequentialStart) / 1e9;
        System.out.println("Sequential time: " + sequentialTime + " seconds");

        // Configurar Hazelcast
        Config config = new Config();
        config.getSerializationConfig().addDataSerializableFactory(MatrixTaskDataSerializableFactory.FACTORY_ID, new MatrixTaskDataSerializableFactory());

        HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance(config);
        IExecutorService executorService = hazelcastInstance.getExecutorService("matrixMultiplication");

        // Multiplicación distribuida con Hazelcast
        System.out.println("Starting distributed matrix multiplication with Hazelcast...");
        Double[][] distributedResult = new Double[MATRIX_SIZE][MATRIX_SIZE];
        long distributedStart = System.nanoTime();

        for (int i = 0; i < MATRIX_SIZE; i++) {
            DistributedMatrixMultiplication.MatrixMultiplyTask task =
                    new DistributedMatrixMultiplication.MatrixMultiplyTask(matrixA[i], matrixB, MATRIX_SIZE);

            // Ejecutar las tareas distribuidas
            distributedResult[i] = executorService.submit(task).get();
        }

        long distributedEnd = System.nanoTime();
        double distributedTime = (distributedEnd - distributedStart) / 1e9;
        System.out.println("Distributed Hazelcast time: " + distributedTime + " seconds");

        hazelcastInstance.shutdown();
    }
}
