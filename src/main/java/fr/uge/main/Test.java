package fr.uge.main;

import fr.uge.tree.Edge;
import fr.uge.tree.MaximumSpanningTree;
import fr.uge.tree.Word;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {
    public static void main(String[] args) {
        MaximumSpanningTree exampleMst = new MaximumSpanningTree(new Word("A"), new Word("B"));
        exampleMst.addEdge(new Edge(new Word("A"), 10, new Word("B")));
        System.out.println("MST : " + exampleMst);

        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge(new Word("C"), 30, new Word("A")));
        edges.add(new Edge(new Word("C"), 20, new Word("B")));
        exampleMst.addWord(new HashMap<>(Map.of(new Word("A"), edges)));
        System.out.println("MST : " + exampleMst);

        edges.clear();
        edges.add(new Edge(new Word("D"), 5, new Word("A")));
        edges.add(new Edge(new Word("D"), 50, new Word("B")));
        edges.add(new Edge(new Word("D"), 40, new Word("C")));
        exampleMst.addWord(new HashMap<>(Map.of(new Word("A"), edges)));
        System.out.println("MST : " + exampleMst);

        BestPath bestPath = new BestPath(exampleMst);
        bestPath.printPathAndScore(bestPath.getBestPathEdges());
    }
}
