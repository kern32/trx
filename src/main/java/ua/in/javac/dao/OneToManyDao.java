package ua.in.javac.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.in.javac.entity.onetomany.Address;
import ua.in.javac.entity.onetomany.Customer;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by kernel32 on 22.01.2019.
 */
@Repository
public class OneToManyDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    public void saveCustomer(String firstName) {
        Session session = session = sessionFactory.getCurrentSession();
        Customer item = new Customer();
        item.setFirstName(firstName);
        item.setLastNameName("Surname");
        item.setList(getAddressList(item));
        session.save(item);
    }

    public List<Address> getAddressList(Customer item) {
        List<Address> list = new ArrayList<>();
        for (int i = 0; i < 15; i++){
            Address address = new Address();
            address.setPopstalCode("" + i);
            address.setStreet("Street " + i);
            address.setCustomer(item);
            list.add(address);
        }

        return list;
    }

    @Transactional
    public Customer getCustomer(String firstName) {
        Session session = session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Customer> query = criteriaBuilder.createQuery(Customer.class);
        Root<Customer> from = query.from(Customer.class);

        Predicate condition = criteriaBuilder.equal(from.get("firstName"), firstName);
        CriteriaQuery<Customer> all = query.where(condition);

        CriteriaQuery<Customer> result = all.select(from);
        Customer item = session.createQuery(result).getSingleResult();

        return item;
    }

    @Transactional
    public void testFetching(){
        //saveCustomer("Name");
        Customer name = getCustomer("Name");
        List<Address> list = name.getList();
        Iterator<Address> iterator = list.iterator();
        while(iterator.hasNext()){
            Address next = iterator.next();
            System.out.println(next.getStreet());
        }
    }
}
