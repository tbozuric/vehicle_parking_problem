package hr.fer.tki.optimization.genetic.selection;

import hr.fer.tki.optimization.genetic.individual.IIndividual;
import hr.fer.tki.optimization.genetic.providers.ISelection;
import hr.fer.tki.util.IndicesGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class TournamentSelection implements ISelection {

    private int tournamentSize;

    public TournamentSelection(int tournamentSize) {
        this.tournamentSize = tournamentSize;
    }


    @Override
    public List<IIndividual> select(int numberOfIndividuals, List<Double> fitnessOfIndividuals, List<IIndividual> individuals) {
        List<IIndividual> selection = new ArrayList<>(tournamentSize);
        List<Double> fitnessOfSelectedIndividuals = new ArrayList<>(tournamentSize);


        List<Integer> indices = IndicesGenerator.uniqueRandomNumbers(individuals.size(), tournamentSize);

        for (int i = 0; i < tournamentSize; i++) {
            int index = indices.get(i);
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
