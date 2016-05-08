package com.ag.ara.dao;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ag.ara.model.User;

@Repository
public class UserDAO implements IUserDAO {

	@Autowired
    private SessionFactory sessionFactory;
	

	 public SessionFactory getSessionFactory() {
	        return sessionFactory;
	    }

	    public void setSessionFactory(SessionFactory sessionFactory) {
	        this.sessionFactory = sessionFactory;
	    }
	    

		@Override
		@Transactional
		public User findById(long id) {
	        Session currentSession = this.sessionFactory.getCurrentSession();

			// TODO Auto-generated method stub
			return null;
		}
}
