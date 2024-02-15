package fr.uge.main;

import fr.uge.tree.Edge;
import fr.uge.tree.MaximumSpanningTree;
import fr.uge.tree.Word;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class BestPath {

    private final MaximumSpanningTree maximumSpanningTree;
    private final List<Edge> bestPathEdges;
    private double minimumSimilarity;

    public BestPath(MaximumSpanningTree maximumSpanningTree) {
        this.maximumSpanningTree = maximumSpanningTree;
        this.bestPathEdges = breadthFirstSearch();
        this.minimumSimilarity = calculatePathScore(bestPathEdges);
    }

    public MaximumSpanningTree getMaximumSpanningTree() {
        return maximumSpanningTree;
    }

    public List<Edge> getBestPathEdges() {
        return bestPathEdges;
    }

    public double getMinimumSimilarity() {
        return minimumSimilarity;
    }

    public void setMinimumSimilarity(double minimumSimilarity) {
        this.minimumSimilarity = minimumSimilarity;
    }

    public void writeBestPathToFile(String file) {
        try (BufferedWriter bw = Files.newBufferedWriter(Path.of(file))) {
            bw.write("BestPath :");
            bw.newLine();
            bw.write("startWord : " + maximumSpanningTree.getStartWord());
            bw.newLine();
            bw.write("endWord : " + maximumSpanningTree.getEndWord());
            bw.newLine();
            bw.write("bestPathEdges :");
            bw.newLine();
            for (Edge edge : bestPathEdges) {
                bw.write(edge.sourceWord() + "_" + edge.targetWord() + "," + edge.similarity());
                bw.newLine();
            }
            bw.write("EOF");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double calculatePathScore(List<Edge> path) {
        // Initialiser le score avec la valeur maximale possible
        double score = Double.MAX_VALUE;
        for (Edge edge : path) { // Parcourir chaque arête du chemin
            // Mettre à jour le score avec la similarité minimale trouvée
            score = Math.min(score, edge.similarity());
        }
        // Si aucun chemin n’est trouvé (chemin vide), retourner 0 ou une autre valeur par défaut
        return score == Double.MAX_VALUE ? 0 : score;
    }

    public void printPathAndScore(List<Edge> path) {
        // Vérifier si le chemin est vide
        if (path.isEmpty()) {
            System.out.println("Aucun chemin trouvé.");
            return;
        }

        // Afficher le chemin
        System.out.println("Chemin :");
        for (Edge edge : path) {
            System.out.printf("%s -> %s : %.2f%n", edge.sourceWord(), edge.targetWord(), edge.similarity());
        }

        // Afficher le score du chemin
        System.out.println("Score (similarité minimale) : " + getMinimumSimilarity());
    }

    // Méthode auxiliaire pour trouver l’arête entre deux mots dans l’arbre MST
    private Edge findEdgeBetween(Word word1, Word word2) {
        for (Edge edge : maximumSpanningTree.getEdgesMST()) {
            if ((edge.sourceWord().equals(word1) && edge.targetWord().equals(word2)) ||
                    (edge.sourceWord().equals(word2) && edge.targetWord().equals(word1))) {
                return edge;
            }
        }
        return null;
    }

    // Méthode pour construire le chemin à partir de la carte parent
    private List<Edge> constructPath(Map<Word, Word> parentMap) {
        List<Edge> path = new ArrayList<>();
        // Commencer par le mot de fin et remonter jusqu’au mot de départ
        Word current = maximumSpanningTree.getEndWord();
        while (current != null && !current.equals(maximumSpanningTree.getStartWord())) {
            Word parent = parentMap.get(current);
            // Si aucun parent n’est trouvé, cela signifie que le chemin est incomplet
            if (parent == null) {
                break;
            }
            // Trouver l’arête entre le mot actuel et son parent
            Edge edge = findEdgeBetween(parent, current);
            if (edge != null) {
                // Ajouter l’arête au début de la liste pour que le chemin soit dans le bon ordre
                path.addFirst(edge);
            }
            // Passer au mot parent pour continuer à remonter le chemin
            current = parent;
        }
        return path;
    }

    // Méthode pour trouver le chemin le plus court entre le mot de départ et le mot de fin
    public List<Edge> breadthFirstSearch() {
        Map<Word, Word> parentMap = new HashMap<>(); // Carte pour garder une trace du mot parent pour chaque mot visité
        Queue<Word> queue = new LinkedList<>(); // File d’attente pour le parcours BFS
        Set<Word> visited = new HashSet<>(); // Ensemble pour suivre les mots déjà visités
        // Initialiser la file d’attente et les ensembles
        queue.add(maximumSpanningTree.getStartWord());
        visited.add(maximumSpanningTree.getStartWord());
        parentMap.put(maximumSpanningTree.getStartWord(), null);

        // Parcours BFS
        while (!queue.isEmpty()) {
            Word current = queue.remove();
            // Arrêter si le mot de fin est atteint
            if (current.equals(maximumSpanningTree.getEndWord())) {
                break;
            }
            // Vérifier chaque arête de l’arbre
            for (Edge edge : maximumSpanningTree.getEdgesMST()) {
                Word neighbor = null;
                // Trouver le voisin du mot actuel
                if (edge.sourceWord().equals(current)) {
                    neighbor = edge.targetWord();
                } else if (edge.targetWord().equals(current)) {
                    neighbor = edge.sourceWord();
                }
                // Si le voisin n’a pas encore été visité, l’ajouter à la file d’attente
                if (neighbor != null && !visited.contains(neighbor)) {
                    queue.add(neighbor);
                    visited.add(neighbor);
                    parentMap.put(neighbor, current);
                }
            }
        }
        // Construire et retourner le chemin trouvé
        return constructPath(parentMap);
    }
}
