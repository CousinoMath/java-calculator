package website.cousinomath;

import java.util.function.Function;

final class Ok<R, S> extends Result<R, S> {
  private final R okValue;

  public Ok(R okValue) { this.okValue = okValue; }
  @Override
  public boolean isOk() { return true; }
  @Override
  public boolean isErr() { return false; }
  @Override
  public R unwrap() { return this.okValue; }
  @Override
  public S unwrapErr() {
    throw new UnsupportedOperationException("Cannot get an Err value out of Ok");
  }
  @Override
  public R expected(String message) { return this.okValue; }
  @Override
  public <U> Result<U, S> map(Function<R, U> f) {
    return new Ok<U, S>(f.apply(this.okValue));
  }
  @Override
  public <U> U mapOrElse(Function<R, U> f, Function<S, U> g) {
    return f.apply(this.okValue);
  }
  @Override
  public R okOr(R dflt) { return this.okValue; }
  @Override
  public R okOrElse(Function<S, R> g) { return this.okValue; }

  @Override
  public boolean equals(Object other) {
    if (other instanceof Ok) {
      Object otherOk = ((Ok<?,?>)other).okValue;
      return this.okValue.equals(otherOk);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return this.okValue.hashCode() << 1;
  }
}