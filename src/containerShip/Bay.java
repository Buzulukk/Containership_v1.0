package containerShip;

import containers.Container;

import java.io.BufferedReader;
import java.io.IOException;

public class Bay {
    public int TEULimit;
    public int maxCargoWeight;
    public int[] tiers;
    public int rows;
    public boolean isConnectedToNext;
    public boolean isItGap;

    public Container[][] containers;
    public int currantWeight;

    public Bay(int TEULimit, int maxCargoWeight, int[] tiers, int rows, boolean isConnectedToNext, boolean isItGap) {
        this.TEULimit = TEULimit;
        this.maxCargoWeight = maxCargoWeight;
        this.tiers = tiers;
        this.rows = rows;
        this.isConnectedToNext = isConnectedToNext;
        this.isItGap = isItGap;
        this.containers = new Container[tiers[1] + 1][rows];
        this.currantWeight = 0;
    }

    public static Bay readNewBay(BufferedReader shipConfigFilePath) throws IOException {
        shipConfigFilePath.readLine();

        String s = shipConfigFilePath.readLine();
        if (s.equals("Gap")) {
            shipConfigFilePath.readLine();
            shipConfigFilePath.readLine();
            return new Bay(0, 0, new int[]{0, 0}, 0, false, true);
        }

        int TEULimit = Integer.parseInt(s.substring(11));
        int maxCargoWeight = Integer.parseInt(shipConfigFilePath.readLine().substring(18));

        s = shipConfigFilePath.readLine().substring(8);
        int[] tiers = {Integer.parseInt(s.substring(0, s.indexOf(' '))),
                Integer.parseInt(s.substring(s.indexOf('-') + 2, s.length() - 1))};

        int rows = Integer.parseInt(shipConfigFilePath.readLine().substring(6));

        shipConfigFilePath.readLine();
        boolean isConnectedToNext = shipConfigFilePath.readLine().equals("connected");

        return new Bay(TEULimit, maxCargoWeight, tiers, rows, isConnectedToNext, false);
    }
}
