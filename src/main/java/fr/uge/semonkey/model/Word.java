package fr.uge.semonkey.model;

import java.util.Objects;

/**
 * Classe qui représente un mot dans un arbre recouvrant maximal
 *
 * @param word Mot
 *             word : Représente un mot dans l’arbre recouvrant maximal
 * @author Mamadou BA
 * @author Cédric MARIYA CONSTANTINE
 * @author Abdelrahim RICHE
 * @author Vincent SOUSA
 * @author Yacine ZEMOUCHE
 */
public record Word(String word) {

    /**
     * Méthode toString pour afficher un mot
     *
     * @return the word
     */
    @Override
    public String toString() {
        return word;
    }

    /**
     * Méthode equals pour comparer deux mots
     *
     * @param o o : Objet à comparer
     * @return Méthode equals pour comparer deux mots
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word1 = (Word) o;
        return Objects.equals(word, word1.word);
    }
}
