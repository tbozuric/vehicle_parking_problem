package hr.fer.tki.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IndicesGenerator {

    public static List<Integer> uniqueRandomNumbers(int upperLimit, int numberOfPoints) {
        List<Integer> list = new ArrayList<>(upperLimit);
        List<Integer> indices = new ArrayList<>(numberOfPoints);
        for (int i = 0; i < upperLimit; i++) {
            list.add(i);
        }
        Collections.shuffle(list);
        for (int i = 0; i < numberOfPoints; i++) {
            indices.add(list.get(i));
        }
        return indices;
    }
}
