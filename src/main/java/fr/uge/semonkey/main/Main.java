package fr.uge.semonkey.main;

import fr.uge.semonkey.algorithm.BestPath;
import fr.uge.semonkey.config.FileLine;
import fr.uge.semonkey.filemanagement.SpanningTreeSerializer;
import fr.uge.semonkey.model.MaximumSpanningTree;
import fr.uge.semonkey.model.Word;

import java.io.IOException;
import java.util.Objects;

/**
 * Classe principale d’éxécution
 *
 * @author Mamadou BA
 * @author Cédric MARIYA CONSTANTINE
 * @author Abdelrahim RICHE
 * @author Vincent SOUSA
 * @author Yacine ZEMOUCHE
 */
public class Main {
    /**
     * Constructeur par défaut
     */
    public Main() {
    }

    /**
     * Méthode principale :
     *
     * @param args args[0] : pseudo du joueur
     *             args[1] : etat du jeu (1er tour ou ajout de mot), 0 pour le 1er tour, 1 pour l'ajout de mot
     * @throws IOException Récupèration ou création d’un arbre recouvrant maximal et recherche du meilleur chemin entre le mot de départ et de cible.
     *                     Les fichiers doivent être dans le dossier `partie`.
     */
    public static void main(String[] args) throws IOException {
        if (args.length == 0) { // S'il n'y a pas d'arguments
            System.out.println("Auteurs :\nMamadou BA\nCédric MARIYA CONSTANTINE\nAbdelrahim RICHE\nVincent SOUSA\nYacine ZEMOUCHE\n" +
                    "Détails de l'utilisation : java -cp ChainMotor/target/classes fr.uge.semonkey.main.Main --help\n");
            return;
        }
        if (args.length == 1 && Objects.equals(args[0], "--help")) { // Si l'argument est --help
            System.out.println("Utilisation : java -cp ChainMotor/target/classes fr.uge.semonkey.main.Main <fichier-c> <fichier-java>\n"
                    + "fichier-c : Nom du fichier C : game_data_[pseudo].txt\n"
                    + "fichier-java (facultatif) : Nom du fichier Java : mst_[pseudo].txt\n"
                    + "Attention : On doit mettre l'argument <fichier-java> si on veut ajouter un mot\n"
                    + "Exemple : java -cp ChainMotor/target/classes fr.uge.semonkey.main.Main game_data_pseudoDuTesst.txt\n"
                    + "Exemple : java -cp ChainMotor/target/classes fr.uge.semonkey.main.Main game_data_pseudoDuTesst.txt mst_pseudoDuTesst.txt\n");
            return;
        }
        // Si le nombre d'arguments n'est égal ni à 1 ni à 2
        if (args.length != 1 && args.length != 2) {
            System.out.println("Erreur : Nombre d'arguments incorrect");
            return;
        }
        // Récupération du pseudo du joueur à partir du nom du fichier
        String pseudo = args[0].replace(FileLine.GAME_FILE_EXTENSION.line, "")
                .replace(FileLine.GAME_FILE_C.line, "")
                .replace(FileLine.FOLDER.line, "");
        SpanningTreeSerializer maximumSpanningTree = new MaximumSpanningTree(new Word("test"), new Word("test"));
        String fileName;
        if (args.length == 1 && !Objects.equals(args[0], "--help")) {
            // Si le nombre d'arguments est égal à 1 et que l'argument n'est pas --help
            // Création de l’arbre recouvrant maximal et exportation dans un fichier
            maximumSpanningTree = MaximumSpanningTree.createMaximumSpanningTree(args[0]);
            fileName = FileLine.FOLDER.line + FileLine.GAME_FILE_JAVA.line + pseudo + FileLine.GAME_FILE_EXTENSION.line;
        } else { // Sinon, on charge l’arbre recouvrant maximal à partir du fichier (on a 2 arguments)
            fileName = args[1];
            maximumSpanningTree = maximumSpanningTree.deserialize(fileName);
            maximumSpanningTree.loadAddEdges(args[0]); // On ajoute les arêtes du nouveau mot à l'arbre recouvrant maximal
        }
        // On exporte l’arbre recouvrant maximal dans un fichier
        maximumSpanningTree.serialize(fileName);

        BestPath bestPath = new BestPath(maximumSpanningTree); // Trouver le chemin entre deux mots
        bestPath.printPathAndScore(); // Afficher le meilleur chemin et le score
        // Écrire le meilleur chemin dans un fichier
        String bestPathFileName = FileLine.FOLDER.line + FileLine.GAME_FILE_OUTPUT.line + pseudo + FileLine.GAME_FILE_EXTENSION.line;
        bestPath.writeBestPathToFile(bestPathFileName);
    }
}
