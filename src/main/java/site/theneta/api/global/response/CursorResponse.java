package site.theneta.api.global.response;

import java.util.List;
import java.util.function.Function;

public class CursorResponse<T, C> {
  private List<T> content;
  private C nextCursor;
  private int size;
  private boolean hasNext;

  private CursorResponse(List<T> content, C nextCursor, int size, boolean hasNext) {
    this.content = content;
    this.nextCursor = nextCursor;
    this.size = size;
    this.hasNext = hasNext;
  }

  public static <T, C> CursorResponse<T, C> of(List<T> content, C nextCursor, int size, boolean hasNext) {
    return new CursorResponse<>(content, nextCursor, size, hasNext);
  }

  // 마지막 아이템의 ID를 커서로 사용하는 경우를 위한 유틸리티 메서드
  public static <T> CursorResponse<T, Long> of(List<T> content, int size, Function<T, Long> cursorExtractor) {
    boolean hasNext = content.size() > size;
    // size보다 1개 더 조회했다면 마지막 항목은 제거
    List<T> actualContent = hasNext ? content.subList(0, size) : content;

    Long nextCursor = hasNext && !actualContent.isEmpty()
        ? cursorExtractor.apply(actualContent.get(actualContent.size() - 1))
        : null;

    return new CursorResponse<>(actualContent, nextCursor, size, hasNext);
  }

  public List<T> getContent() {
    return content;
  }

  public C getNextCursor() {
    return nextCursor;
  }

  public int getSize() {
    return size;
  }

  public boolean isHasNext() {
    return hasNext;
  }
}