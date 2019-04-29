package website.cousinomath;

import java.util.function.Function;
import java.util.function.Supplier;

final class Left<R, S> extends Either<R, S>{
  private final R lvalue;

  public Left(R lvalue) { this.lvalue = lvalue; }
  @Override
  public boolean isLeft() { return true; }
  @Override
  public boolean isRight() { return false; }
  @Override
  public R getLeft() { return this.lvalue; }
  @Override
  public R leftOrElse(R dflt) { return this.lvalue; }
  @Override
  public R leftOrElseGet(Supplier<? extends R> dflt) { return this.lvalue; }
  @Override
  public S getRight() {
    throw new UnsupportedOperationException("Cannot get a Right value out of Left");
  }
  @Override
  public S rightOrElse(S dflt) { return dflt; }
  @Override
  public S rightOrElseGet(Supplier<? extends S> dflt) { return dflt.get(); }
  @Override
  public <U> Either<U, S> mapLeft(Function<R, U> f) {
    return new Left<U, S>(f.apply(this.lvalue));
  }
  @Override
  public <V> Either<R, V> mapRight(Function<S, V> g) {
    return new Left<R, V>(this.lvalue);
  }
  @Override
  public <U> Either<U, S> flatMapLeft(Function<R, Either<U, S>> f) {
    return f.apply(this.lvalue);
  }
  @Override
  public <V> Either<R, V> flatMapRight(Function<S, Either<R, V>> g) {
    return new Left<R, V>(this.lvalue);
  }
  @Override
  public <T> T either(Function<R, T> f, Function<S, T> g) {
    return f.apply(this.lvalue);
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof Left) {
      Object otherLeft = ((Left<?,?>)other).lvalue;
      return this.lvalue.equals(otherLeft);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return this.lvalue.hashCode() << 1;
  }
}