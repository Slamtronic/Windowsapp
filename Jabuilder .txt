package jabuild;
import javax.tools.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Jabuilder {
    public static void main(String[] args) {
        // Define directories
        String srcDir = "src";
        String binDir = "bin";
        String libDir = "lib";
        
        // Create the bin directory if it doesn't exist
        new File(binDir).mkdirs();

        // Get all Java files from the src directory
        List<File> javaFiles = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(Paths.get(srcDir))) {
            paths.filter(Files::isRegularFile)
                 .filter(p -> p.toString().endsWith(".java"))
                 .forEach(p -> javaFiles.add(p.toFile()));
        } catch (IOException e) {
            System.err.println("Error reading source files: " + e.getMessage());
            return;
        }

        // Prepare classpath from jar files in lib directory
        StringBuilder classpath = new StringBuilder();
        try (Stream<Path> paths = Files.walk(Paths.get(libDir))) {
            paths.filter(Files::isRegularFile)
                 .filter(p -> p.toString().endsWith(".jar"))
                 .forEach(p -> classpath.append(p.toString()).append(File.pathSeparator));
        } catch (IOException e) {
            System.err.println("Error reading library files: " + e.getMessage());
            return;
        }

        // Compile the Java files
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        
        Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(javaFiles);
        List<String> options = Arrays.asList("-d", binDir, "-classpath", classpath.toString());
        
        boolean success = compiler.getTask(null, fileManager, null, options, null, compilationUnits).call();
        
        try {
            fileManager.close();
        } catch (IOException e) {
            System.err.println("Error closing file manager: " + e.getMessage());
        }

        // Print result
        if (success) {
            System.out.println("Compilation successful. All .class files are in the 'bin' directory.");
        } else {
            System.err.println("Compilation failed.");
        }
    }
}