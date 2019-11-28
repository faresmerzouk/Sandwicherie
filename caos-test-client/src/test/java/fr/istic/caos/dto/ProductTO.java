package fr.istic.caos.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ProductTO {
    /**
     * L'identifiant unique
     */
    public int id;

    /**
     * Le nom du produit
     */
    public String p_name;

    /**
     * Le prix
     */
    public float price;

    /**
     * Le stock disponible
     */
    public int stock;

    /**
     * Le stock initial
     */
    public int initStock;

    /**
     * La liste des ingrédients
     */
    public String ingredients;
}
