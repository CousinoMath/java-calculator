package website.cousinomath;

import java.util.function.Function;

class Constant<R, S> implements Function<S, R> {
  private final R value;

  public Constant(R value) {
    this.value = value;
  }

  @Override
  public R apply(S x) {
    return this.value;
  }
}