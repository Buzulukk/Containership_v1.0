import containerShip.ContainerShip;
import containers.ListOfContainer;
import distribution.ContainersDistribution;
import visualization.VisualizationRows;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static java.lang.System.exit;

public class Main {
    public static void main(String[] args) {

        int mode = JOptionPane.showOptionDialog(null, "Choose the mode: ", "Work Mode",
                0, 3, null, new String[]{"Generate list of containers", "Create a manifest file"}, 0);

        switch (mode) {
            case -1 -> exit(0);
            case 0 -> ListOfContainer.getAmountOfContainers();
        }

        try {
            String shipConfigFilePath = JOptionPane.showInputDialog("Input the ship config file path: ");
            BufferedReader shipConfigFile = new BufferedReader(new FileReader(shipConfigFilePath));

            String listOfContainersFilePath = JOptionPane.showInputDialog("Input the list of containers file path:");
            BufferedReader listOfContainersFile = new BufferedReader(new FileReader(listOfContainersFilePath));

            ContainerShip containerShip = ContainerShip.readShip(shipConfigFile);

            ContainersDistribution.distributeContainers(containerShip, ListOfContainer.readListOfContainer(listOfContainersFile));

            mode = JOptionPane.showOptionDialog(null, "Manifest file was successfully created.\n\nStart visualisation?", "Visualisation",
                    0, 3, null, new String[]{"Start", "Close"}, 0);

            if (mode == 0) {
                VisualizationRows visualizationRows = new VisualizationRows();
                visualizationRows.visualization(containerShip);
            } else {
                exit(0);
            }

        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Incorrect file path", "Error", JOptionPane.ERROR_MESSAGE);
            exit(0);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Incorrect data in file", "Error", JOptionPane.ERROR_MESSAGE);
            exit(0);
        }
    }
}
