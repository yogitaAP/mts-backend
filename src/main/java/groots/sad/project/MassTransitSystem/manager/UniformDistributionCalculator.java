package groots.sad.project.MassTransitSystem.manager;

import java.util.concurrent.ThreadLocalRandom;

public class UniformDistributionCalculator {

    public static int generateRandomNumber(int lowBound,int highBound){

        return ThreadLocalRandom.current().nextInt(lowBound,highBound+1);
    }
}
