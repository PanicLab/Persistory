package com.paniclab.persistory;


import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class Utils {

    public static boolean isNot(boolean b) {
        return !b;
    }

    public static boolean isEmpty(CharSequence charSequence) {
        return charSequence.length() <= 0;
    }

    public static URI getApplicationURI(Object obj) {
        URI result = null;
        try {
            result = obj.getClass().getProtectionDomain().getCodeSource().getLocation()
                    .toURI();
        } catch (URISyntaxException e) {
            System.err.println("Persistory не удалось установить каталог приложения.");
            System.err.println("Ошибка при определении URI: ");
            System.err.println(obj.getClass().getProtectionDomain().getCodeSource().getLocation().toString());
            System.err.println("Работа приложения невозможна. Обратитесь к разработчику.");
            throw new InternalError(e);
        }
        return result;
    }

    public static Path getApplicationPath(Object obj) {
        URI uri = getApplicationURI(obj);
        return Paths.get(uri);
    }

    public static byte[] stringToBytes(String string, String charset) {
        byte[] result = null;
        try {
            result = string.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
}
