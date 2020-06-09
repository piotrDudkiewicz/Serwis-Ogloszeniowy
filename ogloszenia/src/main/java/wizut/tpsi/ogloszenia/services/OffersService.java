/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wizut.tpsi.ogloszenia.services;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import wizut.tpsi.ogloszenia.jpa.BodyStyle;
import wizut.tpsi.ogloszenia.jpa.CarManufacturer;
import wizut.tpsi.ogloszenia.jpa.CarModel;
import wizut.tpsi.ogloszenia.jpa.FuelType;
import wizut.tpsi.ogloszenia.jpa.Offer;
import wizut.tpsi.ogloszenia.web.OfferFilter;

/**
 *
 * @author Pioterk
 */
@Service
@Transactional
public class OffersService {

    @PersistenceContext
    private EntityManager em;

    public CarManufacturer getCarManufacturer(int id) {
        return em.find(CarManufacturer.class, id);
    }

    public CarModel getCarModel(int id) {
        return em.find(CarModel.class, id);
    }

    public List<CarManufacturer> getCarManufacturers() {
        String jpql = "select cm from CarManufacturer cm order by cm.name";
        TypedQuery<CarManufacturer> query = em.createQuery(jpql, CarManufacturer.class);
        List<CarManufacturer> result = query.getResultList();
        return result;
    }

    public List<BodyStyle> getBodyStyles() {
        String jpql = "select cm from BodyStyle cm order by cm.name";
        TypedQuery<BodyStyle> query = em.createQuery(jpql, BodyStyle.class);
        List<BodyStyle> result = query.getResultList();
        return result;
    }

    public List<FuelType> getFuelTypes() {
        String jpql = "select cm from FuelType cm order by cm.name";
        TypedQuery<FuelType> query = em.createQuery(jpql, FuelType.class);
        List<FuelType> result = query.getResultList();
        return result;
    }

    public List<CarModel> getCarModels() {
        String jpql = "select cm from CarModel cm order by cm.name";
        TypedQuery<CarModel> query = em.createQuery(jpql, CarModel.class);
        List<CarModel> result = query.getResultList();
        return result;
    }

    public List<CarModel> getCarModels(int manufacturerId) {
        String jpql = "select cm from CarModel cm where cm.manufacturer.id = :id order by cm.name";

        TypedQuery<CarModel> query = em.createQuery(jpql, CarModel.class);
        query.setParameter("id", manufacturerId);

        return query.getResultList();
    }

    public List<Offer> getOffers() {
        String jpql = "select cm from Offer cm order by cm.title";
        TypedQuery<Offer> query = em.createQuery(jpql, Offer.class);
        List<Offer> result = query.getResultList();
        return result;
    }

    public List<Offer> getOffersByModel(int modelId) {
        String jpql = "select cm from Offer cm where cm.model.id = :id order by cm.title";

        TypedQuery<Offer> query = em.createQuery(jpql, Offer.class);
        query.setParameter("id", modelId);

        return query.getResultList();
    }

    public List<Offer> getOffersByManufacturer(int manufacturerId) {
        String jpql = "select cm from Offer cm where cm.model.manufacturer.id = :id order by cm.title";

        TypedQuery<Offer> query = em.createQuery(jpql, Offer.class);
        query.setParameter("id", manufacturerId);

        return query.getResultList();
    }

    public Offer getOffer(int id) {
        return em.find(Offer.class, id);
    }

    public Page<Offer> getOffers(OfferFilter filter, Pageable offersPage) {
        int pageSize = offersPage.getPageSize();
        int actualPage = offersPage.getPageNumber();
        int start = pageSize * actualPage;

        String jpql = "select cm from Offer cm";
        List<String> temp = new ArrayList<>();

        Integer manufacturerId = filter.getManufacturerId();
        Integer modelId = filter.getModelId();
        Integer fuelId = filter.getFuelTypeId();
        Integer yearFrom = filter.getYearFrom();
        Integer yearTo = filter.getYearTo();
        String sortOption = filter.getSortOption();
        String description = filter.getDescription();

        if (manufacturerId != null) {
            temp.add(" cm.model.manufacturer.id = " + manufacturerId);
        }

        if (modelId != null) {
            temp.add(" cm.model.id = " + modelId);
        }

        if (yearFrom != null) {
            temp.add(" cm.year >= " + yearFrom);
        }

        if (yearTo != null) {
            temp.add(" cm.year <= " + yearTo);
        }

        if (fuelId != null) {
            temp.add(" cm.fuelType.id <= " + fuelId);
        }

        if (description != null) {
            temp.add(" cm.description Like '%" + description + "%'");
        }

        if (temp.size() != 0) {
            jpql += " where";
            jpql += temp.get(0);
            for (int x = 1; x < temp.size(); x++) {
                jpql += " AND" + temp.get(x);
            }
        }

        System.out.println(jpql);

        if (sortOption != null) {
            switch (sortOption) {
                case "dateUp":
                    jpql += " order by cm.date";
                    break;
                case "dateDown":
                    jpql += " order by cm.date DESC";
                    break;
                case "priceUp":
                    jpql += " order by cm.price";
                    break;
                case "priceDown":
                    jpql += " order by cm.price DESC";
                    break;
                case "yearUp":
                    jpql += " order by cm.year";
                    break;
                case "yearDown":
                    jpql += " order by cm.year DESC";
                    break;
                default:
                    jpql += " order by cm.id ASC";
            }
        }

        TypedQuery<Offer> query = em.createQuery(jpql, Offer.class);
        List<Offer> list = query.getResultList();
        int size = list.size();
        query.setFirstResult(start);
        query.setMaxResults(pageSize);

        list = query.getResultList();

        return new PageImpl<Offer>(list, PageRequest.of(actualPage, pageSize), size);
    }

    public Offer createOffer(Offer offer) {
        em.persist(offer);
        return offer;
    }

    public Offer deleteOffer(Integer id, Integer user) {
        Offer offer = em.find(Offer.class, id);
        if (offer.getUser().getId() == user) {
            em.remove(offer);
            return offer;
        }

        return null;
    }

    public Offer saveOffer(Offer offer) {
        return em.merge(offer);
    }
}
