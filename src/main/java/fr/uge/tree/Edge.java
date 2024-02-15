package fr.uge.tree;

import java.util.Objects;

public record Edge(Word sourceWord, double similarity, Word targetWord) {

    @Override
    public String toString() {
        return sourceWord + " -> " + targetWord + " (" + similarity + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return Objects.equals(sourceWord, edge.sourceWord) && Objects.equals(targetWord, edge.targetWord) && similarity == edge.similarity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceWord, similarity, targetWord);
    }
}
