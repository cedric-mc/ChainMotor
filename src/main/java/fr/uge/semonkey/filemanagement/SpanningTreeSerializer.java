package fr.uge.semonkey.filemanagement;

import java.io.IOException;

public interface SpanningTreeSerializer {
    void serialize(String fileName) throws IOException;
    SpanningTreeSerializer deserialize(String fileName) throws IOException;
}
