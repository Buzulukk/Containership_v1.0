package containers.flammableContainer;

import containers.Container;

import java.awt.*;
import java.util.Random;

public class FlammableContainer40ft extends Container {

    public FlammableContainer40ft(boolean isEmpty, int cargoWeight, char grade) {
        super("Flammable Container 40 ft", 40, 8, 2560, 3900,
                27600, new Color(0xB80000), isEmpty, cargoWeight, grade);
    }


    public static FlammableContainer40ft getRandom() {
        Random random = new Random();

        /*~1/4 of containers on the ship is empty*/
        boolean isEmpty = random.nextInt(4) == 3;

        /*we will random values from 1000kg to 27000kg (max)*/
        int cargoWeight = isEmpty ? 0 : 1000 + random.nextInt(27) * 1000;

        /*15% - Grade A, 65% - Grade B, 20% - Grade C     info from the internet*/
        int rand_grade = random.nextInt(100);
        char grade = rand_grade < 15 ? 'A' : rand_grade < 80 ? 'B' : 'C';

        return new FlammableContainer40ft(isEmpty, cargoWeight, grade);
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
