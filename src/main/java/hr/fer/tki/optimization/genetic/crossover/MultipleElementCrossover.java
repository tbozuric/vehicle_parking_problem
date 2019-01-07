package hr.fer.tki.optimization.genetic.crossover;

import hr.fer.tki.optimization.genetic.individual.IIndividual;
import hr.fer.tki.optimization.genetic.individual.IIndividualFactory;
import hr.fer.tki.util.IndicesGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MultipleElementCrossover extends AbstractCrossoverAlgorithm {

    private int numberOfCrossoverElements;

    public MultipleElementCrossover(IIndividualFactory factory, int numberOfCrossoverElements) {
        super(factory);
        this.numberOfCrossoverElements = numberOfCrossoverElements;
    }

    @Override
    public IIndividual crossover(IIndividual firstParent, IIndividual secondParent) {
        List<Integer> firstParentValues = firstParent.getValues();
        List<Integer> secondParentValues = secondParent.getValues();

        int size = firstParentValues.size();

        List<Integer> indices = IndicesGenerator.uniqueRandomNumbers(size, numberOfCrossoverElements);
        Collections.sort(indices);

        List<Integer> newValues = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            if (indices.contains(i)) {
                newValues.add(firstParentValues.get(i));
            } else {
                newValues.add(secondParentValues.get(i));
            }
        }
        return factory.createIndividual(newValues);}
}
