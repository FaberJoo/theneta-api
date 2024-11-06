package site.theneta.api.global.response;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SingleResponse<T> {
  private T item;

  public static <T> SingleResponse<T> of(T item) {
    return new SingleResponse<>(item);
  }
}