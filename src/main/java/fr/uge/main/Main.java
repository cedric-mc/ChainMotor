package fr.uge.main;

import java.io.IOException;
import java.util.Objects;

/**
 * Classe principale d’éxécution
 * lang = fr
 *
 * @author Mamadou BA
 * @author Cédric MARIYA CONSTANTINE
 * @author Abdelrahim RICHE
 * @author Vincent SOUSA
 * @author Yacine ZEMOUCHE
 * @see MaximumSpanningTree
 * @see fr.uge.main.FileLine
 * @see fr.uge.main.BestPath
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
                    "Détails de l'utilisation : java -cp ChainMotor/target/classes fr.uge.main.Main --help\n");
            return;
        }
        if (Objects.equals(args[0], "--help")) { // Si l'argument est --help
            StringBuilder help = new StringBuilder();
            help.append("Utilisation : java -cp ChainMotor/target/classes fr.uge.main.Main <fichier-c> <fichier-java>\n");
            help.append("fichier-c : Nom du fichier C : game_data_[pseudo].txt\n");
            help.append("fichier-java (facultatif) : Nom du fichier Java : mst_[pseudo].txt\n");
            help.append("Attention : On doit mettre l'argument <fichier-java> si on veut ajouter un mot\n");
            help.append("Exemple : java -cp ChainMotor/target/classes fr.uge.main.Main game_data_pseudoDuTesst.txt\n");
            help.append("Exemple : java -cp ChainMotor/target/classes fr.uge.main.Main game_data_pseudoDuTesst.txt mst_pseudoDuTesst.txt\n");
            return;
        }
        // Si le nombre d'arguments est supérieur à 2 (2 arguments maximum)
        if (args.length > 2) {
            System.out.println("Vérifier l'utilisation avec l'argument --help\n");
            return;
        }
        // Récupération du pseudo du joueur à partir du nom du fichier
        String pseudo = args[0].replace(FileLine.GAME_FILE_EXTENSION.line, "")
                .replace(FileLine.GAME_FILE_C.line, "")
                .replace(FileLine.FOLDER.line, "");
        MaximumSpanningTree maximumSpanningTree;
        String fileName;
        if (args.length == 1 && !Objects.equals(args[0], "--help")) {
            // Si le nombre d'arguments est égal à 1 et que l'argument n'est pas --help
            // Création de l’arbre recouvrant maximal et exportation dans un fichier
            maximumSpanningTree = MaximumSpanningTree.createMaximumSpanningTree(args[0]);
            fileName = FileLine.FOLDER.line + FileLine.GAME_FILE_JAVA.line + pseudo + FileLine.GAME_FILE_EXTENSION.line;
        } else { // Sinon, on charge l’arbre recouvrant maximal à partir du fichier (on a 2 arguments)
            fileName = args[1];
            maximumSpanningTree = MaximumSpanningTree.loadMaximumSpanningTree(fileName);
            System.out.println("MaximumSpanningTree : " + maximumSpanningTree);
            maximumSpanningTree.loadAddEdges(args[0]); // On ajoute les arêtes du nouveau mot à l'arbre recouvrant maximal
            System.out.println("loadAddEdges : " + maximumSpanningTree);
        }
        // On exporte l’arbre recouvrant maximal dans un fichier
        maximumSpanningTree.exportMaximumSpanningTreeToFile(fileName);

        BestPath bestPath = new BestPath(maximumSpanningTree); // Trouver le chemin entre deux mots
        bestPath.printPathAndScore(); // Afficher le meilleur chemin et le score
        // Écrire le meilleur chemin dans un fichier
        String bestPathFileName = FileLine.FOLDER.line + FileLine.BEST_PATH.line + pseudo + FileLine.GAME_FILE_EXTENSION.line;
        bestPath.writeBestPathToFile(bestPathFileName);
    }
}
