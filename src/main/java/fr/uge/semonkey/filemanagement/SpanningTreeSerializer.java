package fr.uge.semonkey.filemanagement;

import fr.uge.semonkey.model.Edge;
import fr.uge.semonkey.model.Word;

import java.io.IOException;
import java.util.List;

public interface SpanningTreeSerializer {
    Word getStartWord();

    Word getEndWord();

    List<Edge> getEdges();

    void serialize(String fileName) throws IOException;

    SpanningTreeSerializer deserialize(String fileName) throws IOException;

    void loadAddEdges(String fileName) throws IOException;
}
