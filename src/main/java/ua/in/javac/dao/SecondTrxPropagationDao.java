package ua.in.javac.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.in.javac.entity.User;

import java.io.IOException;

/**
 * Created by kernel32 on 17.01.2019.
 */
@Service
public class SecondTrxPropagationDao {

    @Autowired
    private SessionFactory sessionFactory;

    public SecondTrxPropagationDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateTest(User item, Transaction trx) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.getTransaction();
        System.out.println(transaction == trx);
        System.out.println("different trx");

        User a = new User();
        a.setUsername("New Test");
        a.setEmail("testFetching@m.ru");
        a.setLink("http://d.ty");
        a.setPassword("111");
        a.setResource("fff");
        session.save(a);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateTestWithIOE(User item, Transaction firstTransaction) throws IOException {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.getTransaction();
        System.out.println(transaction == firstTransaction);
        System.out.println("different trx");

        User a = new User();
        a.setUsername("New Test");
        a.setEmail("testFetching@m.ru");
        a.setLink("http://d.ty");
        a.setPassword("111");
        a.setResource("fff");
        session.save(a);

        if(true){
            throw new IOException();
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateTestWithRE(User item, Transaction firstTransaction) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.getTransaction();
        System.out.println(transaction == firstTransaction);
        System.out.println("different trx");

        User a = new User();
        a.setUsername("New Test");
        a.setEmail("testFetching@m.ru");
        a.setLink("http://d.ty");
        a.setPassword("111");
        a.setResource("fff");
        session.save(a);

        if(true){
            throw new RuntimeException();
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public void updateWithReadOnly(User item, Transaction firstTransaction) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.getTransaction();
        System.out.println(transaction == firstTransaction);
        System.out.println("different trx");

        User a = new User();
        a.setUsername("New Test");
        a.setEmail("testFetching@m.ru");
        a.setLink("http://d.ty");
        a.setPassword("111");
        a.setResource("fff");
        session.save(a);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public void updateWithReadOnlyButSameTrx(User item, Transaction firstTransaction) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.getTransaction();
        System.out.println(transaction == firstTransaction);
        System.out.println("same trx");

        User a = new User();
        a.setUsername("New Test");
        a.setEmail("testFetching@m.ru");
        a.setLink("http://d.ty");
        a.setPassword("111");
        a.setResource("fff");
        session.save(a);
    }
}
