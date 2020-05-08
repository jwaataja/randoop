package randoop.compile;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import org.checkerframework.checker.determinism.qual.PolyDet;

/** Compiler for Java source code files. */
public class FileCompiler {

  /** The command-line options for running this compiler. */
  private final List<@PolyDet String> options;

  /** The compiler object. */
  private final JavaCompiler compiler;

  /** Creates a {@link FileCompiler} with no command-line options. */
  public FileCompiler() {
    this(new ArrayList<String>());
  }

  /**
   * Creates a {@link FileCompiler} with the given command-line options.
   *
   * @param options the <a
   *     href="https://docs.oracle.com/javase/7/docs/api/javax/tools/JavaCompiler.html">command-line</a>
   *     arguments for the {@code JavaCompiler}
   */
  public FileCompiler(@PolyDet List<@PolyDet String> options) {
    this.options = options;
    this.compiler = ToolProvider.getSystemJavaCompiler();
  }

  /**
   * Compile the given source files, writing resulting class files to the destination directory.
   *
   * @param sourceFiles the Java source files
   * @param destinationDir the destination directory for class files
   * @throws FileCompilerException if the compilation fails
   */
  public void compile(@PolyDet List<@PolyDet File> sourceFiles, Path destinationDir)
      throws FileCompilerException {
    // Set the destination directory for the compiler
    @PolyDet List<@PolyDet String> compilerOptions = new @PolyDet ArrayList<>(options);
    compilerOptions.add("-d");
    compilerOptions.add(destinationDir.toString());
    compilerOptions.add("-XDuseUnsharedTable");

    DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
    StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
    @SuppressWarnings("determinism") // library not annotated
    Iterable<? extends JavaFileObject> filesToCompile =
        fileManager.getJavaFileObjectsFromFiles(sourceFiles);

    @SuppressWarnings("determinism") // library not annotated
    JavaCompiler.CompilationTask task =
        compiler.getTask(null, fileManager, diagnostics, compilerOptions, null, filesToCompile);

    Boolean succeeded = task.call();
    if (succeeded == null || !succeeded) {
      @SuppressWarnings("determinism") // library not annotated
      FileCompilerException tmp =
          new FileCompilerException(
              "Compilation failed", sourceFiles, compilerOptions, diagnostics);
      throw tmp;
    }
  }

  /**
   * Compile the given source file, writing resulting class files to the destination directory.
   *
   * @param sourceFile the Java source file
   * @param destinationDir the destination directory for class files
   * @throws FileCompilerException if the compilation fails
   */
  public void compile(Path sourceFile, Path destinationDir) throws FileCompilerException {
    compile(Collections.singletonList(sourceFile.toFile()), destinationDir);
  }

  /** Exception for errors during compilation using {@link FileCompiler}. */
  public static class FileCompilerException extends Throwable {

    private static final long serialVersionUID = 8362158619216912395L;

    /** The list of source files for the compilation. */
    private final List<@PolyDet File> sourceFiles;

    /** The compiler options. */
    private final List<@PolyDet String> options;

    /** The compiler diagnostics. */
    private final DiagnosticCollector<@PolyDet JavaFileObject> diagnostics;

    /**
     * Creates a {@link FileCompilerException} with the error message, the list of source classes,
     * and compiler diagnostics from the compilation.
     *
     * @param message the exception message
     * @param sourceFiles the list of source files
     * @param options the list of compiler options
     * @param diagnostics the compiler diagnostics
     */
    FileCompilerException(
        String message,
        @PolyDet List<@PolyDet File> sourceFiles,
        @PolyDet List<@PolyDet String> options,
        DiagnosticCollector<@PolyDet JavaFileObject> diagnostics) {
      super(message);
      this.sourceFiles = sourceFiles;
      this.options = options;
      this.diagnostics = diagnostics;
    }

    /**
     * Returns the list of source files used in the compilation that generated this error.
     *
     * @return the list of source files for which compilation generated this exception
     */
    public List<@PolyDet File> getSourceFiles() {
      return sourceFiles;
    }

    /**
     * Returns the list of compiler options used in the compilation that generated this error.
     *
     * @return the list of compiler options for which compilation generated this exception
     */
    public List<@PolyDet String> getOptions() {
      return options;
    }

    /**
     * Returns the compiler diagnostics for the compilation that generated this exception.
     *
     * @return the compiler diagnostics for the compilation that generated this exception
     */
    public DiagnosticCollector<@PolyDet JavaFileObject> getDiagnostics() {
      return diagnostics;
    }
  }
}
