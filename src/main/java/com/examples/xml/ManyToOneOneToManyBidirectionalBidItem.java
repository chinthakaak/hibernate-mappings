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
public class ManyToOneOneToManyBidirectionalBidItem {
    public static void main(String[] args) {
        // ############ Test 1 ###########
        // This session saves item only, bids cascaded
        Session session = HibernateUtil.buildSessionFactory("create").openSession();
        Transaction transaction = session.beginTransaction();

        Item item = new Item();
        item.setItemName("IC");

        Bid bid1 = new Bid();
        bid1.setPrice(100);
        bid1.setItem(item);

        Bid bid2 = new Bid();
        bid2.setPrice(120);
        bid2.setItem(item);

        Set bids = new HashSet();
        bids.add(bid1);
        bids.add(bid2);
        item.setBids(bids);
        session.save(item);

        transaction.commit();

        session.close();

        // This session checks bidirectional navigation
        Session session1 = HibernateUtil.buildSessionFactory("update").openSession();
        Transaction transaction1 = session1.beginTransaction();

        // can navigate to a bid from an item
        Item item1 = (Item) session1.get(Item.class, 1);
        for (Object bid: item1.getBids()) System.out.println(((Bid)bid).getPrice());

        // can navigate to an item from a bid
        Bid bid = (Bid) session1.load(Bid.class, 2);
        System.out.println(bid.getItem().getItemName());

        transaction1.commit();

        session1.close();

        // ############ Test 2 ###########
        // This session bids only, item cascades
        Session session2 = HibernateUtil.buildSessionFactory("create").openSession();
        Transaction transaction2 = session2.beginTransaction();

        Item item2 = new Item();
        item2.setItemName("IC");

        Bid bid3 = new Bid();
        bid3.setPrice(1000);
        bid3.setItem(item2);

        Bid bid4 = new Bid();
        bid4.setPrice(1200);
        bid4.setItem(item2);

        session2.save(bid3);
        session2.save(bid4);

        transaction2.commit();

        session2.close();

        // This session checks bidirectional navigation
        Session session3 = HibernateUtil.buildSessionFactory("update").openSession();
        Transaction transaction3 = session3.beginTransaction();

        // can navigate to a bid from an item
        Item item3 = (Item) session3.get(Item.class, 2);
        for (Object bid5: item3.getBids()) System.out.println(((Bid)bid5).getPrice());

        // can navigate to an item from a bid
        Bid bid5 = (Bid) session3.load(Bid.class, 1);
        System.out.println(bid5.getItem().getItemName());

        transaction3.commit();

        session3.close();

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
        private Item item;

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

        public Item getItem() {
            return item;
        }

        public void setItem(Item item) {
            this.item = item;
        }
    }

    private static class HibernateUtil {
//        private static final SessionFactory sessionFactory = buildSessionFactory();

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
//                        .setProperty(Environment.HBM2DDL_AUTO, "update")
                        .addResource("ManyToOneOneToManyBidirectionalBidItem.xml")
                        .buildSessionFactory();

            } catch (Throwable ex) {
                // Make sure you log the exception, as it might be swallowed
                System.err.println("Initial SessionFactory creation failed." + ex);
                throw new ExceptionInInitializerError(ex);
            }
        }
    }
}
