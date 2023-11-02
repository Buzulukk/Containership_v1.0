package containers;

import containers.flammableContainer.FlammableContainer20ft;
import containers.flammableContainer.FlammableContainer40ft;
import containers.openTopContainer.OpenTopContainer20ft;
import containers.openTopContainer.OpenTopContainer40ft;
import containers.reefer.Reefer20ft;
import containers.reefer.Reefer40ft;
import containers.standardContainer.StandardContainer20ft;
import containers.standardContainer.StandardContainer40ft;
import containers.tankContainer.TankContainer20ft;
import containers.tankContainer.TankContainer40ft;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import static java.lang.System.exit;

public class ListOfContainer {

    public static void getAmountOfContainers() {
        String amountOfContainers_str = JOptionPane.showInputDialog("Input the amount of containers to generate:");

        try {
            int amountOfContainers = Integer.parseInt(amountOfContainers_str);
            if (amountOfContainers > 15000) {
                throw new NumberFormatException();
            }
            generateListOfContainer(amountOfContainers);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Incorrect input", "Error", JOptionPane.ERROR_MESSAGE);
            exit(0);
        }
    }

    private static void generateListOfContainer(int amountOfContainers) {
        try {
            FileWriter file = new FileWriter("ListOfContainers.txt");
            Random random = new Random();

            file.write("Amount Of Containers: " + String.valueOf(amountOfContainers));
            file.write("\n\n");

            for (; amountOfContainers != 0; --amountOfContainers) {

                int typeOfContainer = random.nextInt(100);

                //20ft - 75% | 40ft - 25%
                boolean size = !(random.nextBoolean() && random.nextBoolean()); //hah

                if (typeOfContainer < 50) {
                    //Standard Container - 50%
                    file.write(size ? StandardContainer20ft.getRandom().getText() : StandardContainer40ft.getRandom().getText());
                } else if (typeOfContainer < 75) {
                    //Reefer - 25%
                    file.write(size ? Reefer20ft.getRandom().getText() : Reefer40ft.getRandom().getText());
                } else if (typeOfContainer < 90) {
                    //Tank Container - 15%
                    file.write(size ? TankContainer20ft.getRandom().getText() : TankContainer40ft.getRandom().getText());
                } else if (typeOfContainer < 95) {
                    //Open Top Container - 5%
                    file.write(size ? OpenTopContainer20ft.getRandom().getText() : OpenTopContainer40ft.getRandom().getText());
                } else {
                    //Flammable Container - 5%
                    file.write(size ? FlammableContainer20ft.getRandom().getText() : FlammableContainer40ft.getRandom().getText());
                }
            }

            JOptionPane.showMessageDialog(null, "List was successfully created", "", JOptionPane.INFORMATION_MESSAGE);
            file.close();
            exit(0);
        } catch (IOException e) {
            System.out.println("Problem with file");
        }
    }

    public static Container[] readListOfContainer(BufferedReader listOfContainersFile) throws IOException {

        int amountOfContainers = Integer.parseInt(listOfContainersFile.readLine().substring(22));

        Container[] listOfContainers = new Container[amountOfContainers];

        listOfContainersFile.readLine();

        for (int i = 0; i != amountOfContainers; ++i) {
            listOfContainersFile.readLine();

            String name = listOfContainersFile.readLine().substring(6);
            boolean isEmpty = listOfContainersFile.readLine().substring(9).equals("true");
            int cargoWeight = Integer.parseInt(listOfContainersFile.readLine().substring(13));
            char grade = listOfContainersFile.readLine().charAt(7);

            switch (name) {
                case "Standard Container 20 ft" -> listOfContainers[i] = new StandardContainer20ft(isEmpty, cargoWeight, grade);
                case "Standard Container 40 ft" -> listOfContainers[i] = new StandardContainer40ft(isEmpty, cargoWeight, grade);
                case "Reefer 20 ft" -> listOfContainers[i] = new Reefer20ft(isEmpty, cargoWeight, grade, listOfContainersFile.readLine().substring(7).equals("true"), Integer.parseInt(listOfContainersFile.readLine().substring(13)));
                case "Reefer 40 ft" -> listOfContainers[i] = new Reefer40ft(isEmpty, cargoWeight, grade, listOfContainersFile.readLine().substring(7).equals("true"), Integer.parseInt(listOfContainersFile.readLine().substring(13)));
                case "Tank Container 20 ft" -> listOfContainers[i] = new TankContainer20ft(isEmpty, cargoWeight, grade, listOfContainersFile.readLine().substring(9).equals("true"));
                case "Tank Container 40 ft" -> listOfContainers[i] = new TankContainer40ft(isEmpty, cargoWeight, grade, listOfContainersFile.readLine().substring(9).equals("true"));
                case "Flammable Container 20 ft" -> listOfContainers[i] = new FlammableContainer20ft(isEmpty, cargoWeight, grade);
                case "Flammable Container 40 ft" -> listOfContainers[i] = new FlammableContainer40ft(isEmpty, cargoWeight, grade);
                case "Open Top Container 20 ft" -> listOfContainers[i] = new OpenTopContainer20ft(isEmpty, cargoWeight, grade, Integer.parseInt(listOfContainersFile.readLine().substring(8)));
                case "Open Top Container 40 ft" -> listOfContainers[i] = new OpenTopContainer40ft(isEmpty, cargoWeight, grade, Integer.parseInt(listOfContainersFile.readLine().substring(8)));
            }

            listOfContainersFile.readLine();
            listOfContainersFile.readLine();
        }

        listOfContainersFile.close();
        return listOfContainers;
    }
}

