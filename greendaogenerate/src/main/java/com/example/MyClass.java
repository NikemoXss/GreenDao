package com.example;

import java.io.IOException;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

public class MyClass {

    public static void main(String args[]) throws IOException {
        Schema schema=new Schema(1,"www.nikemo.com");
        Entity son=schema.addEntity("Son");
        son.addStringProperty("name");
        son.addIntProperty("age");
        son.addIdProperty();
        Property fatherId=son.addLongProperty("fatherId").getProperty();

        Entity father=schema.addEntity("Father");
        father.addIdProperty();
        father.addStringProperty("name");
        father.addIntProperty("age");
        son.addToOne(father,fatherId);

        try {
            new DaoGenerator().generateAll(schema,"app/src/main/java");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
