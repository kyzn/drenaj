package direnaj.functionalities.sna.communityDetection;

import java.util.Vector;
import direnaj.domain.Community;

public class DetectedCommunities {

    private Vector<Community> detectedCommunties;
    private double modularity;

    public DetectedCommunities(double modularity) {
        detectedCommunties = new Vector<Community>();
        this.modularity = modularity;
    }

    public Vector<Community> getDetectedCommunties() {
        return detectedCommunties;
    }

    public double getModularity() {
        return modularity;
    }

}
