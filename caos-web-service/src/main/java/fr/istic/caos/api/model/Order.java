package fr.istic.caos.api.model;

import java.util.Date;

public class Order implements Entity {
	/**
	 * L'identifiant unique de la commande
	 */
	private int id;

	/**
	 * La date de commande
	 */
	private Date datetime;

	/**
	 * La commande a-t-elle été retirée ?
	 */
	private boolean taken;

	/**
	 * La référence du produit commandé
	 */
	private Product produit;

	/**
	 * La référence de la personne qui commande
	 */
	private Customer client;

	/**
	 * Créé une commande
	 * @param id L'identifiant
	 * @param datetime La date
	 * @param product La référence produit
	 * @param customer La référence client
	 */
	public Order(int id, Date datetime, boolean taken, Product product, Customer customer) {
		this.id = id;
		this.datetime = datetime;
		this.taken = taken;
		this.produit = product;
		this.client = customer;
	}

	/**
	 * Créé une commande
	 */
	public Order() {  }

	@Override
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}

	public boolean isTaken() {
		return taken;
	}

	public void setTaken(boolean taken) {
		this.taken = taken;
	}

	public Product getProduit() {
		return produit;
	}

	public void setProduit(Product produit) {
		this.produit = produit;
	}

	public Customer getClient() {
		return client;
	}

	public void setClient(Customer client) {
		this.client = client;
	}
}
