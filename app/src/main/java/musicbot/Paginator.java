package musicbot;

import java.util.ArrayList;
import java.util.List;

public class Paginator {

  public static class PaginatedItems<T>  {
    private List<T> list = new ArrayList<T>();
    private static boolean isLastPage = true;
    private static int nextPageNum;

    public void set(List<T> items){
      list = items;
    }

    public void setNextPageNum(final int nextPage) {
      nextPageNum = nextPage;
    }

    public int getNextPageNum() {
      return nextPageNum;
    }

    public void setIsLastPage(final boolean isLastPageBool) {
      isLastPage = isLastPageBool;
    }

    public boolean isIsLastPage() {
      return isLastPage;
    }

    public List<T> get() {
      return list;
    }
  }

  public <E> PaginatedItems<E> paginate(
      final List<E> tracks,
      final int numItemsPerPage,
      final int pageNumber
  ) {
    if (tracks.isEmpty()) {
      return new PaginatedItems<>();
    }

    final int maxSize = tracks.size();
    final int maxPage = (int) Math.floor(maxSize / numItemsPerPage);
    final int maxIndex = numItemsPerPage * pageNumber;
    final int baseIndex = maxIndex - numItemsPerPage;
    final int nextPage = pageNumber + 1;
    final PaginatedItems<E> paginatedItems = new PaginatedItems<>();

    if (nextPage <= maxPage) {
      paginatedItems.set(tracks.subList(baseIndex, maxIndex));
      paginatedItems.setIsLastPage(false);
      paginatedItems.setNextPageNum(nextPage);
    } else {
      paginatedItems.set(tracks.subList(maxIndex, maxSize));
      paginatedItems.setIsLastPage(true);
    }

    return paginatedItems;
  }
}
