package website.cousinomath;

import java.util.function.Function;
import java.util.function.Supplier;

final class Right<R, S> extends Either<R, S>{
  private final S rvalue;

  public Right(S rvalue) { this.rvalue = rvalue; }
  @Override
  public boolean isLeft() { return false; }
  @Override
  public boolean isRight() { return true; }
  @Override
  public R getLeft() {
    throw new UnsupportedOperationException("Cannot get a Left value out of Right");
  }
  @Override
  public R leftOrElse(R dflt) { return dflt; }
  @Override
  public R leftOrElseGet(Supplier<? extends R> dflt) { return dflt.get(); }
  @Override
  public S getRight() { return this.rvalue; }
  @Override
  public S rightOrElse(S dflt) { return this.rvalue; }
  @Override
  public S rightOrElseGet(Supplier<? extends S> dflt) { return this.rvalue; }
  @Override
  public <U> Either<U, S> mapLeft(Function<R, U> f) {
    return new Right<U, S>(this.rvalue);
  }
  @Override
  public <V> Either<R, V> mapRight(Function<S, V> g) {
    return new Right<R, V>(g.apply(this.rvalue));
  }
  @Override
  public <U> Either<U, S> flatMapLeft(Function<R, Either<U, S>> f) {
    return new Right<U, S>(this.rvalue);
  }
  @Override
  public <V> Either<R, V> flatMapRight(Function<S, Either<R, V>> g) {
    return g.apply(this.rvalue);
  }
  @Override
  public <T> T either(Function<R, T> f, Function<S, T> g) {
    return g.apply(this.rvalue);
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof Right) {
      Object otherRight = ((Right<?,?>)other).rvalue;
      return this.rvalue.equals(otherRight);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return 1 + this.rvalue.hashCode() << 1;
  }
}