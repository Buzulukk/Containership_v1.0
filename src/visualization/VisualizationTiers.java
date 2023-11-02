package visualization;

import containerShip.ContainerShip;
import containers.reefer.Reefer20ft;
import containers.reefer.Reefer40ft;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class VisualizationTiers implements ChangeListener {
    public static final int size = 80;
    public static final int containersSize = 5;
    static JFrame frame;
    static JSlider slider;
    static JButton changeModeButton;
    static ContainerShip containerShip;
    static int xContainerPosition;
    static int yContainerPosition;

    public void visualization(ContainerShip got_containerShip) {
        containerShip = got_containerShip;

        //frame
        frame = new JFrame("Visualization Tiers");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(16 * size, 9 * size);
        frame.getContentPane().setBackground(new Color(130, 130, 130));
        frame.setResizable(false);
        frame.setLayout(null);

        //slider
        slider = new JSlider(1, containerShip.maxTiersAmount, 1);
        slider.setPaintTrack(true);
        slider.setMajorTickSpacing(1);
        slider.setPaintLabels(true);
        slider.addChangeListener(this);
        slider.setBounds(0, 0, 16 * size - 15, 50);

        //change mode button
        changeModeButton = new JButton();
        changeModeButton.setText("Rows");
        changeModeButton.setFocusable(false);
        changeModeButton.addActionListener(e -> {
            frame.setVisible(false);
            frame.dispose();
            VisualizationRows visualizationRows = new VisualizationRows();
            visualizationRows.visualization(containerShip);
        });
        changeModeButton.setBounds(10, 9 * size - 75, 100, 30);

        visualize(1);
    }

    private static void visualize(int tier) {
        yContainerPosition = 100;
        frame.add(slider);
        frame.add(changeModeButton);
        for (int row = 0; row <= containerShip.maxRowsAmount; ++row) {
            xContainerPosition = 10;
            for (int bay = 0; bay < containerShip.baysAmount; ++bay) {
                if (containerShip.bays[bay].isItGap
                        || tier < containerShip.bays[bay].tiers[0] || tier > containerShip.bays[bay].tiers[1]
                        || containerShip.bays[bay].rows <= row) {
                    continue;
                }

                String name;
                Color color;
                if (containerShip.bays[bay].containers[tier][row] == null) {
                    name = "Empty";
                    color = Color.GRAY;
                } else {
                    name = switch (containerShip.bays[bay].containers[tier][row].name) {
                        case "Flammable Container 20 ft" -> "<html>Flammable<br>Container 20 ft</html>";
                        case "Flammable Container 40 ft" -> "Flammable Container 40 ft";
                        case "Open Top Container 20 ft" -> "<html>Open Top<br>Container 20ft</html>";
                        case "Open Top Container 40 ft" -> "Open Top Container 40 ft";
                        case "Reefer 20 ft" -> "Reefer 20 ft";
                        case "Reefer 40 ft" -> "Reefer 40 ft";
                        case "Standard Container 20 ft" -> "<html>Standard<br>Container 20 ft</html>";
                        case "Standard Container 40 ft" -> "Standard Container 40 ft";
                        case "Tank Container 20 ft" -> "<html>Tank<br>Container 20 ft</html>";
                        case "Tank Container 40 ft" -> "Tank Container 40 ft";
                        default -> throw new RuntimeException((Throwable) null);
                    };
                    if (containerShip.bays[bay].containers[tier][row].name.equals("Reefer 20 ft") && ((Reefer20ft)containerShip.bays[bay].containers[tier][row]).isNOR){
                        name = "NOR 20 ft";
                    } else if (containerShip.bays[bay].containers[tier][row].name.equals("Reefer 40 ft") && ((Reefer40ft)containerShip.bays[bay].containers[tier][row]).isNOR){
                        name = "NOR 40 ft";
                    }
                    color = containerShip.bays[bay].containers[tier][row].color;
                }

                boolean is40ft = containerShip.bays[bay].containers[tier][row] != null && containerShip.bays[bay].containers[tier][row].length == 40 && containerShip.bays[bay].isConnectedToNext;

                JLabel containerLabel = new JLabel(name, SwingConstants.CENTER);
                containerLabel.setFont(new Font(containerLabel.getFont().getName(), containerLabel.getFont().getStyle(), containerLabel.getFont().getSize() - 1));
                containerLabel.setBackground(color);
                containerLabel.setOpaque(true);
                containerLabel.setBounds(xContainerPosition + (17*containersSize+7)*bay, yContainerPosition + (row * 42), 17 * containersSize * (is40ft ? 2 : 1), 8 * containersSize);
                frame.add(containerLabel);

                if (containerShip.bays[bay].isConnectedToNext) {
                    xContainerPosition -= 7;
                }
            }
        }
        frame.setVisible(true);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        frame.getContentPane().removeAll();
        frame.repaint();
        visualize(slider.getValue());
    }
}
