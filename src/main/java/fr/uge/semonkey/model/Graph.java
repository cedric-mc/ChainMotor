package fr.uge.semonkey.model;

import java.util.List;
import java.util.Map;

public record Graph(Map<Word, List<Edge>> adjacencyList) {
    public List<Edge> getEdges(Word word) {
        return adjacencyList.get(word);
    }

    public List<Word> getVertices() {
        return List.copyOf(adjacencyList.keySet());
    }
}
