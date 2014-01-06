package direnaj.functionalities.semantic;

import java.util.List;
import java.util.Vector;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import direnaj.domain.Community;
import direnaj.domain.CommunityInspectorOntologyVocabulary;
import direnaj.domain.User;

public class CommunityInspectorOntologyHandler {
    private static String ontologySchemaFileUrl = "file:ontologyFiles/mergedCommunityInspectorSchema.owl";

    public static void loadModel(OntModel ontModel) {
        ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, null);
        ontModel.read(ontologySchemaFileUrl, "RDF/XML");
    }

    public static void addCommunityBelongings(OntModel ontModel, Community community) {
        // FIXME burada individual'larý daha da detaylandýrmamýz gerekir
        // FIXME user node'una User detayýný ekle
        // FIXME post node'una post detayýný ekle
        Individual communityIndv = ontModel.createIndividual(
                CommunityInspectorOntologyVocabulary.getIndividualURI(community.getCommunityName(), false),
                CommunityInspectorOntologyVocabulary.COMMUNITY_RSC);
        Vector<User> usersInCommunity = community.getUsersInCommunity();
        for (User user : usersInCommunity) {
            Individual userIndv = ontModel.createIndividual(
                    CommunityInspectorOntologyVocabulary.getIndividualURI(user.getUserScreenName(), false),
                    CommunityInspectorOntologyVocabulary.USER_ACCOUNT_RSC);
            communityIndv.addProperty(CommunityInspectorOntologyVocabulary.BELONGS_TO_PROP, userIndv);
            List<String> posts = user.getPosts();
            for (String tweet : posts) {
                Individual postIndv = ontModel.createIndividual(
                        CommunityInspectorOntologyVocabulary.getIndividualURI("POST", true),
                        CommunityInspectorOntologyVocabulary.POST_RSC);
                communityIndv.addProperty(CommunityInspectorOntologyVocabulary.BELONGS_TO_PROP, postIndv);
            }
        }

    }

}
