package containerShip;

import java.io.BufferedReader;
import java.io.IOException;

public class ContainerShip {
    public int TEULimit;
    public int maxCargoWeight;
    public int maxAmountOfReefers;
    public int[] tiersWithPowerSupply;
    public int[] tiersUnderMainDeck;
    public int baysAmount;

    public Bay[] bays;
    public int maxRowsAmount = 0;
    public int maxTiersAmount = 0;

    public ContainerShip(int TEULimit, int maxCargoWeight, int maxAmountOfReefers,
                         int[] tiersWithPowerSupply, int[] tiersUnderMainDeck, int baysAmount) {
        this.TEULimit = TEULimit;
        this.maxCargoWeight = maxCargoWeight;
        this.maxAmountOfReefers = maxAmountOfReefers;
        this.tiersWithPowerSupply = tiersWithPowerSupply;
        this.tiersUnderMainDeck = tiersUnderMainDeck;
        this.baysAmount = baysAmount;
        this.bays = new Bay[this.baysAmount];
    }

    public static ContainerShip readShip(BufferedReader shipConfigFilePath) throws IOException {

        int TEULimit = Integer.parseInt(shipConfigFilePath.readLine().substring(11));
        int maxCargoWeight = Integer.parseInt(shipConfigFilePath.readLine().substring(18));
        int maxAmountOfReefers = Integer.parseInt(shipConfigFilePath.readLine().substring(23));

        String s = shipConfigFilePath.readLine().substring(26);
        int[] tiersWithPowerSupply = {Integer.parseInt(s.substring(0, s.indexOf(' '))),
                Integer.parseInt(s.substring(s.indexOf('-') + 2, s.length() - 1))};

        s = shipConfigFilePath.readLine().substring(24);
        int[] tiersUnderMainDeck = {Integer.parseInt(s.substring(0, s.indexOf(' '))),
                Integer.parseInt(s.substring(s.indexOf('-') + 2, s.length() - 1))};

        int baysAmount = Integer.parseInt(shipConfigFilePath.readLine().substring(13));

        shipConfigFilePath.readLine();
        shipConfigFilePath.readLine();
        shipConfigFilePath.readLine();

        ContainerShip containerShip = new ContainerShip(TEULimit, maxCargoWeight, maxAmountOfReefers,
                tiersWithPowerSupply, tiersUnderMainDeck, baysAmount);

        for (int i = 0; i != containerShip.baysAmount; ++i) {
            containerShip.bays[i] = Bay.readNewBay(shipConfigFilePath);
            if (containerShip.bays[i].rows > containerShip.maxRowsAmount) {
                containerShip.maxRowsAmount = containerShip.bays[i].rows;
            }
            if (containerShip.bays[i].tiers[1] > containerShip.maxTiersAmount) {
                containerShip.maxTiersAmount = containerShip.bays[i].tiers[1];
            }
        }

        shipConfigFilePath.close();
        return containerShip;
    }
}
