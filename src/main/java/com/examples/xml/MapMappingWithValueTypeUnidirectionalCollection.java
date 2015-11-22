package com.examples.xml;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Environment;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by ka40215 on 11/22/15.
 */
public class MapMappingWithValueTypeUnidirectionalCollection {
    public static void main(String[] args) throws InterruptedException {
        Session session = HibernateUtil.buildSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        Item item = new Item();
        item.setItemName("Transistor");
        Map imageMap = new HashMap();
        imageMap.put("first image", "trx1.png");
        imageMap.put("second image", "trx2.png");
        item.setImages(imageMap);

        Item item1 = new Item();
        item1.setItemName("Capacitor");
        Map imageMap1 = new HashMap();
        imageMap1.put("first image", "cap1.png");
        imageMap1.put("second image", "cap2.png");
        item1.setImages(imageMap1);

        session.save(item);
        session.save(item1);

        transaction.commit();

        Transaction transaction1 = session.beginTransaction();
        // Item to ItemImage direction
        Item loadedItem = (Item) session.load(Item.class, 1);
        System.out.println("Loaded item is "+ loadedItem.getItemName());

        Set<Map.Entry> set = loadedItem.getImages().entrySet();
        for (Map.Entry<String, String> e: set) {
            System.out.println("Loaded image name is " + e.getKey());
            System.out.println("Loaded image file name is " + e.getValue());
        }
        transaction1.commit();
        session.close();
    }

    private static class Item {
        private int itemId;
        private String itemName;
        private Map images = new HashMap();

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public Map getImages() {
            return images;
        }

        public void setImages(Map images) {
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
        private String fileName;

        public String getImageName() {
            return imageName;
        }

        public void setImageName(String imageName) {
            this.imageName = imageName;
        }

        private String imageName;

        private int itemId;

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
                        .addResource("MapMappingWithValueTypeUnidirectionalCollection.xml")
                        .buildSessionFactory();

            } catch (Throwable ex) {
                // Make sure you log the exception, as it might be swallowed
                System.err.println("Initial SessionFactory creation failed." + ex);
                throw new ExceptionInInitializerError(ex);
            }
        }
    }
}
