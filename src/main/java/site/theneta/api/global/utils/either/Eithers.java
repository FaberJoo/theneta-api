package site.theneta.api.global.utils.either;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * This class contains static utility methods related to
 * the {@link Either} type.
 */
public final class Eithers {

  private static final Set<Collector.Characteristics> CH_NOID = Set.of();

  /**
   * Returns a {@code Collector} that accumulates the input elements into
   * a Right containing all values in the original order,
   * but only if there are no Left instances in the stream.
   * If the stream does contain a Left instance, it discards the Right instances and
   * accumulates a Left instance, which contains the first LHS value in the stream,
   * in encounter order.
   *
   * @param <L> the type of the LHS values in the stream
   * @param <R> the type of the RHS values in the stream
   * @return a {@code Collector} which collects all the input elements into
   *         a Right containing all RHS values in the stream, or,
   *         if an LHS value exists, a Left containing the first LHS value
   */
  public static <L, R>
  Collector<Either<? extends L, ? extends R>, ?, Either<L, List<R>>>
  firstFailure() {

    BiConsumer<FirstFailureAcc<L, R>, Either<? extends L, ? extends R>> accumulator = (acc, either) ->
        either.ifLeftOrElse(acc::addLeft, acc::addRight);

    BinaryOperator<FirstFailureAcc<L, R>> combiner = (acc, other) ->
        (FirstFailureAcc<L, R>) acc.combine(other);

    return new CollectorImpl<>(FirstFailureAcc::new, accumulator, combiner, FirstFailureAcc::finish);
  }

  /**
   * Returns a {@code Collector} that accumulates the input elements into
   * a Right containing all values in the original order,
   * but only if there are no Left instances in the stream.
   * If the stream does contain a Left instance, it discards the Right instances and
   * accumulates a Left containing only the LHS values,
   * in encounter order.
   *
   * @param <L> the type of the LHS values in the stream
   * @param <R> the type of the RHS values in the stream
   * @return a {@code Collector} which collects all the input elements into
   *         a Right containing all RHS values in the stream,
   *         or, if an LHS value exists, a Left containing a nonempty list
   *         of all LHS values in the stream
   */
  public static <L, R>
  Collector<Either<? extends L, ? extends R>, ?, Either<List<L>, List<R>>>
  allFailures() {

    BiConsumer<AllFailuresAcc<L, R>, Either<? extends L, ? extends R>> accumulator = (acc, either) ->
        either.ifLeftOrElse(acc::addLeft, acc::addRight);

    BinaryOperator<AllFailuresAcc<L, R>> combiner = (acc, other) ->
        (AllFailuresAcc<L, R>) acc.combine(other);

    return new CollectorImpl<>(AllFailuresAcc::new, accumulator, combiner, AllFailuresAcc::finish);
  }

  /**
   * Returns a {@code Collector} that accumulates the input elements into
   * a new {@code List}. There are no guarantees on the type, mutability,
   * serializability, or thread-safety of the {@code List} returned.
   * The final list is wrapped in an {@code Optional},
   * which is empty if and only if the list is empty.
   *
   * @see #optionalList(List)
   * @param <T> the type of the input elements
   * @return a list of the RHS values in the stream,
   *         or, if an LHS value exists, a nonempty list of all LHS values
   */
  public static <T> Collector<T, ?, Optional<List<T>>> toOptionalList() {
    return Collectors.collectingAndThen(
        Collectors.toList(),
        Eithers::optionalList);
  }

  /**
   * If the provided list is empty, returns an empty {@link Optional}.
   * Otherwise, returns an {@code Optional} containing the nonempty
   * input list.
   *
   * <p>Note: The resulting {@code Optional} might be used in a
   * {@link Either#filter(Function) filter} or
   * {@link Either#filterLeft(Function) filterLeft} operation.
   *
   * @see #toOptionalList()
   * @param values a list of objects
   * @param <T> the type of the members of {@code values}
   * @return an {@code Optional} which is either empty, or
   *         contains a nonempty list
   */
  public static <T> Optional<List<T>> optionalList(List<? extends T> values) {
    if (values.isEmpty()) {
      return Optional.empty();
    }
    @SuppressWarnings("unchecked")
    List<T> result = (List<T>) values;
    return Optional.of(result);
  }

  /**
   * Simple implementation class for a collector with characteristic {@link #CH_NOID}.
   *
   * @param <T> the type of elements to be collected
   * @param <R> the type of the result
   */
  private static final class CollectorImpl<T, A, R> implements Collector<T, A, R> {
    final Supplier<A> supplier;
    final BiConsumer<A, T> accumulator;
    final BinaryOperator<A> combiner;
    final Function<A, R> finisher;

    CollectorImpl(Supplier<A> supplier,
        BiConsumer<A, T> accumulator,
        BinaryOperator<A> combiner,
        Function<A, R> finisher) {
      this.supplier = supplier;
      this.accumulator = accumulator;
      this.combiner = combiner;
      this.finisher = finisher;
    }

    @Override
    public BiConsumer<A, T> accumulator() {
      return accumulator;
    }

    @Override
    public Supplier<A> supplier() {
      return supplier;
    }

    @Override
    public BinaryOperator<A> combiner() {
      return combiner;
    }

    @Override
    public Function<A, R> finisher() {
      return finisher;
    }

    @Override
    public Set<Characteristics> characteristics() {
      return CH_NOID;
    }
  }

  /**
   * @param <L> Type of LHS values in the stream
   * @param <C> Type of collected LHS values
   * @param <R> Type of RHS values in the stream
   */
  // visible for testing
  static abstract class Acc<L, C, R> {
    private ArrayList<R> right;

    abstract void combineLeft(C otherLeft);

    // nullable
    abstract C leftColl();

    abstract void addLeft(L left);

    final void addRight(R value) {
      if (leftColl() != null) {
        return;
      }
      if (right == null) {
        right = new ArrayList<>();
      }
      right.add(value);
    }

    final Acc<L, C, R> combine(Acc<L, C, R> other) {
      if (leftColl() != null) {
        combineLeft(other.leftColl());
        return this;
      }
      if (other.leftColl() != null) {
        return other;
      }
      if (other.right == null) {
        return this;
      }
      if (right == null) {
        right = other.right;
      } else {
        right.addAll(other.right);
      }
      return this;
    }

    final Either<C, List<R>> finish() {
      C left = leftColl();
      return left != null
          ? Either.left(left)
          : Either.right(right == null ? List.of() : right);
    }
  }

  private static final class FirstFailureAcc<L, R> extends Acc<L, L, R> {
    L left;

    @Override
    void combineLeft(L otherLeft) {
      addLeft(otherLeft);
    }

    @Override
    void addLeft(L value) {
      if (left == null) {
        left = value;
      }
    }

    L leftColl() {
      return left;
    }
  }

  private static final class AllFailuresAcc<L, R> extends Acc<L, List<L>, R> {
    List<L> left;

    @Override
    void combineLeft(List<L> otherLeft) {
      if (left == null) {
        left = otherLeft;
      } else if (otherLeft != null) {
        left.addAll(otherLeft);
      }
    }

    @Override
    void addLeft(L value) {
      if (left == null) {
        left = new ArrayList<>();
      }
      left.add(value);
    }

    @Override
    List<L> leftColl() {
      return left;
    }
  }

  private Eithers() {
  }
}