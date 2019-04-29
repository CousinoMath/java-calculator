package website.cousinomath;

import java.util.function.Function;

final class Err<R, S> extends Result<R, S> {
  private final S errValue;

  public Err(S errValue) { this.errValue = errValue; }
  @Override
  public boolean isOk() { return false; }
  @Override
  public boolean isErr() { return true; }
  @Override
  public R unwrap() {
    throw new UnsupportedOperationException("Tried to unwrap an Err value");
  }
  @Override
  public S unwrapErr() { return this.errValue; }
  @Override
  public R expected(String message) {
    StringBuilder sb = new StringBuilder(message);
    sb.append(" ");
    sb.append(this.errValue.toString());
    throw new UnsupportedOperationException(sb.toString());
  }
  @Override
  public <U> Result<U, S> map(Function<R, U> f) {
    return new Err<U, S>(this.errValue);
  }
  @Override
  public <U> U mapOrElse(Function<R, U> f, Function<S, U> g) {
    return g.apply(this.errValue);
  }
  @Override
  public R okOr(R dflt) {
    return dflt;
  }
  @Override
  public R okOrElse(Function<S, R> g) {
    return g.apply(this.errValue);
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof Err) {
      Object otherErr = ((Err<?,?>)other).errValue;
      return this.errValue.equals(otherErr);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return 1 + this.errValue.hashCode() << 1;
  }
}