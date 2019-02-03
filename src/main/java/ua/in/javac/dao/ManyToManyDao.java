package ua.in.javac.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.in.javac.entity.manytomany.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * Created by kernel32 on 23.01.2019.
 */
@Repository
@Transactional (propagation = Propagation.REQUIRES_NEW)
public class ManyToManyDao {

    static final Logger logger = LogManager.getLogger(ManyToManyDao.class);

    @Autowired
    private SessionFactory sessionFactory;

    public void save(Category softwareDevelopment) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.save(softwareDevelopment);
    }

    public void save(Author martinFowler) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.save(martinFowler);
    }

    public void save(AbstractBook poeaa) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.save(poeaa);
    }

    public Book findByBookName(String name) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Book> query = criteriaBuilder.createQuery(Book.class);
        Root<Book> from = query.from(Book.class);

        Predicate condition = criteriaBuilder.equal(from.get("title"), name);
        CriteriaQuery<Book> all = query.where(condition);

        CriteriaQuery<Book> result = all.select(from);
        Book item = session.createQuery(result).getSingleResult();

        return item;
    }

    public BookFetchModeSubselect findByBookNameSubselect (String name) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<BookFetchModeSubselect> query = criteriaBuilder.createQuery(BookFetchModeSubselect.class);
        Root<BookFetchModeSubselect> from = query.from(BookFetchModeSubselect.class);

        Predicate condition = criteriaBuilder.equal(from.get("title"), name);
        CriteriaQuery<BookFetchModeSubselect> all = query.where(condition);

        CriteriaQuery<BookFetchModeSubselect> result = all.select(from);
        BookFetchModeSubselect item = session.createQuery(result).getSingleResult();

        return item;
    }

    public Book find(Class<Book> bookClass, int i) {
        Session currentSession = sessionFactory.getCurrentSession();
        Book book = currentSession.get(Book.class, i);
        return  book;
    }

    public void saveAbstract(AbstractBook poeaa) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.save(poeaa);
    }

    public void modeSelect(Htype htype) {
        List<BookFetchModeSelect> bookList = null;
        if(htype.equals(Htype.CRITERIA)){
            System.out.println("<---Cryteria--->");
            // the same as for below HQL
            bookList = sessionFactory.getCurrentSession().createCriteria(BookFetchModeSelect.class).list();
        } else if (htype.equals(Htype.HQL)) {
            System.out.println("<---HQL--->");
            // n + 1 :
            // 1 query from root BookFetchModeSelect s
            // (LAZY category) EAGER authors: 4 query BookFetchModeSelect_Author b inner join Author a where b.BookFetchModeSelect_id=?
            bookList = sessionFactory.getCurrentSession().createQuery("select b from BookFetchModeSelect b").list();
        } else if(htype.equals(Htype.HQL_FETCH)){
            System.out.println("<---HQL Fetch--->");
            // 1 query (but later in LAZY 4 query the same as for LAZY SELECT):  from BookFetchModeSelect inner join BookFetchModeSelect_Author inner join Author
            bookList = sessionFactory.getCurrentSession().createQuery("select b from BookFetchModeSelect b join fetch b.authors a").list();
        }

        Iterator<BookFetchModeSelect> iterator = bookList.iterator();
        while(iterator.hasNext()){
            BookFetchModeSelect next = iterator.next();
            System.out.println("id --> " + next.getId());
            List<Author> authors = next.getAuthors();
            System.out.println("authors size --> " + authors.size());
            List<Category> categories = next.getCategories();
            System.out.println("categories size --> " + categories.size());
        }
    }

    public void modeSubSelect(Htype htype) {
        List<BookFetchModeSubselect> bookList = null;
        if(htype.equals(Htype.CRITERIA)){
            System.out.println("<---Cryteria--->");
            // the same as for below HQL
            bookList = sessionFactory.getCurrentSession().createCriteria(BookFetchModeSubselect.class).list();
        } else if (htype.equals(Htype.HQL)) {
            System.out.println("<---HQL--->");
            bookList = sessionFactory.getCurrentSession().createQuery("select b from BookFetchModeSubselect b").list();
            // 1 query: from root BookFetchModeSubselect
            // 1 query: from BookFetchModeSubselect_Author b inner join Author a where b.BookFetchModeSubselect_id in ( id from BookFetchModeSubselect)
        } else if(htype.equals(Htype.HQL_FETCH)){
            System.out.println("<---HQL Fetch--->");
            // the same as for modeSelect - HQL Fetch
            bookList = sessionFactory.getCurrentSession().createQuery("select b from BookFetchModeSubselect b join fetch b.authors a").list();
        }

        Iterator<BookFetchModeSubselect> iterator = bookList.iterator();
        while(iterator.hasNext()){
            BookFetchModeSubselect next = iterator.next();
            System.out.println("id --> " + next.getId());
            List<Author> authors = next.getAuthors();
            System.out.println("authors size --> " + authors.size());
            List<Category> categories = next.getCategories();
            System.out.println("categories size --> " + categories.size());
        }
    }

    public void modeJoin(Htype htype) {
        List<BookFetchModeJoin> bookList = null;
        if(htype.equals(Htype.CRITERIA)){
            System.out.println("<---Cryteria--->");
            // 1 query for authors:  from BookFetchModeJoin left join BookFetchModeJoin_Author left join Author
            // 4 query for categories: BookFetchModeJoin_Category b inner join Category c where b..BookFetchModeJoin_id=?
            bookList = sessionFactory.getCurrentSession().createCriteria(BookFetchModeJoin.class).list();
        } else if (htype.equals(Htype.HQL)) {
            System.out.println("<---HQL--->");
            // 1 query from root
            // 4 query from Author; the same as for SELECT fetch
            // 4 query from Category; the same as for SELECT fetch
            bookList = sessionFactory.getCurrentSession().createQuery("select b from BookFetchModeJoin b").list();
        } else if(htype.equals(Htype.HQL_FETCH)){
            // 1 query: from BookFetchModeJoin inner join BookFetchModeJoin_Author inner join Author
            // 4 query as for LAZY SELECT;
            System.out.println("<---HQL Fetch--->");
            bookList = sessionFactory.getCurrentSession().createQuery("select b from BookFetchModeJoin b join fetch b.authors a").list();
        }
        Iterator<BookFetchModeJoin> iterator = bookList.iterator();
        while(iterator.hasNext()){
            BookFetchModeJoin next = iterator.next();
            System.out.println("id --> " + next.getId());
            List<Author> authors = next.getAuthors();
            System.out.println("authors size --> " + authors.size());
            List<Category> categories = next.getCategories();
            System.out.println("categories size --> " + categories.size());
        }
    }

    public void modeBatch(Htype htype) {
        List<BookBatchSize> bookList = null;
        if(htype.equals(Htype.CRITERIA)){
            System.out.println("<---Cryteria--->");
            // the same as for below HQL
            bookList = sessionFactory.getCurrentSession().createCriteria(BookBatchSize.class).list();
        } else if (htype.equals(Htype.HQL)) {
            System.out.println("<---HQL--->");
            // 1 query from root BookBatchSize
            // 2 query from BookBatchSize_Author b inner join  Author a where b.BookBatchSize_id in (?, ?)
            bookList = sessionFactory.getCurrentSession().createQuery("select b from BookBatchSize b").list();
        } else if(htype.equals(Htype.HQL_FETCH)){
            // 1 query: from BookBatchSize inner join BookBatchSize_Author inner join Author
            // 2 query: the same as for modeBatch - HQL
            System.out.println("<---HQL Fetch--->");
            bookList = sessionFactory.getCurrentSession().createQuery("select b from BookBatchSize b join fetch b.authors a").list();
        }
        Iterator<BookBatchSize> iterator = bookList.iterator();
        while(iterator.hasNext()){
            BookBatchSize next = iterator.next();
            System.out.println("id --> " + next.getId());
            List<Author> authors = next.getAuthors();
            System.out.println("authors size --> " + authors.size());
            List<Category> categories = next.getCategories();
            System.out.println("categories size --> " + categories.size());
        }
    }

    private void prepareResources() throws CloneNotSupportedException {
        prepareResources(new BookFetchModeSelect());
        prepareResources(new BookFetchModeJoin());
        prepareResources(new BookFetchModeSubselect());
        prepareResources(new BookBatchSize());
    }

    private void prepareResources(AbstractBook book) throws CloneNotSupportedException {
        Category softwareDevelopment = new Category();
        softwareDevelopment.setName("Software development");
        save(softwareDevelopment);

        Category systemDesign = new Category();
        systemDesign.setName("System design");
        save(systemDesign);

        Author martinFowler = new Author();
        martinFowler.setFullName("Martin Fowler");
        save(martinFowler);

        AbstractBook poeaa = book.cloneObject(book);
        poeaa.setIsbn("007-6092019909");
        poeaa.setTitle("Patterns of Enterprise Application Architecture");
        poeaa.setPublicationDate(new Date());
        poeaa.setAuthors(asList(martinFowler));
        poeaa.setCategories(asList(softwareDevelopment, systemDesign));
        saveAbstract(poeaa);

        Author gregorHohpe = new Author();
        gregorHohpe.setFullName("Gregor Hohpe");
        save(gregorHohpe);

        Author bobbyWoolf = new Author();
        bobbyWoolf.setFullName("Bobby Woolf");
        save(bobbyWoolf);

        AbstractBook eip = book.cloneObject(book);
        eip.setIsbn("978-0321200686");
        eip.setTitle("Enterprise Integration Patterns");
        eip.setPublicationDate(new Date());
        eip.setAuthors(asList(gregorHohpe, bobbyWoolf));
        eip.setCategories(asList(softwareDevelopment, systemDesign));
        saveAbstract(eip);

        Category objectOrientedSoftwareDesign = new Category();
        objectOrientedSoftwareDesign.setName("Object-Oriented Software Design");
        save(objectOrientedSoftwareDesign);

        Author ericEvans = new Author();
        ericEvans.setFullName("Eric Evans");
        save(ericEvans);

        AbstractBook ddd = book.cloneObject(book);
        ddd.setIsbn("860-1404361814");
        ddd.setTitle("Domain-Driven Design: Tackling Complexity in the Heart of Software");
        ddd.setPublicationDate(new Date());
        ddd.setAuthors(asList(ericEvans));
        ddd.setCategories(asList(softwareDevelopment, systemDesign, objectOrientedSoftwareDesign));
        saveAbstract(ddd);

        Category networkingCloudComputing = new Category();
        networkingCloudComputing.setName("Networking & Cloud Computing");
        save(networkingCloudComputing);

        Category databasesBigData = new Category();
        databasesBigData.setName("Databases & Big Data");
        save(databasesBigData);

        Author pramodSadalage = new Author();
        pramodSadalage.setFullName("Pramod J. Sadalage");
        save(pramodSadalage);

        AbstractBook nosql = book.cloneObject(book);
        nosql.setIsbn("978-0321826626");
        nosql.setTitle("NoSQL Distilled: A Brief Guide to the Emerging World of Polyglot Persistence");
        nosql.setPublicationDate(new Date());
        nosql.setAuthors(asList(pramodSadalage, martinFowler));
        nosql.setCategories(asList(networkingCloudComputing, databasesBigData));
        saveAbstract(nosql);
    }

    public void testFetching() throws CloneNotSupportedException {
        //prepareResources();
        long queryCount = sessionFactory.getStatistics().getQueryExecutionCount();
        System.out.println("queryCount - " + queryCount);
        for (int i = 0; i < Htype.values().length; i++){
            System.out.println("Mode SELECT");
            modeSelect(Htype.values()[i]);
            System.out.println("Mode SUBSELECT");
            modeSubSelect(Htype.values()[i]);
            System.out.println("Mode JOIN");
            modeJoin(Htype.values()[i]);
            System.out.println("Mode BATCH");
            modeBatch(Htype.values()[i]);
        }
        System.out.println("queryCount - " + sessionFactory.getStatistics().getQueryExecutionCount());
    }
}
