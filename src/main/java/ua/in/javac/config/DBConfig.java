package ua.in.javac.config;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.tool.schema.Action;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by kernel32 on 13.01.2019.
 */
@Configuration
@ComponentScan("ua.in.javac.dao")
@EnableTransactionManagement
public class DBConfig {

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://185.65.246.6:3306/access");
        dataSource.setUsername("root");
        dataSource.setPassword("v0qV0nsktr");
        return dataSource;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("ua.in.javac.entity");
        sessionFactory.setHibernateProperties(getHibernateProperties());
        return sessionFactory;
    }

    @Bean
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(sessionFactory);
        return txManager;
    }

    private Properties getHibernateProperties() {
        Properties properties = new Properties();
        properties.put(AvailableSettings.DIALECT, "org.hibernate.dialect.MySQLDialect");
        properties.put(AvailableSettings.SHOW_SQL, true);
        //properties.put(AvailableSettings.HBM2DDL_AUTO, Action.CREATE);
        //properties.put(AvailableSettings.FORMAT_SQL, true);
        properties.put(AvailableSettings.GENERATE_STATISTICS, true);

        properties.put("org.hibernate.stat", "DEBUG");

        properties.put(AvailableSettings.CURRENT_SESSION_CONTEXT_CLASS, "org.springframework.orm.hibernate5.SpringSessionContext");
        return properties;
    }

}
