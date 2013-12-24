package direnaj.functionalities.graph;

public enum Relations {
    Mentions, Replies, Retweets;

    public static Relations fromString(String textValue) {
        Relations[] relations = Relations.values();
        for (int i = 0; i < relations.length; i++) {
            if (relations[i].name().equals(textValue)) {
                return relations[i];
            }
        }
        return null;
    }
}
