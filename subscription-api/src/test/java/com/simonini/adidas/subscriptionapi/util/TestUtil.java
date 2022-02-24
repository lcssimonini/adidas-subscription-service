package com.simonini.adidas.subscriptionapi.util;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestUtil {

  public static String readFromResources(String filename) {
    try {
      URL resource = TestUtil.class.getClassLoader().getResource(filename);
      byte[] bytes = Files.readAllBytes(Paths.get(resource.toURI()));
      return new String(bytes);
    } catch (Exception ignored) {
    }
    return null;
  }
}
