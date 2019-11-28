package fr.istic.caos.api.controllers;

import fr.istic.caos.api.dto.ProductTO;
import fr.istic.caos.api.errors.BadRequestException;
import fr.istic.caos.api.errors.WebServiceException;
import fr.istic.caos.api.managers.impl.ManagerFactory;
import fr.istic.caos.api.model.Product;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static fr.istic.caos.Utils.isEmpty;

/**
 * Le contrôleur REST pour les produits.
 */
@Path("products")
public class Products extends AbstractController<Product> {

	/**
	 * Constructeur du contrôleur pour /products
	 */
	public Products() {
		super(ManagerFactory.getProductManager());
	}

	/**
	 * Récupère l'intégralité des types de produit enregistrés dans le Web service.
	 * @return Une liste de ProductTO
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ProductTO> getAllProducts(){
		List<ProductTO> products = new ArrayList<>();
		for(Product p : manager.getAll()) {
			products.add(createProductTo(p));
		}
		return products;
	}

	/**
	 * Récupère un type de produit à partir de l'identifiant fourni dans la requête.
	 * @param pId L'identifiant du type de produit (paramètre de la requête)
	 * @return Une réponse encapsulant un ProductTO, ou une erreur si un problème survient.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public Response getProductById(@PathParam("id") int pId) {
		try {
			Product p = manager.get(pId);
			return getResponse(200, createProductTo(p));
		} catch (WebServiceException ex) {
			return ex.toResponse();
		}
	}

	/**
	 * Créé un type de produit
	 * @param pTO L'objet produit à créer (DTO issu du corps de la requête en JSON)
	 * @return La réponse encapsulant l'objet créé, ou une erreur si un problème survient
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createProduct(ProductTO pTO) {
		try {
			checkProductTo(pTO);
			Product p = new Product(pTO.id, pTO.p_name, pTO.price, pTO.initStock, pTO.stock, pTO.ingredients);
			p = manager.create(p);
			return getResponse(201, createProductTo(p));
		} catch (WebServiceException ex) {
			return ex.toResponse();
		}
	}

	/**
	 * Modifie un type de produit.
	 * @param id L'identifiant du type de produit à modifier
	 * @param updates Les modifications partielles à apporter au type de produit
	 * @return Une réponse encapsulant l'objet modifié, ou une erreur si un problème survient
	 */
	@PUT
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response modifyProduct(@PathParam("id") int id, ProductTO updates) {
		try {
			Product p = manager.get(id);

			if (!isEmpty(updates.p_name)) {
				p.setName(updates.p_name);
			}

			if (updates.price != 0) {
				if (updates.price < 0) {
					throw new BadRequestException("Le prix ne peut pas être négatif");
				}
				p.setPrice(updates.price);
			}

			if (updates.initStock != 0) {
				if (updates.initStock < 0) {
					throw new BadRequestException("Le stock initial ne peut pas être négatif");
				}
				p.setInitStock(updates.initStock);
			}

			if (updates.stock != 0) {
				if (updates.stock < 0) {
					throw new BadRequestException("Le stock ne peut pas être négatif");
				}
				p.setStock(updates.stock);
			}

			if (!isEmpty(updates.ingredients)) {
				p.setIngredients(updates.ingredients);
			}

			return getResponse(200, createProductTo(p));
		} catch (WebServiceException ex) {
			return ex.toResponse();
		}
	}

	/**
	 * Supprime un type de produit.
	 * @param id L'identifiant du produit à supprimer
	 * @return Une réponse portant un code de retour suivant le déroulé de l'opération
	 */
	@DELETE
	@Path("{id}")
	public Response removeProduct(@PathParam("id") int id) {
		try {
			manager.delete(id);
			return Response.noContent().build();
		} catch (WebServiceException ex) {
			return ex.toResponse();
		}
	}

	/**
	 * Créer un ProductDTO à partir d'un objet interne Product.
	 * @param p L'objet Product
	 * @return Une instance de ProductTO
	 */
	private ProductTO createProductTo(Product p) {
		ProductTO dto = new ProductTO();
		dto.id = p.getId();
		dto.p_name = p.getName();
		dto.price = p.getPrice();
		dto.stock = p.getStock();
		dto.initStock = p.getInitStock();
		dto.ingredients = p.getIngredients();
		return dto;
	}

	/**
	 * Vérifie que le produit fourni par le client est correct.
	 * @param dto L'objet DTO à contrôler
	 * @throws WebServiceException Des exceptions de ce type sont levées lorsque certains attributs de la requête sont
	 *   manquants ou incorrects.
	 */
	private void checkProductTo(ProductTO dto) throws WebServiceException {
		if(isEmpty(dto.p_name)) {
			throw new BadRequestException("Vous devez fournir un nom");
		}

		if(dto.price<0) {
			throw new BadRequestException("Vous devez fournir un prix");
		}

		if(dto.initStock<0) {
			throw new BadRequestException("Vous devez fournir un stock initial");
		}

		if(dto.stock<0) {
			throw new BadRequestException("Vous devez fournir une valeur pour le stock");
		}

		if(isEmpty(dto.ingredients)) {
			throw new BadRequestException("Vous devez fournir la liste des ingrédients");
		}
	}
}