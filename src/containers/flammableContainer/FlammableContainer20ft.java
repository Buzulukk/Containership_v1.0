package containers.flammableContainer;

import containers.Container;

import java.awt.*;
import java.util.Random;

public class FlammableContainer20ft extends Container {

    public FlammableContainer20ft(boolean isEmpty, int cargoWeight, char grade) {
        super("Flammable Container 20 ft", 20, 8, 1280, 2300,
                25000, new Color(0xEB0000), isEmpty, cargoWeight, grade);
    }


    public static FlammableContainer20ft getRandom() {
        Random random = new Random();

        /*~1/4 of containers on the ship is empty*/
        boolean isEmpty = random.nextInt(4) == 3;

        /*we will random values from 1000kg to 25000kg (max)*/
        int cargoWeight = isEmpty ? 0 : 1000 + random.nextInt(25) * 1000;

        /*15% - Grade A, 65% - Grade B, 20% - Grade C     info from the internet*/
        int rand_grade = random.nextInt(100);
        char grade = rand_grade < 15 ? 'A' : rand_grade < 80 ? 'B' : 'C';

        return new FlammableContainer20ft(isEmpty, cargoWeight, grade);
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
