package fr.uge.main;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.PosixFilePermission;
import java.util.*;

/**
 * Classe pour trouver le chemin le plus court entre deux mots dans un arbre MST
 * lang = fr
 *
 * @author Mamadou BA
 * @author Cédric MARIYA CONSTANTINE
 * @author Abdelrahim RICHE
 * @author Vincent SOUSA
 * @author Yacine ZEMOUCHE
 * @see MaximumSpanningTree
 * @see Word
 * @see Edge
 * @see fr.uge.main
 * @see fr.uge.main.Test
 * @see fr.uge.main.FileLine
 * @see fr.uge.main.Main
 */
public class BestPath {

    /**
     * Arbre MST
     */
    private final MaximumSpanningTree maximumSpanningTree; // Arbre MST
    /**
     * Arêtes du meilleur chemin
     */
    private final List<Edge> bestPathEdges; // Arêtes du chemin
    /**
     * Similarité minimale du chemin (plus petite similarité dans le chemin)
     */
    private final double minimumSimilarity; // Similarité minimale du chemin

    /**
     * Constructeur pour trouver le chemin le plus court entre deux mots dans un arbre MST
     *
     * @param maximumSpanningTree Arbre MST
     */
    public BestPath(MaximumSpanningTree maximumSpanningTree) {
        this.maximumSpanningTree = maximumSpanningTree;
        this.bestPathEdges = breadthFirstSearch(); // Trouver le chemin le plus court
        this.minimumSimilarity = calculatePathScore(bestPathEdges); // Calculer la similarité minimale du chemin
    }

    /**
     * Méthode pour obtenir l’arbre MST
     *
     * @return Arbre MST
     */
    public MaximumSpanningTree getMaximumSpanningTree() {
        return maximumSpanningTree;
    }

    /**
     * Méthode pour obtenir les arêtes du chemin
     *
     * @return Arêtes du chemin
     */
    public List<Edge> getBestPathEdges() {
        return bestPathEdges;
    }

    /**
     * Méthode pour obtenir la similarité minimale du chemin
     *
     * @return Similarité minimale du chemin
     */
    public double getMinimumSimilarity() {
        return minimumSimilarity;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(FileLine.BEST_PATH.line).append("\n");
        sb.append(FileLine.START_WORD.line).append(maximumSpanningTree.getStartWord()).append("\n");
        sb.append(FileLine.END_WORD.line).append(maximumSpanningTree.getEndWord()).append("\n");
        sb.append(FileLine.BEST_PATH_EDGES.line).append("\n");
        for (Edge edge : bestPathEdges) {
            sb.append(String.format(FileLine.EDGE_FORMAT_OUTPUT.line, edge.sourceWord().word(), edge.targetWord(), edge.similarity())).append("\n");
        }
        sb.append(FileLine.MINIMUM_SIMILARITY.line).append(minimumSimilarity).append("\n");
        sb.append(FileLine.EOF.line);
        return sb.toString();
    }

    /**
     * Méthode pour écrire le chemin le plus court dans un fichier
     *
     * @param file Fichier de sortie
     * @throws IOException Erreur d’écriture dans le fichier
     */
    public void writeBestPathToFile(String file) throws IOException {
        // Créer un objet Path pour le fichier et un objet BufferedWriter pour écrire dans le fichier
        Path path = Paths.get(file);
        BufferedWriter bufferedWriter = null;
        try { // Créer un objet BufferedWriter pour écrire dans le fichier
            bufferedWriter = Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            bufferedWriter.write(toString()); // Écrire l’arbre recouvrant maximal dans le fichier

            // Définir les permissions
            Set<PosixFilePermission> perms = Set.of(
                    PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_WRITE,
                    PosixFilePermission.GROUP_READ, PosixFilePermission.GROUP_WRITE,
                    PosixFilePermission.OTHERS_READ, PosixFilePermission.OTHERS_WRITE
            );
            Files.setPosixFilePermissions(path, perms);
        } catch (IOException e) {
            // Handle exception
            throw new IOException("Error writing to file", e);
        } finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close(); // Fermer le fichier
                } catch (IOException e) {
                    // Handle exception
                    throw new IOException("Error closing BufferedWriter", e);
                }
            }
        }
    }

    /**
     * Méthode pour calculer la similarité minimale du chemin
     *
     * @param path Chemin
     * @return Similarité minimale du chemin
     */
    private double calculatePathScore(List<Edge> path) {
        // Initialiser le score avec la valeur maximale possible
        double score = Double.MAX_VALUE;
        for (Edge edge : path) { // Parcourir chaque arête du chemin
            // Mettre à jour le score avec la similarité minimale trouvée
            score = Math.min(score, edge.similarity());
        }
        // Si aucun chemin n’est trouvé (chemin vide), retourner 0 ou une autre valeur par défaut
        return score == Double.MAX_VALUE ? 0 : score;
    }

    /**
     * Méthode pour afficher le chemin le plus court et sa similarité minimale
     */
    public void printPathAndScore() {
        // Vérifier si le chemin est vide
        if (bestPathEdges.isEmpty()) {
            System.out.println("Aucun chemin trouvé.");
            return;
        }

        // Afficher le chemin
        System.out.println("Chemin :");
        for (Edge edge : bestPathEdges) {
            System.out.printf("%s -> %s : %.2f%n", edge.sourceWord(), edge.targetWord(), edge.similarity());
        }

        // Afficher le score du chemin
        System.out.println("Score (similarité minimale) : " + getMinimumSimilarity());
    }

    /**
     * Méthode pour trouver le chemin le plus court entre deux mots
     *
     * @param word1 Mot 1
     * @param word2 Mot 2
     * @return Arêtes du chemin le plus court entre les deux mots
     */
    private Edge findEdgeBetween(Word word1, Word word2) {
        for (Edge edge : maximumSpanningTree.getEdgesMST()) { // Parcourir chaque arête de l’arbre
            if ((edge.sourceWord().equals(word1) && edge.targetWord().equals(word2)) ||
                    (edge.sourceWord().equals(word2) && edge.targetWord().equals(word1))) {
                // Retourner l’arête si elle relie les deux mots
                return edge;
            }
        }
        return null;
    }

    /**
     * Méthode pour construire le chemin à partir de la carte parent
     *
     * @param parentMap Carte parent
     * @return Chemin
     */
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

    /**
     * Méthode pour trouver le chemin le plus court entre le mot de départ et le mot de fin
     *
     * @return Chemin
     */
    private List<Edge> breadthFirstSearch() {
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
