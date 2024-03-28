package fr.uge.semonkey.model;

import java.util.*;

public class Graph {
    private final Map<Word, List<Edge>> adjacencyList;

    public Graph(Map<Word, List<Edge>> adjacencyList) {
        this.adjacencyList = adjacencyList;
    }

    public Graph() {
        this.adjacencyList = new HashMap<>();
    }

    public List<Edge> getEdges(Word word) {
        return adjacencyList.get(word);
    }

    public List<Word> getVertices() {
        return List.copyOf(adjacencyList.keySet());
    }

    public void addEdge(Edge edge) {
        Word sourceWord = edge.sourceWord();
        Word targetWord = edge.targetWord();

        // Ajouter l'arête à la liste d'adjacence des deux nœuds
        adjacencyList.computeIfAbsent(sourceWord, k -> new ArrayList<>()).add(edge);
        adjacencyList.computeIfAbsent(targetWord, k -> new ArrayList<>()).add(edge);
    }

    public void removeEdge(Edge edge) {
        Word sourceWord = edge.sourceWord();
        Word targetWord = edge.targetWord();

        // Supprimer l'arête de la liste d'adjacence des deux nœuds
        adjacencyList.get(sourceWord).remove(edge);
        adjacencyList.get(targetWord).remove(edge);
    }

    public List<Edge> getAdjacentEdges(Word node) {
        return adjacencyList.getOrDefault(node, Collections.emptyList());
    }
}
