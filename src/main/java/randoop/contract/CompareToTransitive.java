package randoop.contract;

import java.util.Arrays;
import org.checkerframework.checker.determinism.qual.Det;
import org.checkerframework.checker.determinism.qual.PolyDet;
import randoop.Globals;
import randoop.types.JavaTypes;
import randoop.types.TypeTuple;

/**
 * The contract: Checks the transitivity of the compare to method.
 *
 * <pre>{@code
 * ((x0.compareTo(x1) > 0) && (x1.compareTo(x2) > 0))
 *  => (x0.compareTo(x2) > 0)
 * }</pre>
 */
public class CompareToTransitive extends ObjectContract {
  private static final @Det CompareToTransitive instance = new CompareToTransitive();

  private CompareToTransitive() {}

  public static @Det CompareToTransitive getInstance() {
    return instance;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public @PolyDet("up") boolean evaluate(Object... objects) {
    Object o1 = objects[0];
    Object o2 = objects[1];
    Object o3 = objects[2];

    // If o1 and o2 are comparable objects, check the implication
    if (o1 instanceof Comparable && o2 instanceof Comparable) {
      Comparable compObj1 = (Comparable) o1;
      Comparable compObj2 = (Comparable) o2;
      Comparable compObj3 = (Comparable) o3;

      boolean tmp =
          !(compObj1.compareTo(compObj2) > 0 && compObj2.compareTo(compObj3) > 0)
              || (compObj1.compareTo(compObj3) > 0);
      return tmp;
    }
    // If the compare to operation can't be done, the statement is trivially true
    return true;
  }

  @Override
  public int getArity() {
    return 3;
  }

  /** The arguments to which this contract can be applied. */
  static TypeTuple inputTypes =
      new TypeTuple(
          Arrays.asList(
              JavaTypes.COMPARABLE_TYPE, JavaTypes.COMPARABLE_TYPE, JavaTypes.COMPARABLE_TYPE));

  @Override
  public @Det TypeTuple getInputTypes() {
    return inputTypes;
  }

  @Override
  public String toCommentString(@Det CompareToTransitive this) {
    return "compareTo-transitive on x0, x1, and x2";
  }

  @Override
  public String get_observer_str() {
    return "CompareToTransitive";
  }

  @Override
  public String toCodeString(@Det CompareToTransitive this) {
    StringBuilder b = new StringBuilder();
    b.append(Globals.lineSep);
    b.append("// Checks the contract: ");
    b.append(" " + toCommentString() + Globals.lineSep);
    b.append("org.junit.Assert.assertTrue(");
    b.append("\"Contract failed: " + toCommentString() + "\", ");
    b.append("!(x0.compareTo(x1)>0 && x1.compareTo(x2)>0) || x0.compareTo(x2)>0");
    b.append(");");
    return b.toString();
  }
}
