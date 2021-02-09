package ru.danya.dao;

import org.hibernate.SessionFactory;
import org.hibernate.TransactionException;
import org.hibernate.cfg.Configuration;

import ru.danya.entity.Student;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.*;
import java.util.Collections;
import java.util.List;

@Startup
@Singleton(name = "StudentDAO")
@LocalBean
public class StudentDAOImpl implements StudentDAO {

    SessionFactory sessionFactory;
    org.hibernate.Session session;

    @PostConstruct
    public void init() {
        sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .buildSessionFactory();

        session = sessionFactory.openSession();
    }

    @Lock(LockType.WRITE)
    @Override
    public boolean addEntity(Student student) {
        if (!session.isOpen()) session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.save(student);
            session.getTransaction().commit();
            return true;
        } catch (TransactionException e) {
            session.getTransaction().rollback();
            System.err.println(e.getMessage());
            return false;
        }
    }

    @Lock(LockType.WRITE)
    @Override
    public boolean deleteEntity(long id) {
        if (!session.isOpen()) session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.createQuery("delete Student where id = " + id).executeUpdate();
            session.getTransaction().commit();
            return true;
        } catch (TransactionException e) {
            session.getTransaction().rollback();
            System.err.println(e.getMessage());
            return false;
        }
    }

    @Lock(LockType.READ)
    @Override
    public List<Student> getAllEntities() {
        if (!session.isOpen()) session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            List<Student> allStudents = session.createQuery("from Student").getResultList();
            session.getTransaction().commit();
            return allStudents;
        } catch (TransactionException e) {
            session.getTransaction().rollback();
            System.err.println(e.getMessage());
            return Collections.emptyList();
        }
    }

    @PreDestroy
    public void destroy() {
        try {
            session.beginTransaction();
            session.flush();
            session.getTransaction().commit();
        } catch (TransactionException e) {
            session.getTransaction().rollback();
            System.err.println(e.getMessage());
        } finally {
            session.close();
            sessionFactory.close();
        }
    }
}
