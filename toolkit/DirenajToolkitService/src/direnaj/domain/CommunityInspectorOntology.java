package direnaj.domain;

import java.util.Vector;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

import direnaj.functionalities.semantic.CommunityInspectorOntologyHandler;
import direnaj.functionalities.sna.communityDetection.DetectedCommunities;

public class CommunityInspectorOntology {

    private OntModel ontModel;

    public CommunityInspectorOntology() {
       ontModel = CommunityInspectorOntologyHandler.loadModel(ontModel);
    }

    public OntModel getOntModel() {
        return ontModel;
    }

    public void addDetectedCommunities(DetectedCommunities communities) {
        Vector<Community> detectedCommunties = communities.getDetectedCommunties(false);
        for (Community community : detectedCommunties) {
            CommunityInspectorOntologyHandler.addCommunityBelongings(ontModel, community);
        }
        ontModel.write(System.out);
    }
    
    public void calculateTopicSimilarities(){
        ExtendedIterator<Individual> listIndividuals = ontModel.listIndividuals(CommunityInspectorOntologyVocabulary.TOPIC_RSC);
        for(Individual indv : listIndividuals.toList()){
            indv.toString();
        }
    }

}
