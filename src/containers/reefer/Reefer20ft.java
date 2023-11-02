package containers.reefer;

import containers.Container;

import java.awt.*;
import java.util.Random;

public class Reefer20ft extends Container {

    public boolean isNOR;
    public int temperature;

    public Reefer20ft(boolean isEmpty, int cargoWeight, char grade, boolean isNOR, int temperature) {
        super("Reefer 20 ft", 20, 8, 993, 3080,
                27700, new Color(0xCBE6EF), isEmpty, cargoWeight, grade);
        this.isNOR = isNOR;
        this.temperature = temperature;
    }

    public static Reefer20ft getRandom() {
        Random random = new Random();

        /*~1/4 of containers on the ship is empty*/
        boolean isEmpty = random.nextInt(4) == 3;

        /*we will random values from 1000kg to 27000kg (max)*/
        int cargoWeight = isEmpty ? 0 : 1000 + random.nextInt(27) * 1000;

        /*15% - Grade A, 65% - Grade B, 20% - Grade C     info from the internet*/
        int rand_grade = random.nextInt(100);
        char grade = rand_grade < 15 ? 'A' : rand_grade < 80 ? 'B' : 'C';

        /*idk, but let it be 1/5*/
        boolean isNOR = random.nextInt(5) == 4;

        /*from -30 to +30*/
        int temperature = random.nextInt(61) - 30;

        return new Reefer20ft(isEmpty, cargoWeight, grade, isNOR, temperature);
    }

    public String getText() {
        return "{\n" +
                "name: " + this.name + "\n" +
                "isEmpty: " + this.isEmpty + "\n" +
                "cargoWeight: " + this.cargoWeight + "\n" +
                "grade: " + this.grade + "\n" +
                "isNOR: " + this.isNOR + "\n" +
                "temperature: " + this.temperature + "\n" +
                "}\n\n";
    }
}
