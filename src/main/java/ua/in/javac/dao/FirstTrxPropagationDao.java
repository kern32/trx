package ua.in.javac.dao;

import org.hibernate.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.in.javac.entity.User;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.IOException;
import java.util.List;

/**
 * Created by kernel32 on 13.01.2019.
 */
@Repository
public class FirstTrxPropagationDao {

    Transaction firstTransaction;
    Transaction secondTransaction;

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    SecondTrxPropagationDao secondPropagationDao;

    public FirstTrxPropagationDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public User getUser(int id) {
        Session session = session = sessionFactory.getCurrentSession();
        User item = session.get(User.class, 1);
        return item;
    }

    @Transactional
    public User getUser(String username) {
        Session session = session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
        Root<User> from = query.from(User.class);

        Predicate condition = criteriaBuilder.equal(from.get("username"), username);
        CriteriaQuery<User> all = query.where(condition);

        CriteriaQuery<User> result = all.select(from);
        User item = session.createQuery(result).getSingleResult();

        return item;
    }

    @Transactional
    public List<User> getUserList() {
        Session session = session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
        Root<User> from = query.from(User.class);
        CriteriaQuery<User> all = query.select(from);

        List<User> resultList = session.createQuery(all).getResultList();
        return resultList;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveUser(User item)  {
        Session session = sessionFactory.getCurrentSession();
        session.save(item);
    }




/*---------------------------------------------------------------------------------------------*/

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = IOException.class)
    public void saveCredentialWithRollbackFor(User item) throws IOException, InterruptedException {
        System.out.println("rollback all trx");
        Session session = sessionFactory.getCurrentSession();
        item.setUsername("Test");
        session.save(item);
        if(true){
            throw new IOException();
        }
    }

/*---------------------------------------------------------------------------------------------*/

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveCredentialWithOtherTransactionalBeanReadOnly(User item) throws IOException, InterruptedException {
        Session session = sessionFactory.getCurrentSession();
        firstTransaction = session.getTransaction();
        item.setUsername("Test");
        session.save(item);
        secondPropagationDao.updateWithReadOnly(item, firstTransaction);
        System.out.println("save only 'Test' since new trx with readOnly flag");
    }

/*---------------------------------------------------------------------------------------------*/

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveCredentialWithOtherSameTransactionalBeanReadOnly(User item) throws IOException, InterruptedException {
        Session session = sessionFactory.getCurrentSession();
        firstTransaction = session.getTransaction();
        item.setUsername("Test");
        session.save(item);
        secondPropagationDao.updateWithReadOnlyButSameTrx(item, firstTransaction);
        System.out.println("save 'Test' and 'New Test' even though current trx with readOnly flag");
    }

/*---------------------------------------------------------------------------------------------*/

    @Transactional(timeout = 5, propagation = Propagation.REQUIRED)
    public void saveCredentialWithTimeOut(User item) throws IOException, InterruptedException {
        System.out.println("timeout: " + sessionFactory.getCurrentSession().getTransaction().getTimeout());
        for (int i = 0; i < 6; i++) {
            SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery("select sleep(2)");
            System.out.println("executed (" + i + "): " + query.uniqueResult());
        }
    }

/*---------------------------------------------------------------------------------------------*/

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveCredentialWithFlush(User item) throws IOException, InterruptedException {
        System.out.println("because of flush save even after RE");
        Session session = sessionFactory.getCurrentSession();
        item.setUsername("Test");
        session.save(item);
        session.flush();
        if(true){
            throw new RuntimeException();
        }
    }

/*---------------------------------------------------------------------------------------------*/

    @Transactional(propagation = Propagation.REQUIRED)
    public void rollbackSaveCredentialWithOtherTransactionalLocalMethod(User item) {
        Session session = sessionFactory.getCurrentSession();
        firstTransaction = session.getTransaction();
        item.setUsername("Test");
        session.save(item);
        updateCredential(item, firstTransaction);
        if(true){
            throw new RuntimeException();
        }
        System.out.println("rollback all since the same trx with RE");
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateCredential(User item, Transaction firstTransaction) {
        Session session = sessionFactory.getCurrentSession();
        secondTransaction = session.getTransaction();
        System.out.println(firstTransaction == secondTransaction);
        System.out.println("same transaction");
        item.setUsername("New Test");
        session.save(item);
    }

/*---------------------------------------------------------------------------------------------*/

    @Transactional(propagation = Propagation.REQUIRED)
    public void savePartCredentialWithOtherTransactionalLocalMethod(User item) throws IOException, InterruptedException {
        Session session = sessionFactory.getCurrentSession();
        firstTransaction = session.getTransaction();
        item.setUsername("Test");
        session.save(item);
        updateCredentialWithThrowIOException(item, firstTransaction);
        System.out.println("save 'Test' but doesn't 'New Test'");
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateCredentialWithThrowIOException(User item, Transaction firstTransaction) throws IOException, InterruptedException {
        Session session = sessionFactory.getCurrentSession();
        secondTransaction = session.getTransaction();
        System.out.println(firstTransaction == secondTransaction);
        System.out.println("same transaction");
        item.setUsername("New Test");

        if(true){
            throw new IOException();
        }
        session.save(item);
    }

/*---------------------------------------------------------------------------------------------*/

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveCredentialWithOtherTransactionalBean(User item) {
        Session session = sessionFactory.getCurrentSession();
        firstTransaction = session.getTransaction();
        item.setUsername("Test");
        session.save(item);
        secondPropagationDao.updateTest(item, firstTransaction);
        System.out.println("save 'Test' in current trx and 'New Test' in new trx");
    }

/*---------------------------------------------------------------------------------------------*/

    @Transactional(propagation = Propagation.REQUIRED)
    public void savePartCredentialWithOtherTransactionalBean(User item) throws IOException {
        Session session = sessionFactory.getCurrentSession();
        firstTransaction = session.getTransaction();
        item.setUsername("Test");
        session.save(item);

        secondPropagationDao.updateTest(item, firstTransaction);
        if(true){
            throw new RuntimeException();
        }

        System.out.println("not save 'Test' in current trx and save 'New Test' in new trx");
    }

/*---------------------------------------------------------------------------------------------*/

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveCredentialWithOtherTransactionalBeanBecauseOfIOE(User item) throws IOException {
        Session session = sessionFactory.getCurrentSession();
        firstTransaction = session.getTransaction();
        item.setUsername("Test");
        session.save(item);

        secondPropagationDao.updateTest(item, firstTransaction);
        if(true){
            throw new IOException();
        }

        System.out.println("save 'Test' and 'New Test'");
    }

/*---------------------------------------------------------------------------------------------*/

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveCredentialWithOtherTransactionalBeanWith2Exception(User item) throws IOException {
        Session session = sessionFactory.getCurrentSession();
        firstTransaction = session.getTransaction();
        item.setUsername("Test");
        session.save(item);
        secondPropagationDao.updateTestWithIOE(item, firstTransaction);

        if(true){
            throw new RuntimeException();
        }

        System.out.println("save all because in second trx throws IOE");
    }

/*---------------------------------------------------------------------------------------------*/

    @Transactional(propagation = Propagation.REQUIRED)
    public void savePartCredentialWithOtherTransactionalBeanWith2Exception(User item) throws IOException {
        Session session = sessionFactory.getCurrentSession();
        firstTransaction = session.getTransaction();
        item.setUsername("Test");
        session.save(item);
        secondPropagationDao.updateTestWithRE(item, firstTransaction);

        if(true){
            throw new IOException();
        }

        System.out.println("save nothing becase RE in 2nd trx");
    }

}
