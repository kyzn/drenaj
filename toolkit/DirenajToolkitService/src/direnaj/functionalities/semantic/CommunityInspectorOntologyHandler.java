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
    private static String ontologySchemaFileUrl = "mergedCommunityInspectorSchema.owl";

    public static OntModel loadModel(OntModel ontModel) {
        ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, null);
        ontModel.read(ontologySchemaFileUrl, "RDF/XML");
        return ontModel;
    }

    public static void addCommunityBelongings(OntModel ontModel, Community community) {
        // create Community Individual
        Individual communityIndv = ontModel.createIndividual(
                CommunityInspectorOntologyVocabulary.getIndividualURI(community.getCommunityName(), false),
                CommunityInspectorOntologyVocabulary.COMMUNITY_RSC);
        communityIndv.addProperty(CommunityInspectorOntologyVocabulary.SIOC_ID_PROP, community.getCommunityName());
        // get all users in community
        Vector<User> usersInCommunity = community.getUsersInCommunity();
        for (User user : usersInCommunity) {
            // create user Individual
            Individual userIndv = ontModel.createIndividual(
                    CommunityInspectorOntologyVocabulary.getIndividualURI(user.getUserScreenName(), false),
                    CommunityInspectorOntologyVocabulary.USER_ACCOUNT_RSC);
            userIndv.addProperty(CommunityInspectorOntologyVocabulary.SIOC_NAME_PROP, user.getUserScreenName());
            userIndv.addProperty(CommunityInspectorOntologyVocabulary.SIOC_ID_PROP, user.getUserId());
            communityIndv.addProperty(CommunityInspectorOntologyVocabulary.BELONGS_TO_PROP, userIndv);
            List<String> posts = user.getPosts();
            for (String tweet : posts) {
                // create post individuals
                Individual postIndv = ontModel.createIndividual(
                        CommunityInspectorOntologyVocabulary.getIndividualURI("POST", true),
                        CommunityInspectorOntologyVocabulary.POST_RSC);
                postIndv.addProperty(CommunityInspectorOntologyVocabulary.CONTENT_PROP, tweet);
                communityIndv.addProperty(CommunityInspectorOntologyVocabulary.BELONGS_TO_PROP, postIndv);
            }
        }

    }

}
