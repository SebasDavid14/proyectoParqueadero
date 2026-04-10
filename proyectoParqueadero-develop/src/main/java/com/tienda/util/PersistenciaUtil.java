package com.tienda.util;

import java.io.*;

public class PersistenciaUtil {

    public static void guardarObjeto(Object obj, String path) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
            oos.writeObject(obj);
        } catch (Exception e) {
            System.err.println("Error al guardar: " + e.getMessage());
        }
    }

    public static Object cargarObjeto(String path) {
        File file = new File(path);
        if (!file.exists()) return null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return ois.readObject();
        } catch (Exception e) {
            System.err.println("Error al cargar: " + e.getMessage());
            return null;
        }
    }
}
