package nl.codecentric.xpdays2017.gareth;


import nl.codecentric.xpdays2017.gareth.definition.BicycleShedDefinition;
import org.craftsmenlabs.gareth.api.ExperimentEngine;
import org.craftsmenlabs.gareth.api.ExperimentEngineConfig;
import org.craftsmenlabs.gareth.core.ExperimentEngineConfigImpl;
import org.craftsmenlabs.gareth.core.ExperimentEngineImpl;
import org.craftsmenlabs.gareth.core.ExperimentEngineImplBuilder;

/**
 * Created by hylke on 22/05/2017.
 */
public class BicycleShedContinuousValidationApplication {

    public static void main(final String[] args) {
        final ExperimentEngineConfig experimentEngineConfig = new ExperimentEngineConfigImpl
                .Builder()
                .addDefinitionClass(BicycleShedDefinition.class)
                .addInputStreams(BicycleShedContinuousValidationApplication.class.getClass().getResourceAsStream("/experiments/bicycleshed.experiment"))
                .setIgnoreInvocationExceptions(true)
                .build();
        final ExperimentEngine experimentEngine = new ExperimentEngineImplBuilder(experimentEngineConfig).build();
        experimentEngine.start();


        Runtime.getRuntime().addShutdownHook(new ShutdownHook(experimentEngine));

    }

    /**
     * Shutdown hook when application is stopped then also stop the experiment engine.
     */
    static class ShutdownHook extends Thread {

        private final ExperimentEngine experimentEngine;

        private ShutdownHook(final ExperimentEngine experimentEngine) {
            this.experimentEngine = experimentEngine;
        }

        @Override
        public void run() {
            experimentEngine.stop();
        }
    }
}
