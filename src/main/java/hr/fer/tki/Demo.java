package hr.fer.tki;

import hr.fer.tki.models.Garage;
import hr.fer.tki.parser.InstanceParser;

public class Demo {

    public static void main(String[] args) {
        Garage garage = InstanceParser.parseInstance("src/main/resources/instanca1.txt");
        System.out.println();
    }
}
