package hr.fer.tki;

import hr.fer.tki.models.Garage;
import hr.fer.tki.optimization.genetic.EliminationGeneticAlgorithm;
import hr.fer.tki.optimization.genetic.GeneticAlgorithm;
import hr.fer.tki.optimization.genetic.GeneticManager;
import hr.fer.tki.optimization.genetic.crossover.MultipleElementCrossover;
import hr.fer.tki.optimization.genetic.individual.IIndividual;
import hr.fer.tki.optimization.genetic.individual.IndividualFactory;
import hr.fer.tki.optimization.genetic.individual.ParkingIndividual;
import hr.fer.tki.optimization.genetic.mutation.SimpleMutation;
import hr.fer.tki.optimization.genetic.mutation.UniformMutation;
import hr.fer.tki.optimization.genetic.providers.ICrossover;
import hr.fer.tki.optimization.genetic.providers.IMutation;
import hr.fer.tki.optimization.genetic.selection.TournamentSelection;
import hr.fer.tki.output.GarageOutputWriter;
import hr.fer.tki.parser.InstanceParser;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Demo {

    private static final int INSTANCE = 3;

    public static void main(String[] args) throws FileNotFoundException {
        Garage garage = InstanceParser.parseInstance("src/main/resources/instanca" + INSTANCE + ".txt");
        IndividualFactory factory = IndividualFactory.getFactory(garage);
        GeneticManager manager = GeneticManager.getManager(factory, 10, 3);

        List<IMutation> mutations = new ArrayList<>();
        mutations.add(new SimpleMutation(factory, garage.getNumberOfParkingLanes()));
        mutations.add(new UniformMutation(factory, garage.getNumberOfParkingLanes()));

        List<ICrossover> crossovers = new ArrayList<>();
        crossovers.add(new MultipleElementCrossover(factory, 1));
        crossovers.add(new MultipleElementCrossover(factory, 2));
        crossovers.add(new MultipleElementCrossover(factory, 3));
        crossovers.add(new MultipleElementCrossover(factory, 4));
        crossovers.add(new MultipleElementCrossover(factory, 5));
        crossovers.add(new MultipleElementCrossover(factory, 6));

        GeneticAlgorithm eliminationGeneticAlgorithm = new EliminationGeneticAlgorithm(manager,
                new TournamentSelection(3), mutations, crossovers, 20_000_000,
                60000, 0.03
        );

        IIndividual result = eliminationGeneticAlgorithm.search();
        Garage resultGarage = ((ParkingIndividual) result).getGarage();

        GarageOutputWriter.printGarageToFile("src/main/resources/output" + INSTANCE + "_3.txt", resultGarage);
    }
}
