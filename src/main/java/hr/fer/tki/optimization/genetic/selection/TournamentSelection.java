package hr.fer.tki.optimization.genetic.selection;

import hr.fer.tki.optimization.genetic.individual.IIndividual;

import java.util.ArrayList;
import java.util.List;
import java.util.PrimitiveIterator;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class TournamentSelection extends AbstractSelectionAlgorithm {

    private int tournamentSize;

    public TournamentSelection(List<Double> fitnessOfIndividuals, List<IIndividual> individuals,
                               int tournamentSize) {
        super(fitnessOfIndividuals, individuals);
        this.tournamentSize = tournamentSize;
    }


    @Override
    public List<IIndividual> select(int numberOfIndividuals) {
        List<IIndividual> selection = new ArrayList<>(tournamentSize);
        List<Double> fitnessOfSelectedIndividuals = new ArrayList<>(tournamentSize);

        PrimitiveIterator.OfInt iterator = ThreadLocalRandom.current()
                .ints(tournamentSize, 0, individuals.size())
                .distinct()
                .iterator();

        for (int i = 0; i < tournamentSize; i++) {
            int index = iterator.next();
            selection.add(individuals.get(index));
            fitnessOfSelectedIndividuals.add(fitnessOfIndividuals.get(index));
        }


        int[] sortedIndices = IntStream
                .range(0, fitnessOfSelectedIndividuals.size())
                .boxed()
                .sorted((i, j) -> fitnessOfSelectedIndividuals.get(j).compareTo(fitnessOfSelectedIndividuals.get(i)))
                .mapToInt(element -> element)
                .toArray();


        List<IIndividual> tournament = new ArrayList<>(tournamentSize);
        for (int i : sortedIndices) {
            tournament.add(selection.get(i));
        }


        return tournament;
    }
}
