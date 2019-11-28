package fr.istic.caos;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Classe utilitaire
 */
public class Utils {

    /**
     * Indique si la chaîne de caractère passée en argument est vide.
     * @param string Le chaîne à tester
     * @return true si la chaîne est nulle ou représente une chaine vide.
     */
    public static boolean isEmpty(String string) {
        return string == null || string.trim().isEmpty();
    }

    /**
     * Permet de convertir une représentation de date au format ISO 8601 (utilisé dans le format JSON)
     * en un objet Date utilisable en Java
     * @param date La représentation de date
     * @return L'objet Date
     */
    public static Date date(String date) {
        Instant instant = Instant.from(DateTimeFormatter.ISO_DATE_TIME.parse(date));
        return Date.from(instant);
    }
}
