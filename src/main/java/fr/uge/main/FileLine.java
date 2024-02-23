package fr.uge.main;

/**
 * Enumération des lignes de fichier, pour une meilleure lisibilité
 * @see FileLine
 * lang = fr
 * @author Mamadou BA
 * @author Cédric MARIYA CONSTANTINE
 * @author Abdelrahim RICHE
 * @author Vincent SOUSA
 * @author Yacine ZEMOUCHE
 */
public enum FileLine {
    FOLDER("partie/"),
    GAME_FILE_C("game_data_"),
    GAME_FILE_JAVA("mst_"),
    GAME_FILE_OUTPUT("best_path_"),
    GAME_FILE_EXTENSION(".txt"),
    FIELDS_SEPARATOR(":"),
    BANNED_WORDS("bannedWords :"),
    DISTANCE_BETWEEN_WORDS("Distance entre les mots :"),
    SIMILARITY_C_FILE_SEPARATOR(", distance:"),
    WORDS_SEPARATOR_OUTPUT("_"),
    SIMILARITY_SEPARATOR(","),
    EOF("EOF"),
    BEST_PATH("BestPath :"),
    START_WORD("startWord : "),
    END_WORD("endWord : "),
    EDGES_MST("edgesMST :"),
    EDGE_FORMAT("%s_%s,%.2f"),
    BEST_PATH_EDGES("bestPathEdges :"),
    MINIMUM_SIMILARITY("MinimumSimilarity : "),
    MAXIMUM_SPANNING_TREE("MaximumSpanningTree :");

    public final String line;

    FileLine(String line) {
        this.line = line;
    }
}
