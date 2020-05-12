package randoop.contract;

import java.util.Arrays;
import org.checkerframework.checker.determinism.qual.Det;
import org.checkerframework.checker.determinism.qual.PolyDet;
import randoop.Globals;
import randoop.types.JavaTypes;
import randoop.types.TypeTuple;

/**
 * The contract: {@code x == null}.
 *
 * <p>Obviously, this is not a property that must hold of all objects in a test. Randoop creates an
 * instance of this contract when, during execution of a sequence, it determines that the above
 * property holds. The property thus represents a <i>regression</i> as it captures the behavior of
 * the code when it is executed.
 */
public final class IsNull extends ObjectContract {

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    return o instanceof IsNull;
  }

  @Override
  public int hashCode() {
    return 37; // no state to compare.
  }

  @Override
  public boolean evaluate(Object... objects) throws Throwable {
    assert objects.length == 1;
    @SuppressWarnings("determinism") // varargs can't be @OrderNonDet so @PolyDet("up") same as @PolyDet
    @PolyDet boolean tmp = objects[0] == null;
    return tmp;
  }

  @Override
  public int getArity() {
    return 1;
  }

  /** The arguments to which this contract can be applied. */
  static TypeTuple inputTypes = new TypeTuple(Arrays.asList(JavaTypes.OBJECT_TYPE));

  @Override
  public @Det TypeTuple getInputTypes() {
    return inputTypes;
  }

  @Override
  public String toCodeString(@Det IsNull this) {
    StringBuilder b = new StringBuilder();
    b.append(Globals.lineSep);
    b.append(
        "// Regression assertion (captures the current behavior of the code)" + Globals.lineSep);
    b.append("org.junit.Assert.assertNull(x0);");
    return b.toString();
  }

  @Override
  public String toCommentString(@Det IsNull this) {
    return "x0 == null";
  }

  @Override
  public String get_observer_str() {
    return "isNull";
  }
}
