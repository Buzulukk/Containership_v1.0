package distribution;

import containers.Container;

import java.util.Comparator;

public class ContainersWeightComparator implements Comparator<Container> {
    @Override
    public int compare(Container cont1, Container cont2) {
        return Integer.compare(cont1.sumWeight, cont2.sumWeight);
    }
}
