package hr.fer.tki.optimization.genetic.crossover;

import hr.fer.tki.optimization.genetic.individual.IIndividual;
import hr.fer.tki.optimization.genetic.individual.IIndividualFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SingleElementCrossover extends AbstractCrossoverAlgorithm {

    public SingleElementCrossover(IIndividualFactory factory) {
        super(factory);
    }

    @Override
    public IIndividual crossover(IIndividual firstParent, IIndividual secondParent) {
        List<Integer> firstParentValues = firstParent.getValues();
        List<Integer> secondParentValues = secondParent.getValues();

        int size = firstParentValues.size();

        int index = ThreadLocalRandom.current().nextInt(0, size);
        List<Integer> newValues = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            if (i == index) {
                newValues.add(firstParentValues.get(i));
            } else {
                newValues.add(secondParentValues.get(i));
            }
        }
        return factory.createIndividual(newValues);
    }
}
