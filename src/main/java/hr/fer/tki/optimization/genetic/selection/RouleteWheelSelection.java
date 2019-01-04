package hr.fer.tki.optimization.genetic.selection;

import hr.fer.tki.optimization.genetic.individual.IIndividual;
import hr.fer.tki.optimization.genetic.providers.ISelection;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RouleteWheelSelection implements ISelection {


    @Override
    public List<IIndividual> select(int numberOfIndividuals, List<Double> fitnessOfIndividuals, List<IIndividual> individuals) {
        List<IIndividual> selection = new ArrayList<>(1);

        double sum = fitnessOfIndividuals.stream().mapToDouble(Double::doubleValue).sum();
        double pick = ThreadLocalRandom.current().nextDouble(0, sum);
        int size = selection.size();
        double current = 0;

        for (int i = 0; i < size; i++) {
            current += fitnessOfIndividuals.get(i);
            if (current > pick) {
                selection.add(individuals.get(i));
                return selection;
            }
        }
        selection.add(individuals.get(size - 1));
        return selection;
    }
}
