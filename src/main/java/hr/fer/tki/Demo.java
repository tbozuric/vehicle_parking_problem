package hr.fer.tki;

import hr.fer.tki.models.Garage;
import hr.fer.tki.optimization.greedy.GreedyParkingAlgorithm;
import hr.fer.tki.optimization.taboo.TabooSearch;
import hr.fer.tki.output.GarageOutputWriter;
import hr.fer.tki.parser.InstanceParser;
import hr.fer.tki.sketch.GarageDrawer;
import hr.fer.tki.validator.GarageValidator;
import hr.fer.tki.validator.ValidatorResult;

import java.io.FileNotFoundException;

public class Demo {

    public static void main(String[] args) throws FileNotFoundException {
        Garage garage = InstanceParser.parseInstance("src/main/resources/instanca1.txt");
        GreedyParkingAlgorithm parkingAlgorithm = new GreedyParkingAlgorithm(garage);
        parkingAlgorithm.parkVehiclesInTheGarage();

        ValidatorResult validatorResult = GarageValidator.validate(garage);
        for (String restriction : validatorResult.getViolatedRestrictions()) {
            System.out.println(restriction);
        }
        System.out.println("GREEDY VALID: " + validatorResult.isValid());


        GarageDrawer.drawGarage(garage);

        TabooSearch tabooSearch = new TabooSearch(garage);
        tabooSearch.parkVehiclesInTheGarage();
        GarageDrawer.drawGarage(garage);

        validatorResult = GarageValidator.validate(garage);
        for (String restriction : validatorResult.getViolatedRestrictions()) {
            System.out.println(restriction);
        }
        System.out.println("TABOO VALID: " + validatorResult.isValid());

        GarageOutputWriter.printGarageToFile("src/main/resources/output3.txt", garage);
    }
}
