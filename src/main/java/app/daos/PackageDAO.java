package app.daos;

import app.entities.Package;
import app.enums.DeliveryStatus;
import app.exceptions.JpaException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.*;
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.pbkdf2.Pack;

public class PackageDAO implements IDAO<Package> {
    EntityManagerFactory emf;

    public PackageDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Package create(Package aPackage) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(aPackage);
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new JpaException("Error creating package: " + e.getMessage());
        }
        return aPackage;
    }

    @Override
    public Package findById(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Package.class, id);
        } catch (EntityNotFoundException e) {
            throw new JpaException("Error finding package: " + e.getMessage());
        }
    }

    @Override
    public Package findByTrackingNumber(String trackingNumber) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Package> query = em.createQuery("SELECT p FROM Package p WHERE p.trackingNumber = :trackingNumber", Package.class);
            query.setParameter("trackingNumber", trackingNumber);
            return query.getSingleResult();
        } catch (Exception e) {
            throw new JpaException("Error finding package by tracking number: " + e.getMessage());
        }
    }

    public Package updateDeliveryStatus(Package aPackage, DeliveryStatus deliveryStatus) {
        Package found = null;
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            found = em.find(Package.class, aPackage.getId());
            found.setDeliveryStatus(deliveryStatus);


        }
    }

    @Override
    public Package update(Package aPackage) {
        Package updatedPackage = null;
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            updatedPackage = em.merge(aPackage);
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new JpaException("Error updating package: " + e.getMessage());
        }
        return updatedPackage;
    }

    @Override
    public boolean delete(Package t) {
        return false;
    }
}
