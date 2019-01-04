package hr.fer.tki;

import hr.fer.tki.models.Garage;
import hr.fer.tki.optimization.genetic.EliminationGeneticAlgorithm;
import hr.fer.tki.optimization.genetic.GeneticAlgorithm;
import hr.fer.tki.optimization.genetic.GeneticManager;
import hr.fer.tki.optimization.genetic.crossover.MultiPointCrossover;
import hr.fer.tki.optimization.genetic.crossover.SinglePointCrossover;
import hr.fer.tki.optimization.genetic.individual.IndividualFactory;
import hr.fer.tki.optimization.genetic.mutation.ShuffleMutation;
import hr.fer.tki.optimization.genetic.mutation.SimpleMutation;
import hr.fer.tki.optimization.genetic.mutation.UniformMutation;
import hr.fer.tki.optimization.genetic.providers.ICrossover;
import hr.fer.tki.optimization.genetic.providers.IMutation;
import hr.fer.tki.optimization.genetic.selection.TournamentSelection;
import hr.fer.tki.parser.InstanceParser;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Demo {

    public static void main(String[] args) throws FileNotFoundException {
        Garage garage = InstanceParser.parseInstance("src/main/resources/instanca2.txt");
        IndividualFactory factory = IndividualFactory.getFactory(garage);
        GeneticManager manager = GeneticManager.getManager(factory,
                1000, 3);


        List<IMutation> mutations = new ArrayList<>();
        mutations.add(new ShuffleMutation(factory));
        mutations.add(new SimpleMutation(factory, garage.getNumberOfParkingLanes()));
        mutations.add(new UniformMutation(factory, garage.getNumberOfParkingLanes()));

        List<ICrossover> crossovers = new ArrayList<>();
        crossovers.add(new SinglePointCrossover(factory));
        crossovers.add(new MultiPointCrossover(factory, 3));

        GeneticAlgorithm eliminationGeneticAlgorithm = new EliminationGeneticAlgorithm(manager,
                new TournamentSelection(3), mutations, crossovers, 10_000_000,
                60000, 0.05
        );

        eliminationGeneticAlgorithm.search();


//        GreedyParkingAlgorithm parkingAlgorithm = new GreedyParkingAlgorithm(garage);
//        parkingAlgorithm.parkVehiclesInTheGarage();
//
//        ValidatorResult validatorResult = GarageValidator.validate(garage);
//        for (String restriction : validatorResult.getViolatedRestrictions()) {
//            System.out.println(restriction);
//        }
//        System.out.println("GREEDY VALID: " + validatorResult.isValid());
//
//
//        GarageDrawer.drawGarage(garage);
//
//        TabooSearch tabooSearch = new TabooSearch(garage);
//        tabooSearch.parkVehiclesInTheGarage();
//        GarageDrawer.drawGarage(garage);
//
//        validatorResult = GarageValidator.validate(garage);
//        for (String restriction : validatorResult.getViolatedRestrictions()) {
//            System.out.println(restriction);
//        }
//        System.out.println("TABOO VALID: " + validatorResult.isValid());
//
//        GarageOutputWriter.printGarageToFile("src/main/resources/output3.txt", garage);
    }
}
