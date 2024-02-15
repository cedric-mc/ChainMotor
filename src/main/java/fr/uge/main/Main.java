package fr.uge.main;

import fr.uge.tree.Edge;
import fr.uge.tree.MaximumSpanningTree;
import fr.uge.tree.Word;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    // Méthode pour le premier fichier et le MaximumSpanningTree du premier tour (soit mot de départ et mot de fin et la seule arête)
    public static void createMaximumSpanningTree(String fileC, String fileJava) {
        MaximumSpanningTree maximumSpanningTree = null;
        Path readerPath = Path.of(fileC);
        try (BufferedReader br = Files.newBufferedReader(readerPath)) {
            String line;
            br.readLine(); // Ligne 1 : "Mots de départ :"
            Word startWord = new Word(br.readLine().split(",")[0].trim()); // Ligne 2 : "voiture,561464"
            Word endWord = new Word(br.readLine().split(",")[0].trim()); // Ligne 3 : "bus,1715044"
            maximumSpanningTree = new MaximumSpanningTree(startWord, endWord);
            br.readLine(); // Ligne 4 : "Liste des mots :"
            br.readLine(); // Ligne 5 : "voiture, offset: 561464"
            br.readLine(); // Ligne 6 : "bus, offset: 1715044"
            br.readLine(); // Ligne 7 : "Distance entre les mots :"
            String[] parts = br.readLine().split(",");
            String[] words = parts[0].split("_");
            Word sourceWord = new Word(words[0]);
            Word targetWord = new Word(words[1]);
            double similarity = Double.parseDouble(parts[1]);
            maximumSpanningTree.addEdge(new Edge(sourceWord, similarity, targetWord));
            System.out.printf("MST in the file : %s%n", maximumSpanningTree);
        } catch (Exception e) {
            System.out.println("Erreur lors de la lecture du fichier");
        }

        Path writePath = Path.of(fileJava);
        try (BufferedWriter bw = Files.newBufferedWriter(writePath)) {
            bw.write("MaximumSpanningTree :");
            bw.newLine();
            bw.write(String.format("startWord : %s", maximumSpanningTree.getStartWord()));
            bw.newLine();
            bw.write(String.format("endWord : %s", maximumSpanningTree.getEndWord()));
            bw.newLine();
            bw.write("edgesMST :");
            bw.newLine();
            for (Edge edge : maximumSpanningTree.getEdgesMST()) {
                bw.write(String.format("%s_%s,%f", edge.sourceWord().word(), edge.targetWord(), edge.similarity()));
                bw.newLine();
            }
            bw.write("bannedWords :");
            bw.newLine();
            for (Word word : maximumSpanningTree.getBannedWords()) {
                bw.write(word.word());
                bw.newLine();
            }
            bw.write("EOF");
        } catch (Exception e) {
            System.out.println("Erreur lors de l'écriture du fichier");
        }
    }

    public static Map<Word, List<Edge>> loadAddEdges(String file) {
        Word addWord = null;
        List<Edge> edges = new ArrayList<>();
        Path filePath = Path.of(file);
        try (BufferedReader br = Files.newBufferedReader(filePath)) {
            String line;
            br.readLine(); // Ligne 1 : "Mots de départ :"
            br.readLine(); // Ligne 2 : "voiture,561464"
            br.readLine(); // Ligne 3 : "bus,1715044"
            br.readLine(); // Ligne 4 : "Liste des mots :"
            while ((line = br.readLine()) != "Distance entre les mots :") {
                // On récupère les mots uniquement
                String[] words = line.split(",");
                addWord = new Word(words[0]);
            }
            br.readLine(); // Ligne 5 : "Distance entre les mots :"
            while ((line = br.readLine()) != null) {
                if (line.contains(addWord.word())) {
                    MaximumSpanningTree.divideParts(edges, line);
                }
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la lecture du fichier");
        }
        return new HashMap<>(Map.of(addWord, edges));
    }

    public static void main(String[] args) {
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
            createMaximumSpanningTree(fileNameC, fileNameJava);
            return;
        }

        MaximumSpanningTree maximumSpanningTree = MaximumSpanningTree.loadMaximumSpanningTree(fileNameJava);
        Map<Word, List<Edge>> wordMap = loadAddEdges(fileNameC);
        maximumSpanningTree.addWord(wordMap);
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
