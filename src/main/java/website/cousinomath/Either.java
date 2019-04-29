package website.cousinomath;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

abstract class Either<R, S> {
  abstract public boolean isLeft();
  abstract public boolean isRight();
  abstract public R getLeft();
  abstract public R leftOrElse(R dflt);
  abstract public R leftOrElseGet(Supplier<? extends R> dflt);
  abstract public S getRight();
  abstract public S rightOrElse(S dflt);
  abstract public S rightOrElseGet(Supplier<? extends S> dflt);
  abstract public <U> Either<U, S> mapLeft(Function<R, U> f);
  abstract public <V> Either<R, V> mapRight(Function<S, V> g);
  abstract public <U> Either<U, S> flatMapLeft(Function<R, Either<U, S>> f);
  abstract public <V> Either<R, V> flatMapRight(Function<S, Either<R, V>> g);
  abstract public <T> T either(Function<R, T> f, Function<S, T> g);
  
  public static <R, S> List<R> lefts(List<Either<R, S>> xes) {
    return xes.parallelStream().filter(Either::isLeft)
      .map(Either::getLeft).collect(Collectors.toList());
  }
  
  public static <R, S> Set<R> lefts(Set<Either<R, S>> xes) {
    return xes.parallelStream().filter(Either::isLeft)
      .map(Either::getLeft).collect(Collectors.toSet());
  }

  public static <R, S> List<S> rights(List<Either<R, S>> xes) {
    return xes.parallelStream().filter(Either::isRight)
      .map(Either::getRight).collect(Collectors.toList());
  }

  public static <R, S> Set<S> rights(Set<Either<R, S>> xes) {
    return xes.parallelStream().filter(Either::isRight)
      .map(Either::getRight).collect(Collectors.toSet());
  }

  public static <R, S> Tuple<List<R>, List<S>> partition(List<Either<R, S>> xes) {
    Map<Boolean, List<Either<R, S>>> xesp = xes.parallelStream()
      .collect(Collectors.partitioningBy(Either::isRight));
    List<R> lefts = xesp.get(Boolean.FALSE).parallelStream().map(Either::getLeft)
      .collect(Collectors.toList());
    List<S> rights = xesp.get(Boolean.TRUE).parallelStream().map(Either::getRight)
      .collect(Collectors.toList());
    return new Tuple<>(lefts, rights);
  }

  public static <R, S> Tuple<Set<R>, Set<S>> partition(Set<Either<R, S>> xes) {
    Map<Boolean, List<Either<R, S>>> xesp = xes.parallelStream()
      .collect(Collectors.partitioningBy(Either::isRight));
    Set<R> lefts = xesp.get(Boolean.FALSE).parallelStream().map(Either::getLeft)
      .collect(Collectors.toSet());
    Set<S> rights = xesp.get(Boolean.TRUE).parallelStream().map(Either::getRight)
      .collect(Collectors.toSet());
    return new Tuple<>(lefts, rights);
  }
}