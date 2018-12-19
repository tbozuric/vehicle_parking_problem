package hr.fer.tki;

import hr.fer.tki.greedy.GreedyParkingAlgorithm;
import hr.fer.tki.models.Garage;
import hr.fer.tki.parser.InstanceParser;
import hr.fer.tki.validator.GarageValidator;
import hr.fer.tki.validator.ValidatorResult;

public class Demo {

    public static void main(String[] args) {
        Garage garage = InstanceParser.parseInstance("src/main/resources/instanca1.txt");
        GreedyParkingAlgorithm parkingAlgorithm = new GreedyParkingAlgorithm(garage);
        parkingAlgorithm.parkVehiclesInTheGarage();

        ValidatorResult validatorResult = GarageValidator.validate(garage);
        for(String restriction : validatorResult.getViolatedRestrictions()){
            System.out.println(restriction);
        }
        System.out.println(validatorResult.isValid());
    }
}
