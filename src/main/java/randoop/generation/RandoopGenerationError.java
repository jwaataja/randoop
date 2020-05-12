package randoop.generation;

import org.checkerframework.checker.determinism.qual.PolyDet;
import randoop.operation.TypedOperation;

/** Error class to signal generation errors that should stop Randoop execution. */
public class RandoopGenerationError extends Error {

  private static final long serialVersionUID = -2655768762421700468L;

  private final TypedOperation operation;
  private final Throwable exception;

  /**
   * Create a {@link RandoopGenerationError} representing an error that occurs during the generation
   * of a sequence for the given operation.
   *
   * @param operation the actual operation
   * @param exception the exception thrown during generation
   */
  RandoopGenerationError(@PolyDet TypedOperation operation, @PolyDet Throwable exception) {
    this.operation = operation;
    this.exception = exception;
  }

  /**
   * Returns the name of the {@code java.lang.reflect.AccessibleObject} underlying the operation in
   * this object.
   *
   * @return the name of the reflection object for the operation
   */
  @SuppressWarnings("determinism") // AccessibleObject toString is actually deterministic
  public String getOperationName() {
    return operation.getOperation().getReflectionObject().toString();
  }

  /**
   * Return the {@code String} representation of the {@link TypedOperation} in this error.
   *
   * @return the {@code String} representation of the operation
   */
  public String getInstantiatedOperation() {
    return operation.toString();
  }

  /**
   * Return the {@code Throwable} for this error.
   *
   * @return the {@code Throwable} for this error
   */
  public Throwable getException() {
    return exception;
  }
}
