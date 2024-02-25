package fr.uge.tree;

import java.util.Objects;

/**
 * Classe qui représente un mot dans un arbre recouvrant maximal
 * @param word Mot
 * word : Représente un mot dans l’arbre recouvrant maximal
 * lang = fr
 * @see MaximumSpanningTree
 * @see Edge
 * @author Mamadou BA
 * @author Cédric MARIYA CONSTANTINE
 * @author Abdelrahim RICHE
 * @author Vincent SOUSA
 * @author Yacine ZEMOUCHE
 */
public record Word(String word) {

    /**
     * @return the word
     *
     * Méthode toString pour afficher un mot
     */
    @Override
    public String toString() {
        return word;
    }

    /**
     * @param o
     * o : Objet à comparer
     * @return
     *
     * Méthode equals pour comparer deux mots
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word1 = (Word) o;
        return Objects.equals(word, word1.word);
    }
}
