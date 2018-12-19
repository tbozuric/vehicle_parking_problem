package hr.fer.tki;

import hr.fer.tki.greedy.GreedyParkingAlgorithm;
import hr.fer.tki.models.Garage;
import hr.fer.tki.parser.InstanceParser;

public class Demo {

    public static void main(String[] args) {
        Garage garage = InstanceParser.parseInstance("src/main/resources/instanca1.txt");
        GreedyParkingAlgorithm parkingAlgorithm = new GreedyParkingAlgorithm(garage);
        parkingAlgorithm.parkVehiclesInTheGarage();

    }
}
