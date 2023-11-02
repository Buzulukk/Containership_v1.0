package containers.openTopContainer;

import containers.Container;

import java.awt.*;
import java.util.Random;

public class OpenTopContainer40ft extends Container {

    public OpenTopContainer40ft(boolean isEmpty, int cargoWeight, char grade, int height) {
        super("Open Top Container 40 ft", 40, height, 40 * 8 * height, 3800,
                26500, new Color(0x0B472B), isEmpty, cargoWeight, grade);
    }

    public static OpenTopContainer40ft getRandom() {
        Random random = new Random();

        /*~1/4 of containers on the ship is empty*/
        boolean isEmpty = random.nextInt(4) == 3;

        /*height will be from 9 ft - 16 ft*/
        int height = isEmpty ? 0 : 9 + random.nextInt(8);

        /*we will random values from 1000kg to 26000kg (max)*/
        int cargoWeight = isEmpty ? 0 : 1000 + random.nextInt(26) * 1000;

        /*15% - Grade A, 65% - Grade B, 20% - Grade C     info from the internet*/
        int rand_grade = random.nextInt(100);
        char grade = rand_grade < 15 ? 'A' : rand_grade < 80 ? 'B' : 'C';

        return new OpenTopContainer40ft(isEmpty, cargoWeight, grade, height);
    }

    public String getText() {
        return "{\n" +
                "name: " + this.name + "\n" +
                "isEmpty: " + this.isEmpty + "\n" +
                "cargoWeight: " + this.cargoWeight + "\n" +
                "grade: " + this.grade + "\n" +
                "height: " + this.height + "\n" +
                "}\n\n";
    }
}
