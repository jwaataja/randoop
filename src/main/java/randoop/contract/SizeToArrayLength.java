package randoop.contract;

import java.util.Arrays;
import java.util.Collection;
import org.checkerframework.checker.determinism.qual.Det;
import org.checkerframework.checker.determinism.qual.PolyDet;
import randoop.types.JavaTypes;
import randoop.types.TypeTuple;

/** The contract: {@code c.toArray().length == c.size()} for all Collections c. */
public final class SizeToArrayLength extends ObjectContract {
  private static final @Det SizeToArrayLength instance = new SizeToArrayLength();

  private SizeToArrayLength() {}

  public static @Det SizeToArrayLength getInstance() {
    return instance;
  }

  @Override
  public boolean evaluate(Object... objects) {
    assert objects != null && objects.length == 1;
    Object o = objects[0];
    if (o instanceof Collection) {
      @PolyDet("up") Collection<? extends @PolyDet("up") Object> c =
          (Collection<? extends @PolyDet("up") Object>) o;
      assert c != null;
      return c.size() == c.toArray().length;
    }
    return true;
  }

  @Override
  public int getArity() {
    return 1;
  }

  /** The arguments to which this contract can be applied. */
  static TypeTuple inputTypes = new TypeTuple(Arrays.asList(JavaTypes.COLLECTION_TYPE));

  @Override
  public @Det TypeTuple getInputTypes() {
    return inputTypes;
  }

  @Override
  public String toCommentString(@Det SizeToArrayLength this) {
    return "x0.toArray().length == x0.size()";
  }

  @Override
  public String get_observer_str() {
    return "SizeToArrayLength";
  }

  @Override
  public String toCodeString(@Det SizeToArrayLength this) {
    StringBuilder b = new StringBuilder();
    b.append("org.junit.Assert.assertEquals(");
    b.append("\"Contract failed: " + toCommentString() + "\", ");
    b.append("x0.toArray().length, x0.size()");
    b.append(");");
    return b.toString();
  }
}
