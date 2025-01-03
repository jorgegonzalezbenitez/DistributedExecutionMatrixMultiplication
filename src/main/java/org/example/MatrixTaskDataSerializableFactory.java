package org.example;

import com.hazelcast.nio.serialization.DataSerializableFactory;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

public class MatrixTaskDataSerializableFactory implements DataSerializableFactory {

    public static final int FACTORY_ID = 1;
    public static final int MATRIX_MULTIPLY_TASK = 1;

    @Override
    public IdentifiedDataSerializable create(int typeId) {
        switch (typeId) {
            case MATRIX_MULTIPLY_TASK:
                return new DistributedMatrixMultiplication.MatrixMultiplyTask();
            default:
                return null;
        }
    }
}
