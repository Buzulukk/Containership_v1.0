package containers;

import java.awt.*;

public abstract class Container {
    public String name;
    public int length;
    public final int width = 8;
    public int height;
    public int capacity;
    public int tareWeight;
    public int payloadCapacity;
    public Color color;
    public boolean isEmpty;
    public int cargoWeight;
    public int sumWeight;
    public char grade;

    public Container(String name, int length, int height, int capacity, int tareWeight, int payloadCapacity,
                     Color color, boolean isEmpty, int cargoWeight, char grade) {
        this.name = name;
        this.length = length;
        this.height = height;
        this.capacity = capacity;
        this.tareWeight = tareWeight;
        this.payloadCapacity = payloadCapacity;
        this.color = color;
        this.isEmpty = isEmpty;
        this.cargoWeight = cargoWeight;
        this.sumWeight = this.tareWeight + this.cargoWeight;
        this.grade = grade;
    }

    public static Container getRandom() {
        return null;
    }

    public String getText() {
        return "";
    }
}
