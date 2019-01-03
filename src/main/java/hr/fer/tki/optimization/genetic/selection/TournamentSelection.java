package hr.fer.tki.optimization.genetic.selection;

import hr.fer.tki.optimization.genetic.Individual;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PrimitiveIterator;
import java.util.concurrent.ThreadLocalRandom;

public class TournamentSelection extends AbstractSelectionAlgorithm {

    private int tournamentSize;

    public TournamentSelection(List<Double> fitnessOfIndividuals, List<Individual> individuals, int tournamentSize) {
        super(fitnessOfIndividuals, individuals);
        this.tournamentSize = tournamentSize;
    }


    @Override
    public List<Individual> selection(int numberOfIndividuals) {
        List<Individual> selection = new ArrayList<>(tournamentSize);
        List<Double> fitnessOfSelectedIndividuals = new ArrayList<>(tournamentSize);

        PrimitiveIterator.OfInt iterator = ThreadLocalRandom.current()
                .ints(0, tournamentSize)
                .distinct()
                .iterator();
        for (int i = 0; i < tournamentSize; i++) {
            int index = iterator.next();
            fitnessOfSelectedIndividuals.add(fitnessOfIndividuals.get(index));
            selection.add(individuals.get(index));
        }

        //TODO: complete the algorithm


        return selection;
    }
}
