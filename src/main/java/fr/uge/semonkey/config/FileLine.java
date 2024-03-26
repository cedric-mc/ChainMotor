package fr.uge.semonkey.config;

import fr.uge.semonkey.algorithm.BestPath;
import fr.uge.semonkey.main.Main;
import fr.uge.semonkey.model.MaximumSpanningTree;

/**
 * Enumération des lignes de fichier, pour une meilleure lisibilité
 * lang = fr
 *
 * @author Mamadou BA
 * @author Cédric MARIYA CONSTANTINE
 * @author Abdelrahim RICHE
 * @author Vincent SOUSA
 * @author Yacine ZEMOUCHE
 * @see BestPath
 * @see Main
 * @see MaximumSpanningTree
 */
public enum FileLine {
    /**
     * Lignes de fichier : bannedWords
     */
    BANNED_WORDS("bannedWords :"),
    /**
     * Lignes de fichier : BestPath
     */
    BEST_PATH("BestPath :"),
    /**
     * Lignes de fichier : bestPathEdges
     */
    BEST_PATH_EDGES("bestPathEdges :"),
    /**
     * Lignes de fichier : distanceBetweenWords
     */
    DISTANCE_BETWEEN_WORDS("Distance entre les mots :"),
    /**
     * Lignes de fichier : edgesMST
     */
    EDGES_MST("edgesMST :"),
    /**
     * Lignes de fichier : format d'une arête
     */
    EDGE_FORMAT("%s_%s,%.2f"),
    /**
     * Lignes de fichier : format d'une arête dans le fichier de sortie
     */
    EDGE_FORMAT_OUTPUT("%s -> %s : %.2f"),
    /**
     * Lignes de fichier : endWord
     */
    END_WORD("endWord : "),
    /**
     * Lignes de fichier : EOF (End Of File)
     */
    EOF("EOF"),
    /**
     * Lignes de fichier : fieldsSeparator
     */
    FIELDS_SEPARATOR(":"),
    /**
     * Lignes de fichier : folder
     */
    FOLDER("partie/"),
    /**
     * Lignes de fichier : game_data_ (fichier généré par le programme C)
     */
    GAME_FILE_C("game_data_"),
    /**
     * Lignes de fichier : gameFileExtension (extension des fichiers)
     */
    GAME_FILE_EXTENSION(".txt"),
    /**
     * Lignes de fichier : mst_ (fichier généré par le programme Java)
     */
    GAME_FILE_JAVA("mst_"),
    /**
     * Lignes de fichier : best_path_ (fichier généré par le programme Java)
     */
    GAME_FILE_OUTPUT("best_path_"),
    /**
     * Lignes de fichier : maximumSpanningTree
     */
    MAXIMUM_SPANNING_TREE("MaximumSpanningTree :"),
    /**
     * Lignes de fichier : minimumSimilarity
     */
    MINIMUM_SIMILARITY("MinimumSimilarity : "),
    /**
     * Lignes de fichier : obtenir les mots de départ et de fin et la similarité
     */
    SIMILARITY_C_FILE_SEPARATOR(", distance:"),
    /**
     * Lignes de fichier : séparateur des mots et similarité dans le fichier de sortie
     */
    SIMILARITY_SEPARATOR(","),
    /**
     * Lignes de fichier : startWord
     */
    START_WORD("startWord : "),
    /**
     * Lignes de fichier : wordsSeparator
     */
    WORDS_SEPARATOR_OUTPUT("_");

    /**
     * Ligne de fichier
     */
    public final String line;

    /**
     * Constructeur
     *
     * @param line Ligne de fichier
     */
    FileLine(String line) {
        this.line = line;
    }
}
