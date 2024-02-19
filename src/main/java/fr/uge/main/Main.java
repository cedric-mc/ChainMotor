package fr.uge.main;

import fr.uge.tree.MaximumSpanningTree;

import java.io.IOException;
import java.util.Objects;

/**
 * Classe principale d’éxécution
 * @see Main
 * lang = fr
 * @author Mamadou BA
 * @author Cédric MARIYA CONSTANTINE
 * @author Abdelrahim RICHE
 * @author Vincent SOUSA
 * @author Yacine ZEMOUCHE
 */
public class Main {
    /**
     * Méthode principale :
     * @param args
     * args[0] : pseudo du joueur
     * args[1] : etat du jeu (1er tour ou ajout de mot), 0 pour le 1er tour, 1 pour l'ajout de mot
     * @throws IOException
     *
     * Récupèration ou création d’un arbre recouvrant maximal et recherche du meilleur chemin entre le mot de départ et de cible.
     * Les fichiers doivent être dans le dossier `partie`.
     */
    public static void main(String[] args) throws IOException {
        if (args.length == 0) { // S'il n'y a pas d'arguments
            System.out.println("Auteurs :\nMamadou BA\nCédric MARIYA CONSTANTINE\nAbdelrahim RICHE\nVincent SOUSA\nYacine ZEMOUCHE");
            return;
        }
        // Si le nombre d'arguments est différent de 2
        if (args.length != 2) {
            System.out.println("Utilisation : java -cp ChainMotor/target/classes fr.uge.main.Main [pseudo] etat");
            return;
        }
        String fileNameC = FileLine.GAME_FILE_C.line + args[0] + FileLine.GAME_FILE_EXTENSION.line; // Nom du fichier C : game_data_[pseudo].txt
        String fileNameJava = FileLine.GAME_FILE_JAVA.line + args[0] + FileLine.GAME_FILE_EXTENSION.line; // Nom du fichier Java : mst_[pseudo].txt
        String fileNameOutput = FileLine.GAME_FILE_OUTPUT.line + args[0] + FileLine.GAME_FILE_EXTENSION.line; // Nom du fichier de sortie : best_path_[pseudo].txt
        MaximumSpanningTree maximumSpanningTree;
        if (Objects.equals(args[1], "0")) {
            // Création de l’arbre recouvrant maximal et exportation dans un fichier
            maximumSpanningTree = MaximumSpanningTree.createMaximumSpanningTree(fileNameC);
        } else { // Sinon, on charge l’arbre recouvrant maximal
            maximumSpanningTree = MaximumSpanningTree.loadMaximumSpanningTree(fileNameJava);
            maximumSpanningTree.loadAddEdges(fileNameC); // On ajoute les arêtes du nouveau mot à l'arbre recouvrant maximal
        }
        // On exporte l’arbre recouvrant maximal dans un fichier
        maximumSpanningTree.exportMaximumSpanningTreeToFile(fileNameJava);
        BestPath bestPath = new BestPath(maximumSpanningTree); // Trouver le chemin entre deux mots

        // bestPath.printPathAndScore(); // Afficher le meilleur chemin et le score
        // Écrire le meilleur chemin dans un fichier
        bestPath.writeBestPathToFile(fileNameOutput);
    }
}
