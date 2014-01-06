package direnaj.domain;

import java.util.Vector;

import com.hp.hpl.jena.ontology.OntModel;

import direnaj.functionalities.semantic.CommunityInspectorOntologyHandler;
import direnaj.functionalities.sna.communityDetection.DetectedCommunities;

public class CommunityInspectorOntology {

    private OntModel ontModel;

    public CommunityInspectorOntology() {
        CommunityInspectorOntologyHandler.loadModel(ontModel);
    }

    public OntModel getOntModel() {
        return ontModel;
    }

    public void addDetectedCommunities(DetectedCommunities communities) {
        Vector<Community> detectedCommunties = communities.getDetectedCommunties(false);
        for (Community community : detectedCommunties) {
            CommunityInspectorOntologyHandler.addCommunityBelongings(ontModel, community);
        }
    }

}
