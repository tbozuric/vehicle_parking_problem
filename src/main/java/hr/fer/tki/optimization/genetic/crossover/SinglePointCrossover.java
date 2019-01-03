package hr.fer.tki.optimization.genetic.crossover;

import hr.fer.tki.optimization.genetic.Individual;
import hr.fer.tki.optimization.genetic.IndividualFactory;
import hr.fer.tki.optimization.genetic.providers.ICrossover;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SinglePointCrossover implements ICrossover {

    @Override
    public Individual crossover(Individual firstParent, Individual secondParent) {
        List<Integer> firstParentValues = firstParent.getValues();
        List<Integer> secondParentValues = secondParent.getValues();

        int size = firstParentValues.size();

        int index = ThreadLocalRandom.current().nextInt(0, size);
        List<Integer> newValues = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            if (i < index) {
                newValues.add(firstParentValues.get(i));
            } else {
                newValues.add(secondParentValues.get(i));
            }
        }

        return IndividualFactory.createIndividual(newValues);
    }
}
