package org.example;

import com.hazelcast.config.Config;
import com.hazelcast.config.SerializationConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

public class HazelcastConfigurator {

    public static HazelcastInstance createHazelcastInstance() {
        Config config = new Config();
        SerializationConfig serializationConfig = config.getSerializationConfig();

        // Registrar la fábrica de serialización
        serializationConfig.addDataSerializableFactory(
                MatrixTaskDataSerializableFactory.FACTORY_ID,
                new MatrixTaskDataSerializableFactory()
        );

        return Hazelcast.newHazelcastInstance(config);
    }
}
