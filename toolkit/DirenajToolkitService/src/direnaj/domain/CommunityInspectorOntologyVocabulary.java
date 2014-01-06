package direnaj.domain;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;

public class CommunityInspectorOntologyVocabulary {

    public static final String SIOC_BASE_URI = "http://rdfs.org/sioc/ns#";

    public static final String POC_BASE_URI = "http://www.cmpe.boun.edu.tr/soslab/ontologies/poc#";

    public static final String COMMUNITY_INSPECTOR_BASE_URI = "http://www.cmpe.boun.edu.tr/soslab/ontologies/communityInspector#";

    public static final String COMMUNITY_INSPECTOR_INDV_BASE_URI = "http://communityInspector.com#";

    public static final String COMMUNITY = SIOC_BASE_URI + "Community";

    public static final String USER_ACCOUNT = SIOC_BASE_URI + "UserAccount";

    public static final String POST = SIOC_BASE_URI + "Post";

    public static final String BELONGS_TO = POC_BASE_URI + "belongs_to";

    public static final Resource COMMUNITY_RSC = resource(COMMUNITY);

    public static final Resource USER_ACCOUNT_RSC = resource(USER_ACCOUNT);

    public static final Resource POST_RSC = resource(POST);

    public static final Property BELONGS_TO_PROP = property(BELONGS_TO);

    public static String getIndividualURI(String individualName, boolean addRandomNumber) {
        String uri = COMMUNITY_INSPECTOR_INDV_BASE_URI + individualName;
        if (addRandomNumber) {
            uri += Math.random();
        }
        return uri;
    }

    /**
     * creates {@link Property} according to parameter
     * 
     * @param uri
     *            given parameter for which property should be created
     * @return {@link Property} instance
     */
    private static Property property(String uri) {

        return ResourceFactory.createProperty(uri);
    }

    /**
     * creates resources for given parameters
     * 
     * @param uri
     *            uri of the wanted resource
     * @return {@link Resource} instance
     */
    private static Resource resource(String uri) {

        return ResourceFactory.createResource(uri);
    }

}
