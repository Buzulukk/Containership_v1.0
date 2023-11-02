package containers.standardContainer;

import containers.Container;

import java.awt.*;
import java.util.Random;

public class StandardContainer20ft extends Container {

    public StandardContainer20ft(boolean isEmpty, int cargoWeight, char grade) {
        super("Standard Container 20 ft", 20, 8, 1280, 2300,
                25000, new Color(0x1560BD), isEmpty, cargoWeight, grade);
    }


    public static StandardContainer20ft getRandom() {
        Random random = new Random();

        /*~1/4 of containers on the ship is empty*/
        boolean isEmpty = random.nextInt(4) == 3;

        /*we will random values from 1000kg to 25000kg (max)*/
        int cargoWeight = isEmpty ? 0 : 1000 + random.nextInt(25) * 1000;

        /*15% - Grade A, 65% - Grade B, 20% - Grade C     info from the internet*/
        int rand_grade = random.nextInt(100);
        char grade = rand_grade < 15 ? 'A' : rand_grade < 80 ? 'B' : 'C';

        return new StandardContainer20ft(isEmpty, cargoWeight, grade);
    }

    public String getText() {
        return "{\n" +
                "name: " + this.name + "\n" +
                "isEmpty: " + this.isEmpty + "\n" +
                "cargoWeight: " + this.cargoWeight + "\n" +
                "grade: " + this.grade + "\n" +
                "}\n\n";
    }
}
