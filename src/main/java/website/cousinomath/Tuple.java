package website.cousinomath;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

class Tuple<R, S> {
  public final R first;
  public final S second;

  public Tuple(R first, S second) {
    this.first = first;
    this.second = second;
  }

  public Tuple<S, R> flip() {
    return new Tuple<>(this.second, this.first);
  }

  public static <R, S, T> Function<Tuple<R, S>, T> uncurry(BiFunction<R, S, T> f) {
    return (tuple) -> f.apply(tuple.first, tuple.second);
  }

  public static <R, S, T> BiFunction<R, S, T> curry(Function<Tuple<R, S>, T> f) {
    return (first, second) -> f.apply(new Tuple<>(first, second));
  }

  public static <R, S> Consumer<Tuple<R, S>> uncurry(BiConsumer<R, S> f) {
    return (tuple) -> f.accept(tuple.first, tuple.second);
  }

  public static <R, S> BiConsumer<R, S> curry(Consumer<Tuple<R, S>> f) {
    return (first, second) -> f.accept(new Tuple<>(first, second));
  }

  public static <R, S> Predicate<Tuple<R, S>> uncurry(BiPredicate<R, S> f) {
    return (tuple) -> f.test(tuple.first, tuple.second);
  }

  public static <R, S> BiPredicate<R, S> curry(Predicate<Tuple<R, S>> f) {
    return (first, second) -> f.test(new Tuple<>(first, second));
  }

  public static <R, S, T> Tuple<R, Tuple<S, T>> assocRight(Tuple<Tuple<R, S>, T> tuple) {
    return new Tuple<>(tuple.first.first, new Tuple<>(tuple.first.second, tuple.second));
  }

  public static <R, S, T> Tuple<Tuple<R, S>, T> assocLeft(Tuple<R, Tuple<S, T>> tuple) {
    return new Tuple<>(new Tuple<>(tuple.first, tuple.second.first), tuple.second.second);
  }

  public static <T> Tuple<T, T> fromPartition(Map<Boolean, T> xm) {
    T trues = xm.get(Boolean.TRUE);
    T falses = xm.get(Boolean.FALSE);
    return new Tuple<>(trues, falses);
  }

  public static <T> Map<Boolean, T> toPartition(Tuple<T, T> xx) {
    TreeMap<Boolean, T> xm = new TreeMap<>();
    xm.put(Boolean.TRUE, xx.first);
    xm.put(Boolean.FALSE, xx.second);
    return xm;
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof Tuple) {
      Object first = ((Tuple<?,?>)other).first;
      Object second = ((Tuple<?,?>)other).second;
      return this.first.equals(first) && this.second.equals(second);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return this.first.hashCode() ^ this.second.hashCode();
  }
}