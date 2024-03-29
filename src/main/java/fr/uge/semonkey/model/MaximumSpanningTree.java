package fr.uge.semonkey.model;

import fr.uge.semonkey.config.FileLine;
import fr.uge.semonkey.filemanagement.SpanningTreeSerializer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.PosixFilePermission;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe pour représenter un arbre recouvrant maximal (MST) et effectuer des opérations sur cet arbre.
 *
 * @author Mamadou BA
 * @author Cédric MARIYA CONSTANTINE
 * @author Abdelrahim RICHE
 * @author Vincent SOUSA
 * @author Yacine ZEMOUCHE
 */
public class MaximumSpanningTree implements SpanningTreeSerializer {
    /**
     * Mot de départ
     */
    private final Word startWord;
    /**
     * Mot de fin
     */
    private final Word endWord;
    /**
     * Arêtes de l’arbre de recouvrement minimal
     */
    private final List<Edge> edges;
    /**
     * Ensemble de mots interdits
     */
    private final Set<Word> bannedWords;
    private final Graph graph;

    /**
     * Constructeur pour initialiser un arbre recouvrant maximal
     *
     * @param startWord   Mot de départ
     * @param endWord     Mot de fin
     * @param edges       Arêtes de l’arbre de recouvrement minimal
     * @param bannedWords Mots interdits
     */
    public MaximumSpanningTree(Word startWord, Word endWord, List<Edge> edges, Set<Word> bannedWords) {
        this.edges = edges;
        this.startWord = startWord;
        this.endWord = endWord;
        this.bannedWords = bannedWords;
        this.graph = new Graph();
        for (Edge edge : edges) {
            graph.addEdge(edge);
        }
    }

    /**
     * Constructeur pour initialiser un arbre recouvrant maximal sans arêtes
     *
     * @param startWord Mot de départ
     * @param endWord   Mot de fin
     */
    public MaximumSpanningTree(Word startWord, Word endWord) {
        this(startWord, endWord, new ArrayList<>(), new HashSet<>());
    }

    public MaximumSpanningTree() {
        this(null, null, new ArrayList<>(), new HashSet<>());
    }

    /**
     * Méthode pour diviser les mots et la similarité
     *
     * @param edges Liste d’arêtes
     * @param parts Parties de la ligne
     */
    private static void splitWordsAndSimilarity(List<Edge> edges, String[] parts) {
        String[] words = parts[0].split(FileLine.WORDS_SEPARATOR_OUTPUT.line); // Diviser les mots de l’arête
        // Créer les mots source et cible
        Word sourceWord = new Word(words[0]);
        Word targetWord = new Word(words[1]);
        double similarity = Double.parseDouble(parts[1]); // Récupérer la similarité
        edges.add(new Edge(sourceWord, similarity, targetWord)); // Ajouter l’arête à la liste
    }

    /**
     * Méthode pour le premier fichier et le MaximumSpanningTree du premier tour (soit mot de départ et mot de fin et la seule arête)
     *
     * @param fileC Chemin du fichier
     * @return MaximumSpanningTree
     * @throws IOException Charge un arbre recouvrant maximal à partir d’un fichier
     */
    public static MaximumSpanningTree createMaximumSpanningTree(String fileC) throws IOException {
        List<Edge> edges = new ArrayList<>(); // Créer une liste pour stocker les arêtes
        Path readerPath = Path.of(fileC); // Créer un objet Path pour le fichier
        BufferedReader br = Files.newBufferedReader(readerPath); // Créer un objet BufferedReader pour lire le fichier
        br.readLine(); // Ligne 1 : "Mots de départ :"
        Word startWord = new Word(br.readLine().split(FileLine.SIMILARITY_SEPARATOR.line)[0].trim()); // Ligne 2 : "voiture,561464"
        Word endWord = new Word(br.readLine().split(FileLine.SIMILARITY_SEPARATOR.line)[0].trim()); // Ligne 3 : "bus,1715044"
        br.readLine(); // Ligne 4 : "Liste des mots :"
        br.readLine(); // Ligne 5 : "voiture, offset: 561464"
        br.readLine(); // Ligne 6 : "bus, offset: 1715044"
        br.readLine(); // Ligne 7 : "Distance entre les mots :"
        String[] parts = br.readLine().split(FileLine.SIMILARITY_C_FILE_SEPARATOR.line); // Ligne 8 : "voiture_bus, distance:0.5"
        splitWordsAndSimilarity(edges, parts); // Ajouter l’arête à la liste
        br.close(); // Fermer le fichier
        return new MaximumSpanningTree(startWord, endWord, edges, new HashSet<>()); // Retourner un nouvel objet MaximumSpanningTree
    }

    /**
     * Méthode pour obtenir une représentation textuelle de l’arbre recouvrant maximal
     *
     * @return String Représentation textuelle de l’arbre recouvrant maximal
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(); // Créer un objet StringBuilder
        sb.append(FileLine.MAXIMUM_SPANNING_TREE.line).append("\n"); // Ajouter la ligne "MaximumSpanningTree :"
        // Ajouter les mots de départ et de fin
        sb.append(FileLine.START_WORD.line).append(startWord).append("\n");
        sb.append(FileLine.END_WORD.line).append(endWord).append("\n");
        // Ajouter les arêtes de l’arbre
        sb.append(FileLine.EDGES_MST.line).append("\n");
        for (Edge edge : edges) { // Parcourir chaque arête
            sb.append(String.format(FileLine.EDGE_FORMAT.line, edge.sourceWord().word(), edge.targetWord(), edge.similarity())).append("\n");
        }
        // Ajouter les mots interdits
        sb.append(FileLine.BANNED_WORDS.line).append("\n");
        for (Word word : bannedWords) { // Parcourir chaque mot interdit
            sb.append(word.word()).append("\n");
        }
        sb.append(FileLine.EOF.line);
        return sb.toString();
    }

    /**
     * Méthode pour obtenir le mot de départ
     *
     * @return Word Mot de départ
     */
    @Override
    public Word getStartWord() {
        return startWord;
    }

    /**
     * Méthode pour obtenir le mot de fin
     *
     * @return Word Mot de fin
     */
    @Override
    public Word getEndWord() {
        return endWord;
    }

    /**
     * Méthode pour obtenir les arêtes de l’arbre de recouvrement minimal
     *
     * @return La liste d'arêtes de l’arbre de recouvrement minimal
     */
    @Override
    public List<Edge> getEdges() {
        return edges;
    }

    /**
     * Méthode pour obtenir les mots interdits
     *
     * @return l'ensemble de mots interdits
     */
    public Set<Word> getBannedWords() {
        return bannedWords;
    }

    /**
     * Méthode pour ajouter une arête à l’arbre recouvrant maximal
     *
     * @param edge Arête à ajouter
     */
    public void addEdge(Edge edge) {
        edges.add(edge);
        graph.addEdge(edge);
    }

    /**
     * Méthode pour supprimer une arête de l’arbre recouvrant maximal
     *
     * @param edge Arête à supprimer
     */
    public void removeEdge(Edge edge) {
        edges.remove(edge);
        graph.removeEdge(edge);
    }

    /**
     * Méthode pour ajouter les arêtes d’un mot à l’arbre recouvrant maximal
     *
     * @param file Chemin du fichier
     */
    public void loadAddEdges(String file) throws IOException {
        Word addWord = null; // Créer un mot pour stocker le mot à ajouter
        List<Edge> edges = new ArrayList<>(); // Créer une liste pour stocker les arêtes
        Path filePath = Path.of(file); // Créer un objet Path pour le fichier
        BufferedReader br = Files.newBufferedReader(filePath); // Créer un objet BufferedReader pour lire le fichier
        br.readLine(); // Ligne 1 : "Mots de départ :"
        br.readLine(); // Ligne 2 : "voiture,561464"
        br.readLine(); // Ligne 3 : "bus,1715044"
        br.readLine(); // Ligne 4 : "Liste des mots :"
        // Parcourir les lignes jusqu’à la ligne "Distance entre les mots :"
        String line;
        while (!Objects.equals(line = br.readLine(), FileLine.DISTANCE_BETWEEN_WORDS.line)) {
            // On récupère les mots uniquement
            String[] words = line.split(FileLine.SIMILARITY_SEPARATOR.line);
            addWord = new Word(words[0]);
        }
        br.readLine(); // Ligne 5 : "Distance entre les mots :"
        Word sourceWord;
        Word targetWord;
        double similarity;
        // Parcourir les lignes jusqu’à la fin du fichier
        while ((line = br.readLine()) != null) {
            assert addWord != null;
            if (line.contains(addWord.word())) {
                String[] parts = line.split(FileLine.SIMILARITY_C_FILE_SEPARATOR.line); // Diviser la ligne en parties (mots et similarité)
                sourceWord = new Word(parts[0].split(FileLine.WORDS_SEPARATOR_OUTPUT.line)[0]);
                targetWord = new Word(parts[0].split(FileLine.WORDS_SEPARATOR_OUTPUT.line)[1]);
                similarity = Double.parseDouble(parts[1]);
                edges.add(new Edge(sourceWord, similarity, targetWord)); // Ajouter l’arête à la liste
            }
        }
        br.close(); // Fermer le fichier
        if (addWord == null) throw new AssertionError();
        // Créer une carte pour stocker le mot à ajouter et les arêtes et appeler la méthode addWord
        Map<Word, List<Edge>> wordMap = new HashMap<>(Map.of(addWord, edges));
        System.out.println("wordMap : " + wordMap);
        addWord(wordMap);
    }

    private void depthFirstSearchHelper(Word current, Word parent, Set<Word> visited, List<Edge> cycleEdges) {
        visited.add(current);
        List<Edge> adjacentEdges = graph.getAdjacentEdges(current);

        for (Edge edge : adjacentEdges) {
            Word next = (edge.sourceWord().equals(current)) ? edge.targetWord() : edge.sourceWord();
            if (!visited.contains(next)) {
                depthFirstSearchHelper(next, current, visited, cycleEdges);
            } else if (!next.equals(parent)) {
                // Cycle detected, add the edge to cycleEdges list
                cycleEdges.add(edge);
            }
        }
    }

    private boolean depthFirstSearch(Word current, Word parent, Set<Word> visited, List<Edge> cycleEdges, Map<Word, List<Edge>> adjacencyMap) {
        // Marquer le mot actuel comme visité
        visited.add(current);
        // Récupérer les arêtes voisines du nœud actuel depuis la map d'adjacence
        List<Edge> neighbors = adjacencyMap.getOrDefault(current, Collections.emptyList());

        // Parcourir toutes les arêtes voisines
        for (Edge edge : neighbors) {
            Word next = null;
            // Déterminer le nœud voisin
            if (edge.sourceWord().equals(current)) {
                next = edge.targetWord();
            } else if (edge.targetWord().equals(current)) {
                next = edge.sourceWord();
            }
            // Si un nœud voisin est trouvé
            if (next != null) {
                // Vérifier si le nœud voisin n'a pas déjà été visité
                if (!visited.contains(next)) {
                    // Continuer la recherche en profondeur à partir du nœud voisin
                    // Si un cycle est trouvé, ajouter l’arête actuelle à la liste des arêtes du cycle
                    // et retourner true pour indiquer la détection d’un cycle
                    if (depthFirstSearch(next, current, visited, cycleEdges, adjacencyMap)) {
                        cycleEdges.add(edge);
                        return true;
                    }
                } else if (!next.equals(parent)) {
                    // Si le nœud voisin a déjà été visité et n’est pas le parent actuel,
                    // cela signifie qu’un cycle a été détecté, ajouter l’arête actuelle au cycle.
                    cycleEdges.add(edge);
                    return true;
                }
            }
        }
        // Si aucun cycle n’a été détecté à partir du nœud actuel, retourner false
        return false;
    }

    /**
     * Méthode pour trouver les arêtes formant un cycle dans l’arbre recouvrant maximal.
     * Cette méthode effectue une recherche DFS pour détecter un cycle dans l'arbre
     * et collecte les arêtes du cycle le cas échéant.
     *
     * @return La liste des arêtes formant un cycle dans l'arbre recouvrant maximal.
     */
    private List<Edge> findCycleEdges() {
        // Créer un ensemble pour suivre les mots visités
        Set<Word> visited = new HashSet<>();

        // Créer une liste pour stocker les arêtes formant un cycle
        List<Edge> cycleEdges = new ArrayList<>();

        // Créer une map pour indexer les arêtes par nœud de départ et d'arrivée
        Map<Word, List<Edge>> adjacencyMap = new HashMap<>();
        for (Edge edge : edges) {
            adjacencyMap.computeIfAbsent(edge.sourceWord(), k -> new ArrayList<>()).add(edge);
            adjacencyMap.computeIfAbsent(edge.targetWord(), k -> new ArrayList<>()).add(edge);
        }

        // Parcourir toutes les arêtes de l’arbre (MST)
        for (Edge edge : edges) {
            Word word1 = edge.sourceWord();

            // Vérifier si le mot word1 n’a pas déjà été visité
            if (!visited.contains(word1)) {
                // Appeler la méthode dfs pour rechercher un cycle à partir de word1
                if (depthFirstSearch(word1, null, visited, cycleEdges, adjacencyMap)) {
                    // Si un cycle est trouvé, sortir de la boucle
                    break;
                }
            }
        }
        // Retourner la liste des arêtes formant un cycle
        return cycleEdges;
    }

    /**
     * Méthode pour supprimer l’arête avec la similarité la plus faible dans un cycle
     */
    private void removeLowestSimilarityEdgeInCycle() {
        List<Edge> cycleEdges = findCycleEdges(); // Trouver les arêtes formant un cycle
        if (!cycleEdges.isEmpty()) { // Si un cycle est trouvé
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
            if (!lowestSimilarityEdges.isEmpty()) { // Dans le cas où il y a plusieurs arêtes avec la même similarité minimale
                Random random = new Random();
                Edge edgeToRemove = lowestSimilarityEdges.get(random.nextInt(lowestSimilarityEdges.size()));

                // Vérifier si l'arête à supprimer est la seule arête connectée au mot de départ ou au mot cible
                long countStart = edges.stream()
                        .filter(edge -> edge.sourceWord().equals(startWord) || edge.targetWord().equals(startWord))
                        .count();
                long countEnd = edges.stream()
                        .filter(edge -> edge.sourceWord().equals(endWord) || edge.targetWord().equals(endWord))
                        .count();

                if ((countStart > 1 || (!edgeToRemove.sourceWord().equals(startWord) && !edgeToRemove.targetWord().equals(startWord))) &&
                        (countEnd > 1 || (!edgeToRemove.sourceWord().equals(endWord) && !edgeToRemove.targetWord().equals(endWord)))) {
                    removeEdge(edgeToRemove);
                } else {
                    // Trouver une autre arête à supprimer
                    cycleEdges.stream()
                            .filter(edge -> edge != edgeToRemove)
                            .min(Comparator.comparingDouble(Edge::similarity)).ifPresent(this::removeEdge);

                }
            }
        }
    }

    /**
     * Méthode pour connaître si le mot à ajouter a été ajouter ou non dans le MST
     *
     * @param word Mot à ajouter
     * @return boolean Vérifie si un mot a été ajouté à l’arbre recouvrant maximal
     */
    private boolean isWordAdded(Word word) {
        // Retourne vrai si le mot a été ajouté à l’arbre
        return edges.stream()
                .anyMatch(edge -> edge.sourceWord().equals(word) || edge.targetWord().equals(word));
    }

    /**
     * Méthode pour vérifier si une arête contient un mot interdit
     *
     * @param edge Arête à vérifier
     * @return boolean Vérifie si une arête contient un mot interdit
     */
    private boolean containsBannedWord(Edge edge) {
        // Vérifier si l’arête contient un mot interdit
        Word sourceWord = edge.sourceWord();
        Word targetWord = edge.targetWord();
        return bannedWords.contains(sourceWord) || bannedWords.contains(targetWord);
    }

    /**
     * Méthode pour ajouter un mot à l’arbre recouvrant maximal
     *
     * @param addWordAndEdges Mot à ajouter et ses arêtes
     *                        <p>
     *                        Ajoute un mot à l’arbre recouvrant maximal
     */
    public void addWord(Map<Word, List<Edge>> addWordAndEdges) {
        // Créer une liste pour stocker les nouvelles arêtes à ajouter
        List<Edge> addEdges = addWordAndEdges.values().iterator().next();
        // Créer un mot pour stocker le mot à ajouter
        Word addingWord = addWordAndEdges.keySet().iterator().next();

        // Enlever les arêtes dont la similarité est inférieure à la similarité minimale entre les nœuds de l’arbre
        addEdges.removeIf(edge -> edge.similarity() < edges.stream()
                .mapToDouble(Edge::similarity)
                .min()
                .orElse(Double.MIN_VALUE));
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
            double minSimilarityInMST = edges.stream()
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

    @Override
    public SpanningTreeSerializer deserialize(String fileName) throws IOException {
        List<Edge> edgesMST = new ArrayList<>();
        Set<Word> bannedWords = new HashSet<>();
        Path filePath = Path.of(fileName);
        BufferedReader br = Files.newBufferedReader(filePath);
        String line;
        br.readLine(); // Ligne 1 : "MaximumSpanningTree :"
        String startWord = br.readLine().split(FileLine.FIELDS_SEPARATOR.line)[1].trim(); // Ligne 2 : "startWord : word1"
        String endWord = br.readLine().split(FileLine.FIELDS_SEPARATOR.line)[1].trim(); // Ligne 3 : "endWord : word2"

        br.readLine(); // Ligne 4 : "edgesMST :"
        // Parcourir les lignes jusqu’à la ligne "bannedWords :" (si il n'y a pas de mots interdits on passe directement à la ligne EOF)
        while (!Objects.equals(line = br.readLine(), FileLine.BANNED_WORDS.line)) {
            Pattern pattern = Pattern.compile("^(\\w+)_(\\w+),(\\d+\\.\\d+)$");
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                Word sourceWord = new Word(matcher.group(1));
                Word targetWord = new Word(matcher.group(2));
                double similarity = Double.parseDouble(matcher.group(3));
                edgesMST.add(new Edge(sourceWord, similarity, targetWord)); // Ajouter l’arête à la liste
            }
        }
        // Parcourir les lignes jusqu’à la fin du fichier
        while (!Objects.equals(line = br.readLine(), FileLine.EOF.line)) {
            bannedWords.add(new Word(line));
        }
        br.close();
        return new MaximumSpanningTree(new Word(startWord), new Word(endWord), edgesMST, bannedWords);
    }

    @Override
    public void serialize(String fileName) throws IOException {
        // Créer un objet Path pour le fichier et un objet BufferedWriter pour écrire dans le fichier
        Path path = Paths.get(fileName);
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.WRITE)) { // Créer un objet BufferedWriter pour écrire dans le fichier
            bufferedWriter.write(toString()); // Écrire l’arbre recouvrant maximal dans le fichier
            bufferedWriter.close(); // Fermer le fichier
            // Définir les permissions
            Set<PosixFilePermission> perms = Set.of(
                    PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_WRITE,
                    PosixFilePermission.GROUP_READ, PosixFilePermission.GROUP_WRITE,
                    PosixFilePermission.OTHERS_READ, PosixFilePermission.OTHERS_WRITE
            );
            Files.setPosixFilePermissions(path, perms);
        } catch (IOException e) {
            // Handle exception
            throw new IOException("Erreur dans l'écriture du fichier", e);
        }
    }
}
