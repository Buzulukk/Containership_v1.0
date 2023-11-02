package containers.tankContainer;

import containers.Container;

import java.awt.*;
import java.util.Random;

public class TankContainer20ft extends Container {

    public boolean isItGas;

    public TankContainer20ft(boolean isEmpty, int cargoWeight, char grade, boolean isItGas) {
        super("Tank Container 20 ft", 20, 8, 21000, 6000,
                27700, new Color(0xF78FA7), isEmpty, cargoWeight, grade);
        this.isItGas = isItGas;
    }


    public static TankContainer20ft getRandom() {
        Random random = new Random();

        /*~1/4 of containers on the ship is empty*/
        boolean isEmpty = random.nextInt(4) == 3;

        /*we will random values from 1000kg to 27000kg (max)*/
        int cargoWeight = isEmpty ? 0 : 1000 + random.nextInt(27) * 1000;

        /*15% - Grade A, 65% - Grade B, 20% - Grade C     info from the internet*/
        int rand_grade = random.nextInt(100);
        char grade = rand_grade < 15 ? 'A' : rand_grade < 80 ? 'B' : 'C';

        /*let it be 1/3 gas*/
        boolean isItGas = random.nextInt(3) == 2;

        return new TankContainer20ft(isEmpty, cargoWeight, grade, isItGas);
    }

    public String getText() {
        return "{\n" +
                "name: " + this.name + "\n" +
                "isEmpty: " + this.isEmpty + "\n" +
                "cargoWeight: " + this.cargoWeight + "\n" +
                "grade: " + this.grade + "\n" +
                "isItGas: " + this.isItGas + "\n" +
                "}\n\n";
    }
}
