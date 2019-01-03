package hr.fer.tki.optimization.genetic.crossover;

import hr.fer.tki.optimization.genetic.Individual;
import hr.fer.tki.optimization.genetic.IndividualFactory;
import hr.fer.tki.optimization.genetic.providers.ICrossover;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PrimitiveIterator;
import java.util.concurrent.ThreadLocalRandom;

public class MultiPointCrossover implements ICrossover {

    private int numberOfCrossoverPoints;

    public MultiPointCrossover(int numberOfCrossoverPoints) {
        this.numberOfCrossoverPoints = numberOfCrossoverPoints;
    }

    public int getNumberOfCrossoverPoints() {
        return numberOfCrossoverPoints;
    }

    public void setNumberOfCrossoverPoints(int numberOfCrossoverPoints) {
        this.numberOfCrossoverPoints = numberOfCrossoverPoints;
    }

    @Override
    public Individual crossover(Individual firstParent, Individual secondParent) {
        List<Integer> indices = new ArrayList<>(numberOfCrossoverPoints);
        PrimitiveIterator.OfInt iterator = ThreadLocalRandom.current()
                .ints(0, numberOfCrossoverPoints)
                .distinct()
                .iterator();
        for (int i = 0; i < numberOfCrossoverPoints; i++) {
            indices.add(iterator.next());
        }

        Collections.sort(indices);

        List<Integer> firstParentValues = firstParent.getValues();
        List<Integer> secondParentValues = secondParent.getValues();

        int size = firstParentValues.size();
        List<Integer> newValues = new ArrayList<>(size);
        boolean first = true;
        int indexCounter = 0;
        int currentIndex = indices.get(indexCounter++);

        for (int i = 0; i < size; i++) {
            if (i < currentIndex) {
                if (first) {
                    newValues.add(firstParentValues.get(i));
                } else {
                    newValues.add(secondParentValues.get(i));
                }
            } else if (i == currentIndex) {
                if (first) {
                    newValues.add(firstParentValues.get(i));
                } else {
                    newValues.add(secondParentValues.get(i));
                }
                first = !first;

                currentIndex = indices.get(indexCounter++);

            }
        }
        return IndividualFactory.createIndividual(newValues);
    }
}
