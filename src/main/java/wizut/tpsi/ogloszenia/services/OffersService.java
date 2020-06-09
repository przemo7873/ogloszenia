package wizut.tpsi.ogloszenia.services;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import wizut.tpsi.ogloszenia.jpa.BodyStyle;
import wizut.tpsi.ogloszenia.jpa.CarManufacturer;
import wizut.tpsi.ogloszenia.jpa.CarModel;
import wizut.tpsi.ogloszenia.jpa.FuelType;
import wizut.tpsi.ogloszenia.jpa.Offer;
import wizut.tpsi.ogloszenia.web.OfferFilter;

@Service
@Transactional
public class OffersService 
{
    @PersistenceContext
    private EntityManager em;
    
    public Offer saveOffer(Offer offer) 
    {
        return em.merge(offer);
    }
    
    public Offer deleteOffer(Integer id) 
    {
        Offer offer = em.find(Offer.class, id);
        em.remove(offer);
        return offer;
    }
    
    public List<Offer> getOffers(OfferFilter filter) 
    {    
        String jpql = "select o from Offer o ";
        boolean isFiltered = false;
        if(filter.getManufacturerId()!=null)
        {
            isFiltered=true;
            jpql= jpql + "where o.model.manufacturer.id = :id1 ";
        }    
        if(filter.getModelId()!=null)
        {
            jpql= jpql + "AND o.model.id = :id2 ";
        }
        if(filter.getFuelId()!=null)
        {
            if(isFiltered==false) jpql=jpql+"where ";
            else jpql=jpql+"AND ";
            isFiltered=true;
            jpql= jpql + "o.fuelType.id = :id3 ";
        } 
        if(filter.getYearMin()!=null)
        {
            if(isFiltered==false) jpql=jpql+"where ";
            else jpql=jpql+"AND ";
            isFiltered=true;
            jpql= jpql + "o.year >= :id4 ";
        } 
        if(filter.getYearMax()!=null)
        {
            if(isFiltered==false) jpql=jpql+"where ";
            else jpql=jpql+"AND ";
            isFiltered=true;
            jpql= jpql + "o.year <= :id5 ";
        }
        if(filter.getDescription()!=null)
        {
            if(isFiltered==false) jpql=jpql+"where ";
            else jpql=jpql+"AND ";
            isFiltered=true;
            jpql= jpql + "o.description LIKE :id6 ";
        }
        
        jpql= jpql + "order by " + filter.getSort();

         
        TypedQuery<Offer> query = em.createQuery(jpql, Offer.class);
        if(filter.getManufacturerId()!=null)
            query.setParameter("id1", filter.getManufacturerId());
        if(filter.getModelId()!=null)
            query.setParameter("id2", filter.getModelId());
        if(filter.getFuelId()!=null)
            query.setParameter("id3", filter.getFuelId());  
        if(filter.getYearMin()!=null)
            query.setParameter("id4", filter.getYearMin());
        if(filter.getYearMax()!=null)
            query.setParameter("id5", filter.getYearMax());
        if(filter.getDescription()!=null)
            query.setParameter("id6", '%' + filter.getDescription() + '%');
        
        return query.getResultList();
    }

    
    public Offer createOffer(Offer offer) 
    {
        em.persist(offer);
        return offer;
    }
    
    public List<Offer> getOffersByManufacturer(int manufacturerId) 
    {
    String jpql = "select o from Offer o where o.model.manufacturer.id = :id order by o.title";
    TypedQuery<Offer> query = em.createQuery(jpql, Offer.class);
    query.setParameter("id", manufacturerId);
    return query.getResultList();
    }
    
    public List<Offer> getOffersByModel(int modelId) 
    {
    String jpql = "select o from Offer o where o.model.id = :id order by o.title";
    TypedQuery<Offer> query = em.createQuery(jpql, Offer.class);
    query.setParameter("id", modelId);
    return query.getResultList();
    }
    
    public List<Offer> getOffers() 
    {
    String jpql = "select o from Offer o order by o.title";
    TypedQuery<Offer> query = em.createQuery(jpql, Offer.class);
    List<Offer> result = query.getResultList();
    return result;
    }
    
    public List<CarModel> getCarModels(int manufacturerId) 
    {
    String jpql = "select cm from CarModel cm where cm.manufacturer.id = :id order by cm.name";
    TypedQuery<CarModel> query = em.createQuery(jpql, CarModel.class);
    query.setParameter("id", manufacturerId);
    return query.getResultList();
    }
    
    public List<CarModel> getCarModels() 
    {
    String jpql = "select cm from CarModel cm order by cm.name";
    TypedQuery<CarModel> query = em.createQuery(jpql, CarModel.class);
    List<CarModel> result = query.getResultList();
    return result;
    }
    
    public List<FuelType> getFuelTypes() 
    {
    String jpql = "select ft from FuelType ft order by ft.name";
    TypedQuery<FuelType> query = em.createQuery(jpql, FuelType.class);
    List<FuelType> result = query.getResultList();
    return result;
    }
    
    public List<BodyStyle> getBodyStyles() 
    {
    String jpql = "select bs from BodyStyle bs order by bs.name";
    TypedQuery<BodyStyle> query = em.createQuery(jpql, BodyStyle.class);
    List<BodyStyle> result = query.getResultList();
    return result;
    }
    
    public List<CarManufacturer> getCarManufacturers() 
    {
    String jpql = "select cm from CarManufacturer cm order by cm.name";
    TypedQuery<CarManufacturer> query = em.createQuery(jpql, CarManufacturer.class);
    List<CarManufacturer> result = query.getResultList();
    return result;
    }
    
    public CarManufacturer getCarManufacturer(int id)
    {
        return em.find(CarManufacturer.class, id);
    }
    
    public Offer getOffer(int id)
    {
        return em.find(Offer.class, id);
    }
    
    public CarModel getModel(int id)
    {
        return em.find(CarModel.class, id);
    }
}
