package hr.fer.tki.optimization.genetic;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class IndividualFactory {

    private IndividualFactory() {

    }

    public static Individual createIndividual(int numberOfParkingLanes) {
        Random random = new Random();
        List<Integer> values = random
                .ints(numberOfParkingLanes - 1, 0, numberOfParkingLanes)
                .boxed()
                .collect(Collectors.toList());
        return new Individual(values);
    }

    public static Individual createIndividual(List<Integer> values) {
        return new Individual(values);
    }
}
