package containers.openTopContainer;

import containers.Container;

import java.awt.*;
import java.util.Random;

public class OpenTopContainer20ft extends Container {

    public OpenTopContainer20ft(boolean isEmpty, int cargoWeight, char grade, int height) {
        super("Open Top Container 20 ft", 20, height, 20 * 8 * height, 2250,
                28220, new Color(0x0A5F38), isEmpty, cargoWeight, grade);
    }

    public static OpenTopContainer20ft getRandom() {
        Random random = new Random();

        /*~1/4 of containers on the ship is empty*/
        boolean isEmpty = random.nextInt(4) == 3;

        /*height will be from 9 ft - 16 ft*/
        int height = isEmpty ? 0 : 9 + random.nextInt(8);

        /*we will random values from 1000kg to 28000kg (max)*/
        int cargoWeight = isEmpty ? 0 : 1000 + random.nextInt(28) * 1000;

        /*15% - Grade A, 65% - Grade B, 20% - Grade C     info from the internet*/
        int rand_grade = random.nextInt(100);
        char grade = rand_grade < 15 ? 'A' : rand_grade < 80 ? 'B' : 'C';

        return new OpenTopContainer20ft(isEmpty, cargoWeight, grade, height);
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
