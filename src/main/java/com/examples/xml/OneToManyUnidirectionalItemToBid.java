package com.examples.xml;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Environment;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by ka40215 on 11/22/15.
 */
public class OneToManyUnidirectionalItemToBid {
    public static void main(String[] args) {
        Session session = HibernateUtil.buildSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        Item item = new Item();
        item.setItemName("IC");

        Bid bid1 = new Bid();
        bid1.setPrice(100);

        Bid bid2 = new Bid();
        bid2.setPrice(120);

        Set bids = new HashSet();
        bids.add(bid1);
        bids.add(bid2);

        item.setBids(bids);

        session.save(item);

        transaction.commit();

        Transaction transaction2 = session.beginTransaction();

        // can navigate to a bid from an item
        Item item1 = (Item) session.get(Item.class, 1);
        for (Object bid: item1.getBids()) System.out.println(((Bid)bid).getPrice());

        // cannot navigate to an item from a bid
        Bid bid = (Bid) session.load(Bid.class, 2);

        transaction2.commit();

        session.close();

    }

    private static class Item {
        private int itemId;
        private String itemName;
        private Set bids;

        public Set getBids() {
            return bids;
        }

        public void setBids(Set bids) {
            this.bids = bids;
        }

        public int getItemId() {
            return itemId;
        }

        public void setItemId(int itemId) {
            this.itemId = itemId;
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }
    }

    private static class Bid {
        private int bidId;
        private int price;

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getBidId() {
            return bidId;
        }

        public void setBidId(int bidId) {
            this.bidId = bidId;
        }
    }

    private static class HibernateUtil {
        private static final SessionFactory sessionFactory = buildSessionFactory();

        private static SessionFactory buildSessionFactory() {
            try {
                // Create the SessionFactory programmatically
                return new AnnotationConfiguration()
                        .setProperty(Environment.DRIVER, "oracle.jdbc.driver.OracleDriver")
                        .setProperty(Environment.URL, "jdbc:oracle:thin:@127.0.0.1:1521:xe")
                        .setProperty(Environment.USER, "HBMAPPINGS")
                        .setProperty(Environment.PASS, "password")
                        .setProperty(Environment.DIALECT, "org.hibernate.dialect.Oracle10gDialect")
                        .setProperty(Environment.SHOW_SQL, "true")
                        .setProperty(Environment.HBM2DDL_AUTO, "create")
//                        .setProperty(Environment.HBM2DDL_AUTO, "update")
                        .addResource("OneToManyUnidirectionalItemToBid.xml")
                        .buildSessionFactory();

            } catch (Throwable ex) {
                // Make sure you log the exception, as it might be swallowed
                System.err.println("Initial SessionFactory creation failed." + ex);
                throw new ExceptionInInitializerError(ex);
            }
        }
    }
}
