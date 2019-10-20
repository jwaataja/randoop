package randoop.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import org.checkerframework.checker.determinism.qual.Det;

public class JarReader {

  private static boolean debug = false;

  private JarReader() {
    throw new Error("Do not instantiate");
  }

  public static void main(String[] args) throws IOException {
    @Det List<String> names = getClasseNamesInJar(args[0]);
    Collections.sort(names);
    System.out.println(CollectionsExt.toStringInLines(names));
  }

  public static List<String> getClassNamesInPackage(@Det String jarName, @Det String packageName)
      throws IOException {
    @Det ArrayList<String> classes = new ArrayList<>();

    packageName = packageName.replaceAll("\\.", "/");
    if (debug) {
      System.out.println("Jar " + jarName + " looking for " + packageName);
    }

    JarInputStream jarFile = null;
    try {
      jarFile = new JarInputStream(new FileInputStream(jarName));
      JarEntry jarEntry;

      while (true) {
        jarEntry = jarFile.getNextJarEntry();
        if (jarEntry == null) {
          break;
        }
        if (jarEntry.getName().startsWith(packageName) && jarEntry.getName().endsWith(".class")) {
          if (debug) System.out.println(jarEntry.getName().replaceAll("/", "\\."));
          classes.add(jarEntry.getName().replaceAll("/", "\\."));
        }
      }
    } finally {
      if (jarFile != null) jarFile.close();
    }
    return classes;
  }

  public static List<String> getClasseNamesInJar(@Det String jarName) throws IOException {
    @Det ArrayList<String> classes = new ArrayList<>();

    if (debug) {
      System.out.println("Jar " + jarName);
    }

    JarInputStream jarFile = null;
    try {
      jarFile = new JarInputStream(new FileInputStream(jarName));
      JarEntry jarEntry;

      while (true) {
        jarEntry = jarFile.getNextJarEntry();
        if (jarEntry == null) {
          break;
        }
        if (jarEntry.getName().endsWith(".class")) {
          if (debug) System.out.println(jarEntry.getName().replaceAll("/", "\\."));
          classes.add(jarEntry.getName().replaceAll("/", "\\."));
        }
      }
    } finally {
      if (jarFile != null) jarFile.close();
    }
    return classes;
  }
}
