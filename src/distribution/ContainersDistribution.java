package distribution;

import containerShip.ContainerShip;
import containers.Container;
import containers.reefer.Reefer20ft;
import containers.reefer.Reefer40ft;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class ContainersDistribution {

    public static ContainerShip containerShip;
    public static int TEUAmount = 0;
    public static int sumCargoWeight = 0;
    public static int reefersAmount = 0;
    public static FileWriter manifestFile;

    static {
        try {
            manifestFile = new FileWriter("temp.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static ArrayList<Container> standardContainers20ft = new ArrayList<>();
    public static ArrayList<Container> standardContainers40ft = new ArrayList<>();
    public static ArrayList<Container> reefers20ft = new ArrayList<>();
    public static ArrayList<Container> reefers40ft = new ArrayList<>();
    public static ArrayList<Container> openTopContainers20ft = new ArrayList<>();
    public static ArrayList<Container> openTopContainers40ft = new ArrayList<>();
    public static ArrayList<Container> flammableContainers20ft = new ArrayList<>();
    public static ArrayList<Container> flammableContainers40ft = new ArrayList<>();


    public static void writeManifest(Container container, int bay, int tier, int row) {
        try {
            String s;
            if (container.length == 20) {
                s = "bay: " + String.valueOf(bay + 1) + '\n';
                ++TEUAmount;
            } else {
                s = "bay: [ " + String.valueOf(bay + 1) + " - " + String.valueOf(bay + 2) + " ]\n";
                TEUAmount += 2;
            }
            s += "tier: " + String.valueOf(tier) + '\n' +
                    "row: " + String.valueOf(row + 1) + '\n'
                    + container.getText();
            sumCargoWeight += container.sumWeight;
            manifestFile.write(s);
            manifestFile.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void tempToManifest() {
        try {
            BufferedReader tempFile = new BufferedReader(new FileReader("temp.txt"));
            FileWriter manifest = new FileWriter("manifest.txt");
            manifest.write("Loaded TEU Amount: " + String.valueOf(TEUAmount) + "\n");
            manifest.write("Sum Cargo Weight: " + String.valueOf(sumCargoWeight) + "\n\n");
            String s = tempFile.readLine();
            while (s != null) {
                manifest.write(s + '\n');

                s = tempFile.readLine();
            }
            manifest.flush();
            tempFile.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void typedLists(Container[] listOfContainer) {

        for (Container cont : listOfContainer) {
            switch (cont.name) {
                case "Standard Container 20 ft", "Tank Container 20 ft" -> standardContainers20ft.add(cont);
                case "Standard Container 40 ft", "Tank Container 40 ft" -> standardContainers40ft.add(cont);
                case "Open Top Container 20 ft" -> openTopContainers20ft.add(cont);
                case "Open Top Container 40 ft" -> openTopContainers40ft.add(cont);
                case "Reefer 20 ft" -> {
                    if (((Reefer20ft) cont).isNOR) {
                        standardContainers20ft.add(cont);
                    } else {
                        reefers20ft.add(cont);
                    }
                }
                case "Reefer 40 ft" -> {
                    if (((Reefer40ft) cont).isNOR) {
                        standardContainers40ft.add(cont);
                    } else {
                        reefers40ft.add(cont);
                    }
                }
                case "Flammable Container 20 ft" -> {
                    if (cont.isEmpty) {
                        standardContainers20ft.add(cont);
                    } else {
                        flammableContainers20ft.add(cont);
                    }
                }
                case "Flammable Container 40 ft" -> {
                    if (cont.isEmpty) {
                        standardContainers40ft.add(cont);
                    } else {
                        flammableContainers40ft.add(cont);
                    }
                }
            }
        }

        //sorting
        standardContainers20ft.sort(new ContainersWeightComparator());
        standardContainers40ft.sort(new ContainersWeightComparator());
        reefers20ft.sort(new ContainersWeightComparator());
        reefers40ft.sort(new ContainersWeightComparator());
        openTopContainers20ft.sort(new ContainersWeightComparator());
        openTopContainers40ft.sort(new ContainersWeightComparator());
        flammableContainers20ft.sort(new ContainersWeightComparator());
        flammableContainers40ft.sort(new ContainersWeightComparator());
    }

    public static void distributeContainers(ContainerShip containerShip_run, Container[] listOfContainer) {
        containerShip = containerShip_run;

        typedLists(listOfContainer);

        fillNotConnectedBays();

        fillConnectedBays();

        fixingGaps();

        tempToManifest();
    }

    public static boolean checkWeight(int mode, int bay, Container container1, Container container2) {
        return switch (mode) {
            //for not connected
            case 0 ->
                    (containerShip.bays[bay].currantWeight + container1.sumWeight <= containerShip.bays[bay].maxCargoWeight) &&
                            (sumCargoWeight + container1.sumWeight <= containerShip.maxCargoWeight);
            //for connected 2 * 20ft
            case 1 ->
                    (containerShip.bays[bay].currantWeight + container1.sumWeight <= containerShip.bays[bay].maxCargoWeight) &&
                            (containerShip.bays[bay + 1].currantWeight + container2.sumWeight <= containerShip.bays[bay + 1].maxCargoWeight) &&
                            (sumCargoWeight + (container1.sumWeight + container2.sumWeight) <= containerShip.maxCargoWeight);
            //for connected 1 * 40ft
            case 2 ->
                    (containerShip.bays[bay].currantWeight + container1.sumWeight / 2 <= containerShip.bays[bay].maxCargoWeight) &&
                            (containerShip.bays[bay + 1].currantWeight + container1.sumWeight / 2 <= containerShip.bays[bay + 1].maxCargoWeight) &&
                            (sumCargoWeight + container1.sumWeight <= containerShip.maxCargoWeight);
            default -> throw new IllegalStateException("Unexpected value: " + mode);
        };
    }


    private static void addContainerToManifest(int mode, int bay, int tier, int row, ArrayList<Container> list) {
        if (Objects.equals(list.get(0).name, "Reefer 20 ft") || Objects.equals(list.get(0).name, "Reefer 40 ft")) {
            ++reefersAmount;
        }
        switch (mode) {
            //for not connected
            case 0 -> {
                containerShip.bays[bay].containers[tier][row] = list.get(0);
                containerShip.bays[bay].currantWeight += list.get(0).sumWeight;
                writeManifest(list.get(0), bay, tier, row);
                list.remove(0);
            }
            //for connected 2 * 20ft
            case 1 -> {
                addContainerToManifest(0, bay, tier, row, list);
                addContainerToManifest(0, bay + 1, tier, row, list);
            }
            //for connected 1 * 40ft
            case 2 -> {
                containerShip.bays[bay].containers[tier][row] = list.get(0);
                containerShip.bays[bay + 1].containers[tier][row] = list.get(0);
                containerShip.bays[bay].currantWeight += list.get(0).sumWeight / 2;
                containerShip.bays[bay + 1].currantWeight += list.get(0).sumWeight / 2;
                writeManifest(list.get(0), bay, tier, row);
                list.remove(0);
            }
        }
    }

    public static void fillNotConnectedBays() {
        for (int bay = 0; bay < containerShip.baysAmount; ++bay) {
            if (containerShip.bays[bay].isConnectedToNext || containerShip.bays[bay == 0 ? 0 : bay - 1].isConnectedToNext) {
                continue;
            }

            for (int tier = containerShip.bays[bay].tiers[0]; tier <= containerShip.bays[bay].tiers[1]; ++tier) {
                for (int row = 0; row < containerShip.bays[bay].rows; ++row) {
                    if ((tier == containerShip.bays[bay].tiers[1]) && (row == 0 || row == containerShip.bays[bay].rows - 1) && !(flammableContainers20ft.isEmpty()) && checkWeight(0, bay, flammableContainers20ft.get(0), null)) {
                        addContainerToManifest(0, bay, tier, row, flammableContainers20ft);
                    } else if ((tier == containerShip.bays[bay].tiers[1]) && !(openTopContainers20ft.isEmpty()) && checkWeight(0, bay, openTopContainers20ft.get(0), null)) {
                        addContainerToManifest(0, bay, tier, row, openTopContainers20ft);
                    } else if ((tier >= containerShip.tiersWithPowerSupply[0] && tier <= containerShip.tiersWithPowerSupply[1]) && (reefersAmount + 1 <= containerShip.maxAmountOfReefers) &&
                            !(reefers20ft.isEmpty()) && checkWeight(0, bay, reefers20ft.get(0), null)) {
                        addContainerToManifest(0, bay, tier, row, reefers20ft);
                    } else if (!(standardContainers20ft.isEmpty()) && checkWeight(0, bay, standardContainers20ft.get(0), null)) {
                        addContainerToManifest(0, bay, tier, row, standardContainers20ft);
                    }
                }
            }
        }
    }

    public static void fillConnectedBays() {
        for (int bay = 0; bay < containerShip.baysAmount; ++bay) {
            if (!containerShip.bays[bay].isConnectedToNext) {
                continue;
            }

            for (int tier = containerShip.bays[bay].tiers[0]; tier <= containerShip.bays[bay].tiers[1]; ++tier) {
                for (int row = 0; row < containerShip.bays[bay].rows; ++row) {
                    if ((tier == containerShip.bays[bay].tiers[1]) && (row == 0 || row == containerShip.bays[bay].rows - 1) && flammableContainers20ft.size() >= 2 && (containerShip.bays[bay].containers[tier - 1][row] == null || containerShip.bays[bay].containers[tier - 1][row].length == 20) &&
                            checkWeight(1, bay, flammableContainers20ft.get(0), flammableContainers20ft.get(1))) {
                        addContainerToManifest(1, bay, tier, row, flammableContainers20ft);
                    } else if ((tier == containerShip.bays[bay].tiers[1]) && openTopContainers20ft.size() >= 2 && (containerShip.bays[bay].containers[tier - 1][row] == null || containerShip.bays[bay].containers[tier - 1][row].length == 20) &&
                            checkWeight(1, bay, openTopContainers20ft.get(0), openTopContainers20ft.get(1))) {
                        addContainerToManifest(1, bay, tier, row, openTopContainers20ft);
                    } else if ((tier >= containerShip.tiersWithPowerSupply[0] && tier <= containerShip.tiersWithPowerSupply[1]) && reefers20ft.size() >= 2 && (containerShip.bays[bay].containers[tier - 1][row] == null || containerShip.bays[bay].containers[tier - 1][row].length == 20) &&
                            checkWeight(1, bay, reefers20ft.get(0), reefers20ft.get(1)) &&
                            (reefersAmount + 1 <= containerShip.maxAmountOfReefers)) {
                        addContainerToManifest(1, bay, tier, row, reefers20ft);
                    } else if (standardContainers20ft.size() >= 2 && (containerShip.bays[bay].containers[tier - 1][row] == null || containerShip.bays[bay].containers[tier - 1][row].length == 20) &&
                            checkWeight(1, bay, standardContainers20ft.get(0), standardContainers20ft.get(1))) {
                        addContainerToManifest(1, bay, tier, row, standardContainers20ft);
                    } else if ((tier == containerShip.bays[bay].tiers[1]) && (row == 0 || row == containerShip.bays[bay].rows - 1) && !(flammableContainers40ft.isEmpty()) &&
                            checkWeight(2, bay, flammableContainers40ft.get(0), null)) {
                        addContainerToManifest(2, bay, tier, row, flammableContainers40ft);
                    } else if ((tier == containerShip.bays[bay].tiers[1]) && !(openTopContainers40ft.isEmpty()) &&
                            checkWeight(2, bay, openTopContainers40ft.get(0), null)) {
                        addContainerToManifest(2, bay, tier, row, openTopContainers40ft);
                    } else if ((tier >= containerShip.tiersWithPowerSupply[0] && tier <= containerShip.tiersWithPowerSupply[1]) && !(reefers40ft.isEmpty()) &&
                            checkWeight(2, bay, reefers40ft.get(0), null) &&
                            (reefersAmount + 1 <= containerShip.maxAmountOfReefers)) {
                        addContainerToManifest(2, bay, tier, row, reefers40ft);
                    } else if (!(standardContainers40ft.isEmpty()) &&
                            checkWeight(2, bay, standardContainers40ft.get(0), null)) {
                        addContainerToManifest(2, bay, tier, row, standardContainers40ft);
                    }
                }
            }
        }
    }

    public static void fixingGaps() {
        for (int bay = 0; bay < containerShip.baysAmount; ++bay) {
            for (int row = 0; row < containerShip.bays[bay].rows; ++row) {
                for (int tier = containerShip.bays[bay].tiers[1]; tier >= containerShip.bays[bay].tiers[0] + 1; --tier) {
                    if (containerShip.bays[bay].containers[tier][row] != null && containerShip.bays[bay].containers[tier - 1][row] == null) {
                        containerShip.bays[bay].containers[tier - 1][row] = containerShip.bays[bay].containers[tier][row];
                        containerShip.bays[bay].containers[tier][row] = null;
                    }
                }
            }
        }
    }
}
