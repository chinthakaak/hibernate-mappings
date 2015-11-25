package com.examples.xml.polymorphic;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Environment;

/**
 * Created by ka40215 on 11/25/15.
 */
public class ManyToOnePolymorphicMapping {
    private static final String CLASS_NAME = ManyToOnePolymorphicMapping.class.getSimpleName();

    public static void main(String[] args) {
        Session session = HibernateUtil.buildSessionFactoryCreate().openSession();
        Transaction transaction = session.beginTransaction();

        CreditCard creditCard1 = new CreditCard();
        CreditCard creditCard2 = new CreditCard();
        BankAccount bankAccount = new BankAccount();


        session.save(creditCard1);
        session.save(creditCard2);
        User user = new User();
        user.setDefaultBillingDetails(bankAccount);
        session.save(user);
        transaction.commit();
        session.close();

    }
    private static class User {
        private int userId;

        public BillingDetails getDefaultBillingDetails() {
            return defaultBillingDetails;
        }

        public void setDefaultBillingDetails(BillingDetails defaultBillingDetails) {
            this.defaultBillingDetails = defaultBillingDetails;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        private BillingDetails defaultBillingDetails;
    }

    private static abstract class BillingDetails {
        private int billingDetailsId;
    }

    private static class CreditCard extends BillingDetails {

    }

    private static class BankAccount extends BillingDetails {

    }

    private static class HibernateUtil {
        private static SessionFactory buildSessionFactory(String hbm2ddlAuto) {
            try {
                // Create the SessionFactory programmatically
                return new AnnotationConfiguration()
                        .setProperty(Environment.DRIVER, "oracle.jdbc.driver.OracleDriver")
                        .setProperty(Environment.URL, "jdbc:oracle:thin:@127.0.0.1:1521:xe")
                        .setProperty(Environment.USER, "HBMAPPINGS")
                        .setProperty(Environment.PASS, "password")
                        .setProperty(Environment.DIALECT, "org.hibernate.dialect.Oracle10gDialect")
                        .setProperty(Environment.SHOW_SQL, "true")
                        .setProperty(Environment.HBM2DDL_AUTO, hbm2ddlAuto)
                        .addResource(CLASS_NAME + ".hbm.xml")
                        .buildSessionFactory();

            } catch (Throwable ex) {
                // Make sure you log the exception, as it might be swallowed
                System.err.println("Initial SessionFactory creation failed." + ex);
                throw new ExceptionInInitializerError(ex);
            }
        }

        private static SessionFactory buildSessionFactoryCreate() {
            return buildSessionFactory("create");
        }
        private static SessionFactory buildSessionFactoryUpdate() {
            return buildSessionFactory("update");
        }
    }
}
