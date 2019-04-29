package website.cousinomath;

import java.util.function.Function;

abstract class Result<R, S> {
  abstract public boolean isOk();
  abstract public boolean isErr();
  abstract public R unwrap();
  abstract public S unwrapErr();
  abstract public R expected(String message);
  abstract public <U> Result<U, S> map(Function<R, U> f);
  abstract public <U> U mapOrElse(Function<R, U> f, Function<S, U> g);
  abstract public R okOr(R dflt);
  abstract public R okOrElse(Function<S, R> g);
}