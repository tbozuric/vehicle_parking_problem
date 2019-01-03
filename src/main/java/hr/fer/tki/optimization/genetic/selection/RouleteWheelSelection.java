package hr.fer.tki.optimization.genetic.selection;

import hr.fer.tki.optimization.genetic.Individual;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RouleteWheelSelection extends AbstractSelectionAlgorithm {


    public RouleteWheelSelection(List<Double> fitnessOfIndividuals, List<Individual> individuals) {
        super(fitnessOfIndividuals, individuals);
    }

    @Override
    public List<Individual> selection(int numberOfIndividuals) {
        List<Individual> selection = new ArrayList<>(1);

        double sum = fitnessOfIndividuals.stream().mapToDouble(Double::doubleValue).sum();
        double pick = ThreadLocalRandom.current().nextDouble(0, sum);
        int size = selection.size();
        double current = 0;

        for (int i = 0; i < size; i++) {
            current += fitnessOfIndividuals.get(i);
            if (current > pick) {
                selection.add(this.individuals.get(i));
                return selection;
            }
        }
        selection.add(this.individuals.get(size - 1));
        return selection;
    }
}