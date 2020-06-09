/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wizut.tpsi.ogloszenia.services;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import wizut.tpsi.ogloszenia.jpa.User;

/**
 *
 * @author Pioterk
 */
@Service
@Transactional
public class UserService {

    @PersistenceContext
    private EntityManager em;

    public User getUserByName(String name) {
        String jpql = "select cm from User cm where cm.username = :name";
        TypedQuery<User> query = em.createQuery(jpql, User.class);
        query.setParameter("name", name);
        
        User user = null;
        
        try {
            user = query.getSingleResult();
        } catch (NoResultException e) {

        }
        return user;
    }
}
