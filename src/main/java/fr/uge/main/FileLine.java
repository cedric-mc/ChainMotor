package fr.uge.main;

public enum FileLine {
    FILE_WORDS_SEPARATOR("_"),
    WORDS_SEPARATOR("_"),
    SIMILARITY_SEPARATOR(","),
    EOF("EOF"),
    BEST_PATH("BestPath :"),
    START_WORD("startWord : "),
    END_WORD("endWord : "),
    BEST_PATH_EDGES("bestPathEdges :");

    public final String line;

    FileLine(String line) {
        this.line = line;
    }
}
