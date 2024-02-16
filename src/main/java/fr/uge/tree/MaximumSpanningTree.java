package fr.uge.tree;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class MaximumSpanningTree {
    private final Word startWord;
    private final Word endWord;
    private final List<Edge> edgesMST;
    private final Set<Word> bannedWords;

    public MaximumSpanningTree(Word startWord, Word endWord, List<Edge> edgesMST, Set<Word> bannedWords) {
        this.edgesMST = edgesMST;
        this.startWord = startWord;
        this.endWord = endWord;
        this.bannedWords = bannedWords;
    }

    public MaximumSpanningTree(Word startWord, Word endWord) {
        this(startWord, endWord, new ArrayList<>(), new HashSet<>());
    }

    public static MaximumSpanningTree loadMaximumSpanningTree(String file) {
        String startWord = "";
        String endWord = "";
        List<Edge> edgesMST = new ArrayList<>();
        Set<Word> bannedWords = new HashSet<>();
        Path filePath = Path.of(file);
        try (BufferedReader br = Files.newBufferedReader(filePath)) {
            String line;
            br.readLine(); // Ligne 1 : "MaximumSpanningTree :"
            startWord = br.readLine().split(":")[1].trim(); // Ligne 2 : "startWord : word1"
            endWord = br.readLine().split(":")[1].trim(); // Ligne 3 : "endWord : word2"

            br.readLine(); // Ligne 4 : "edgesMST :"

            while (!Objects.equals(line = br.readLine(), "bannedWords")) {
                divideParts(edgesMST, line);
            }

            while (!Objects.equals(line = br.readLine(), "EOF")) {
                bannedWords.add(new Word(line));
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la lecture du fichier dans loadMaximumSpanningTree");
        }
        return new MaximumSpanningTree(new Word(startWord), new Word(endWord), edgesMST, bannedWords);
    }

    public static void divideParts(List<Edge> edgesMST, String line) {
        String[] parts = line.split(",");
        splitWordsAndSimilarity(edgesMST, parts);
    }

    public Word getStartWord() {
        return startWord;
    }

    public Word getEndWord() {
        return endWord;
    }

    public List<Edge> getEdgesMST() {
        return edgesMST;
    }

    public Set<Word> getBannedWords() {
        return bannedWords;
    }

    public void addEdge(Edge edge) {
        edgesMST.add(edge);
    }

    public void removeEdge(Edge edge) {
        edgesMST.remove(edge);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MaximumSpanningTree :").append(System.lineSeparator());
        sb.append("startWord : ").append(startWord).append(System.lineSeparator());
        sb.append("endWord : ").append(endWord).append(System.lineSeparator());
        sb.append("edgesMST :").append(System.lineSeparator());
        for (Edge edge : edgesMST) {
            sb.append(edge.sourceWord()).append("_").append(edge.targetWord()).append(",").append(edge.similarity()).append(System.lineSeparator());
        }
        sb.append("bannedWords :").append(System.lineSeparator());
        for (Word word : bannedWords) {
            sb.append(word).append(System.lineSeparator());
        }
        sb.append("EOF");
        return sb.toString();
    }

    private static void splitWordsAndSimilarity(List<Edge> edges, String[] parts) {
        String[] words = parts[0].split("_");
        Word sourceWord = new Word(words[0]);
        Word targetWord = new Word(words[1]);
        double similarity = Double.parseDouble(parts[1]);
        edges.add(new Edge(sourceWord, similarity, targetWord));
    }

    // Méthode pour le premier fichier et le MaximumSpanningTree du premier tour (soit mot de départ et mot de fin et la seule arête)
    public static MaximumSpanningTree createMaximumSpanningTree(String fileC, String fileJava) throws IOException {
        MaximumSpanningTree maximumSpanningTree = null;
        Word startWord;
        Word endWord;
        List<Edge> edges = new ArrayList<>();
        Path readerPath = Path.of(fileC);
        BufferedReader br = Files.newBufferedReader(readerPath);
        br.readLine(); // Ligne 1 : "Mots de départ :"
        startWord = new Word(br.readLine().split(",")[0].trim()); // Ligne 2 : "voiture,561464"
        endWord = new Word(br.readLine().split(",")[0].trim()); // Ligne 3 : "bus,1715044"
        br.readLine(); // Ligne 4 : "Liste des mots :"
        br.readLine(); // Ligne 5 : "voiture, offset: 561464"
        br.readLine(); // Ligne 6 : "bus, offset: 1715044"
        br.readLine(); // Ligne 7 : "Distance entre les mots :"
        String[] parts = br.readLine().split(",");
        splitWordsAndSimilarity(edges, parts);
        return new MaximumSpanningTree(startWord, endWord, edges, new HashSet<>());
    }

    public void loadAddEdges(String file) {
        Word addWord = null;
        List<Edge> edges = new ArrayList<>();
        Path filePath = Path.of(file);
        try (BufferedReader br = Files.newBufferedReader(filePath)) {
            String line;
            br.readLine(); // Ligne 1 : "Mots de départ :"
            br.readLine(); // Ligne 2 : "voiture,561464"
            br.readLine(); // Ligne 3 : "bus,1715044"
            br.readLine(); // Ligne 4 : "Liste des mots :"
            while (!Objects.equals(line = br.readLine(), "Distance entre les mots :")) {
                // On récupère les mots uniquement
                String[] words = line.split(",");
                addWord = new Word(words[0]);
            }
            br.readLine(); // Ligne 5 : "Distance entre les mots :"
            while ((line = br.readLine()) != null) {
                assert addWord != null;
                if (line.contains(addWord.word())) {
                    MaximumSpanningTree.divideParts(edges, line);
                }
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la lecture du fichier");
        }
        assert addWord != null;
        Map<Word, List<Edge>> wordMap = new HashMap<>(Map.of(addWord, edges));
        addWord(wordMap);
    }

    public void exportMaximumSpanningTreeToFile(String fileNameJava) {
        Path filePath = Path.of(fileNameJava);
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(filePath)) {
            bufferedWriter.write("MaximumSpanningTree :");
            bufferedWriter.newLine();
            bufferedWriter.write(String.format("startWord : %s", startWord));
            bufferedWriter.newLine();
            bufferedWriter.write(String.format("endWord : %s", endWord));
            bufferedWriter.newLine();
            bufferedWriter.write("edgesMST :");
            bufferedWriter.newLine();
            for (Edge edge : edgesMST) {
                bufferedWriter.write(String.format("%s_%s,%f", edge.sourceWord().word(), edge.targetWord(), edge.similarity()));
                bufferedWriter.newLine();
            }
            bufferedWriter.write("bannedWords :");
            bufferedWriter.newLine();
            for (Word word : bannedWords) {
                bufferedWriter.write(word.word());
                bufferedWriter.newLine();
            }
            bufferedWriter.write("EOF");
        } catch (Exception e) {
            System.out.println("Erreur lors de l'écriture du fichier exportMaximumSpanningTreeToFile");
        }
    }

    // Méthode DFS pour détecter un cycle et recueillir les arêtes du cycle
    private boolean depthFirstSearch(Word current, Word parent, Set<Word> visited, List<Edge> cycleEdges) {
        // Marquer le mot actuel comme visité
        visited.add(current);

        // Parcourir toutes les arêtes de l’arbre (MST)
        for (Edge edge : edgesMST) {
            Word next = null;
            // Vérifier si l’arête courante contient le mot actuel et trouver le mot suivant
            if (edge.sourceWord().equals(current)) {
                next = edge.targetWord();
            } else if (edge.targetWord().equals(current)) {
                next = edge.sourceWord();
            }
            // Si un mot suivant est trouvé
            if (next != null) {
                // Vérifier si le mot suivant n’a pas déjà été visité
                if (!visited.contains(next)) {
                    // Continuer la recherche en profondeur à partir du mot suivant
                    // Si un cycle est trouvé, ajouter l’arête actuelle à la liste des arêtes du cycle
                    // et retourner true pour indiquer la détection d’un cycle
                    if (depthFirstSearch(next, current, visited, cycleEdges)) {
                        cycleEdges.add(edge);
                        return true;
                    }
                } else if (!next.equals(parent)) {
                    // Si le mot suivant a déjà été visité et n’est pas le parent actuel,
                    // cela signifie qu’un cycle a été détecté, ajouté l’arête actuelle au cycle.
                    cycleEdges.add(edge);
                    return true;
                }
            }
        }
        // Si aucun cycle n’a été détecté à partir du mot actuel, retourner false
        return false;
    }

    // Méthode publique pour trouver les arêtes formant un cycle
    public List<Edge> findCycleEdges() {
        // Créer un ensemble pour suivre les mots visités
        Set<Word> visited = new HashSet<>();

        // Créer une liste pour stocker les arêtes formant un cycle
        List<Edge> cycleEdges = new ArrayList<>();

        // Parcourir toutes les arêtes de l’arbre (MST)
        for (Edge edge : edgesMST) {
            Word word1 = edge.sourceWord();

            // Vérifier si le mot word1 n’a pas déjà été visité
            if (!visited.contains(word1)) {
                // Appeler la méthode dfs pour rechercher un cycle à partir de word1
                if (depthFirstSearch(word1, null, visited, cycleEdges)) {
                    // Si un cycle est trouvé, sortir de la boucle
                    break;
                }
            }
        }
        // Retourner la liste des arêtes formant un cycle
        return cycleEdges;
    }

    // Méthode pour supprimer l’arête avec la similarité la plus faible dans un cycle
    public void removeLowestSimilarityEdgeInCycle() {
        List<Edge> cycleEdges = findCycleEdges();
        if (!cycleEdges.isEmpty()) {
            // Trouver la similarité minimale parmi les arêtes du cycle
            double minSimilarity = cycleEdges.stream()
                    .mapToDouble(Edge::similarity)
                    .min()
                    .orElse(Double.MAX_VALUE);

            // Filtrer les arêtes avec cette similarité minimale
            List<Edge> lowestSimilarityEdges = cycleEdges.stream()
                    .filter(edge -> edge.similarity() == minSimilarity)
                    .toList();

            // Choisir et supprimer une arête au hasard parmi celles ayant la similarité la plus faible
            if (!lowestSimilarityEdges.isEmpty()) {
                Random random = new Random();
                Edge edgeToRemove = lowestSimilarityEdges.get(random.nextInt(lowestSimilarityEdges.size()));
                removeEdge(edgeToRemove);
            }
        }
    }

    // Méthode pour connaître si le mot à ajouter a été ajouter ou non dans le MST
    public boolean isWordAdded(Word word) { // Retourne vrai si le mot a été ajouté
        return edgesMST.stream()
                .anyMatch(edge -> edge.sourceWord().equals(word) || edge.targetWord().equals(word));
    }

    private boolean containsBannedWord(Edge edge) {
        // Vérifier si l’arête contient un mot interdit
        Word sourceWord = edge.sourceWord();
        Word targetWord = edge.targetWord();
        return bannedWords.contains(sourceWord) || bannedWords.contains(targetWord);
    }

    public void addWord(Map<Word, List<Edge>> addWordAndEdges) {
        // Créer une liste pour stocker les nouvelles arêtes à ajouter
        List<Edge> addEdges = new ArrayList<>(addWordAndEdges.values().iterator().next());
        // Créer un mot pour stocker le mot à ajouter
        Word addingWord = addWordAndEdges.keySet().iterator().next();
        // Trier les nouvelles arêtes par similarité décroissante
        addEdges.sort(Comparator.comparingDouble(Edge::similarity).reversed());

        // Parcourir chaque nouvelle arête
        for (Edge edgeToAdd : addEdges) {
            // Vérifier si l’arête contient un mot interdit
            if (containsBannedWord(edgeToAdd)) {
                continue; // Ignorer cette arête et passer à la suivante
            }

            // Ajouter l’arête au graphe
            addEdge(edgeToAdd);

            // Vérifier si l’ajout de cette arête a créé un cycle
            List<Edge> cycleEdges = findCycleEdges();

            // Si un cycle est détecté
            if (!cycleEdges.isEmpty()) {
                // Supprimer l’arête de similarité minimale dans le cycle
                removeLowestSimilarityEdgeInCycle();
            }

            // Vérifier si l’arête ajoutée possède une similarité plus faible
            // que la similarité minimale entre les nœuds de l’arbre
            double minSimilarityInMST = this.getEdgesMST().stream()
                    .mapToDouble(Edge::similarity)
                    .min()
                    .orElse(Double.MIN_VALUE);

            if (edgeToAdd.similarity() < minSimilarityInMST) {
                // Inutile de continuer pour les arêtes restantes
                break;
            }
        }

        // Ajouter le mot à la liste des mots interdits
        if (!isWordAdded(addingWord)) {
            bannedWords.add(addingWord);
        }
    }
}
