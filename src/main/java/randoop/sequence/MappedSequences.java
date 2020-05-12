package randoop.sequence;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.checkerframework.checker.determinism.qual.CollectionType;
import org.checkerframework.checker.determinism.qual.Det;
import org.checkerframework.checker.determinism.qual.PolyDet;
import randoop.types.Type;
import randoop.util.ListOfLists;
import randoop.util.SimpleList;

/**
 * A multimap from keys of type T to sequences. Such a map can be useful to specify sequences that
 * should only be used in specific contexts, for example sequences that should only be used as
 * components when testing a specific class.
 */
@CollectionType
@SuppressWarnings("determinism") // https://github.com/t-rasmud/checker-framework/issues/134
public class MappedSequences<T extends @PolyDet Object> {

  private Map<T, @PolyDet SequenceCollection> map;

  public MappedSequences() {
    this.map = new @PolyDet LinkedHashMap<>();
  }

  /**
   * Adds a sequence to the set of sequences associated with the given key.
   *
   * @param key the key value
   * @param seq the sequence
   */
  public void addSequence(@Det MappedSequences<T> this, T key, @Det Sequence seq) {
    if (seq == null) throw new IllegalArgumentException("seq is null");
    if (key == null) throw new IllegalArgumentException("key is null");
    @Det SequenceCollection c = map.get(key);
    if (c == null) {
      c = new SequenceCollection();
      map.put(key, c);
    }
    c.add(seq);
  }

  /**
   * Returns the set of sequences (as a list) that are associated with the given key and create
   * values of the desiredType.
   *
   * @param key the key value
   * @param desiredType the query type
   * @return the list of sequences for the key and query type
   */
  public @Det SimpleList<Sequence> getSequences(
      @Det MappedSequences<T> this, @Det T key, @Det Type desiredType) {
    if (key == null) {
      throw new IllegalArgumentException("key is null");
    }
    @Det SequenceCollection c = map.get(key);
    if (c == null) {
      @Det SimpleList<Sequence> tmp = emptyList;
      return tmp;
    }
    return map.get(key).getSequencesForType(desiredType, true, false);
  }

  // Cached empty list used by getSequences method.
  private static final SimpleList<Sequence> emptyList;

  static {
    List<SimpleList<Sequence>> emptyJDKList = Collections.emptyList();
    emptyList = new ListOfLists<>(emptyJDKList);
  }

  /**
   * Returns all sequences as the union of all of the sequence collections.
   *
   * @return the set of all sequence objects in this set of collections
   */
  public Set<@PolyDet Sequence> getAllSequences() {
    @PolyDet Set<@PolyDet Sequence> result = new @PolyDet LinkedHashSet<>();
    for (@PolyDet("up") SequenceCollection c : map.values()) {
      @SuppressWarnings("determinism") // iterating over @PolyDet collection to create another
      boolean ignore = result.addAll(c.getAllSequences());
    }
    return result;
  }
}
