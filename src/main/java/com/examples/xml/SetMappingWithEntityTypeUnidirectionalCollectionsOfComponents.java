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
public class SetMappingWithEntityTypeUnidirectionalCollectionsOfComponents {
    public static void main(String[] args) throws InterruptedException {
        Session session = HibernateUtil.buildSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        Item item = new Item();
        item.setItemName("Transistor");
        Set<ItemImage> itemImages = new HashSet<ItemImage>();
        ItemImage itemImage = new ItemImage();
        itemImage.setFileName("trx.pnd");
        itemImage.setX(100);
        itemImage.setY(20);
        itemImages.add(itemImage);
        item.setImages(itemImages);

        session.save(item);
        transaction.commit();
        session.close();
    }

    private static class Item {
        private int itemId;
        private String itemName;

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        private Set images = new HashSet();

        public Set getImages() {
            return images;
        }

        public void setImages(Set images) {
            this.images = images;
        }

        public int getItemId() {
            return itemId;
        }

        public void setItemId(int itemId) {
            this.itemId = itemId;
        }
    }

    private static class ItemImage {
        private int itemId;

        private String fileName;

        private int x;
        private int y;

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public int getItemId() {
            return itemId;
        }

        public void setItemId(int itemId) {
            this.itemId = itemId;
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
                        .addResource("SetMappingWithEntityTypeUnidirectionalCollectionsOfComponents.xml")
                        .buildSessionFactory();

            } catch (Throwable ex) {
                // Make sure you log the exception, as it might be swallowed
                System.err.println("Initial SessionFactory creation failed." + ex);
                throw new ExceptionInInitializerError(ex);
            }
        }
    }
}
