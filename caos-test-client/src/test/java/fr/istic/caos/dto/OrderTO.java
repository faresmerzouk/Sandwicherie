package fr.istic.caos.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class OrderTO {
    /**
     * L'identifiant unique de la commande
     */
    public int id;

    /**
     * La date de commande
     */
    public String datetime;

    /**
     * La référence du produit commandé
     */
    public int product_id;

    /**
     * La référence de la personne qui commande
     */
    public int customer_id;
}
