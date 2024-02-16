package fr.uge.main;

import fr.uge.tree.Edge;
import fr.uge.tree.MaximumSpanningTree;
import fr.uge.tree.Word;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        // S'il n'y a pas d'arguments
        if (args.length == 0) {
            System.out.println("Auteurs :\nMamadou BA\nCédric MARIYA CONSTANTINE\nAbdelrahim RICHE\nVincent SOUSA\nYacine ZEMOUCHE\n");
            return;
        }
        // Si le nombre d'arguments est différent de 2
        if (args.length != 2 || !args[0].startsWith("game_data_") || !args[1].startsWith("mst_")) {
            System.out.println("Utilisation : java -jar chain-motor.jar <game_data_[pseudo].txt> <mst_[pseudo].txt>");
            return;
        }

        String fileNameC = args[0]; // Nom du fichier C : game_data_[pseudo].txt
        String fileNameJava = args[1]; // Nom du fichier Java : mst_[pseudo].txt
        // On vérifie que les fichiers existent
        if (!Files.exists(Path.of(fileNameJava))) {
            MaximumSpanningTree maximumSpanningTree = MaximumSpanningTree.createMaximumSpanningTree(fileNameC, fileNameJava);
            maximumSpanningTree.exportMaximumSpanningTreeToFile(fileNameJava);
            return;
        }

        MaximumSpanningTree maximumSpanningTree = MaximumSpanningTree.loadMaximumSpanningTree(fileNameJava);
        maximumSpanningTree.loadAddEdges(fileNameC);
        maximumSpanningTree.exportMaximumSpanningTreeToFile(fileNameJava);

        System.out.println(maximumSpanningTree);

        // Trouver le chemin entre deux mots
        BestPath bestPath = new BestPath(maximumSpanningTree);
        bestPath.printPathAndScore(bestPath.getBestPathEdges());

        // Écrire le meilleur chemin dans un fichier
        // Récupérer un pseudo pour le nom du fichier
        String pseudo = fileNameJava.split("_")[1].split("\\.")[0];
        bestPath.writeBestPathToFile("best_path_" + pseudo + ".txt");
    }
}
