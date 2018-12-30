package hr.fer.tki.parser;

import hr.fer.tki.models.Garage;
import hr.fer.tki.models.ParkingLane;
import hr.fer.tki.models.Vehicle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Integer.parseInt;

public class InstanceParser {

    public static Garage parseInstance(String instanceFilepath) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(instanceFilepath));
            return parse(lines);
        } catch (IOException | ArrayIndexOutOfBoundsException e) {
            throw new ParsingException(e.getMessage());
        }
    }

    private static Garage parse(List<String> lines) {
        int currentLine = 0;
        int numberOfVehicles = parseInteger(lines.get(currentLine++));
        int numberOfParkingLanes = parseInteger(lines.get(currentLine++));
        assertBlankLine(lines, currentLine++);

        int[] vehiclesLengths = parseIntArray(lines.get(currentLine++));
        assertArrayLength(vehiclesLengths.length, numberOfVehicles);
        assertBlankLine(lines, currentLine++);

        int[] vehicleSeries = parseIntArray(lines.get(currentLine++));
        assertArrayLength(vehicleSeries.length, numberOfVehicles);
        assertBlankLine(lines, currentLine++);

        Boolean[][] parkingPermissions = parseBoolean2DArray(lines.subList(currentLine, currentLine + numberOfVehicles), numberOfParkingLanes);
        currentLine += numberOfVehicles;
        assertBlankLine(lines, currentLine++);

        int[] parkingLanesLengths = parseIntArray(lines.get(currentLine++));
        assertArrayLength(parkingLanesLengths.length, numberOfParkingLanes);
        assertBlankLine(lines, currentLine++);

        int[] departureTimes = parseIntArray(lines.get(currentLine++));
        assertArrayLength(departureTimes.length, numberOfVehicles);
        assertBlankLine(lines, currentLine++);

        int[] typesOfSchedule = parseIntArray(lines.get(currentLine++));
        assertArrayLength(typesOfSchedule.length, numberOfVehicles);
        assertBlankLine(lines, currentLine++);

        List<Vehicle> vehicles = new ArrayList<>(numberOfVehicles);
        for (int i = 0; i < numberOfVehicles; i++) {
            vehicles.add(new Vehicle(vehiclesLengths[i], vehicleSeries[i], typesOfSchedule[i], departureTimes[i]));
        }

        List<ParkingLane> parkingLanes = new ArrayList<>(numberOfParkingLanes);
        for (int i = 0; i < numberOfParkingLanes; i++) {
            parkingLanes.add(new ParkingLane(parkingLanesLengths[i]));
        }

        int size = lines.size();
        while (currentLine < size) {
            String line = lines.get(currentLine++);
            if (line.trim().isEmpty())
                break;

            int[] blockingIndices = parseIntArray(line);
            ParkingLane blocking = parkingLanes.get(blockingIndices[0] - 1);

            for (int i = 1; i < blockingIndices.length; i++) {
                ParkingLane blocked = parkingLanes.get(blockingIndices[i] - 1);
                blocked.addBlockingParkingLane(blocking);
            }
        }

        return new Garage(numberOfParkingLanes, numberOfVehicles, parkingLanes, vehicles, parkingPermissions);
    }

    private static void assertBlankLine(List<String> lines, int lineIndex) {
        if (!lines.get(lineIndex).trim().isEmpty())
            throw new ParsingException(String.format("Line %d should be empty.", lineIndex));
    }

    private static void assertArrayLength(int arrayLength, int expectedLength) {
        if (arrayLength != expectedLength)
            throw new ParsingException(String.format("Expected length %d, found %d", expectedLength, arrayLength));
    }

    private static int parseInteger(String s) {
        try {
            return parseInt(s);
        } catch (NumberFormatException e) {
            throw new ParsingException(e.getMessage());
        }
    }

    private static Boolean parseBoolean(String s) {
        s = s.trim();
        switch (s) {
            case "1":
                return true;
            case "0":
                return false;
            default:
                throw new ParsingException("Can't parse " + s + " to boolean value");
        }
    }

    private static int[] parseIntArray(String line) {
        String[] chunks = line.split(" ");
        return Arrays.stream(chunks).mapToInt(InstanceParser::parseInteger).toArray();
    }

    private static Boolean[] parseBooleanArray(String line) {
        String[] chunks = line.split(" ");

        return Arrays.stream(chunks).map(InstanceParser::parseBoolean).toArray(Boolean[]::new);
    }

    private static Boolean[][] parseBoolean2DArray(List<String> lines, int expectedColumns) {
        Boolean[][] array = new Boolean[lines.size()][expectedColumns];

        for (int i = 0; i < lines.size(); i++) {
            array[i] = parseBooleanArray(lines.get(i));
            assertArrayLength(array[i].length, expectedColumns);
        }

        return array;
    }
}
