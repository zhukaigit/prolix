package linkedlist;

import com.zk.sort.BinarySearch;
import org.junit.Test;

public class BinarySearchTest {

  @Test
  public void testSearchFirstEQ() {
    int[] arr = {1, 2, 3, 3, 3, 5, 6, 6};
    System.out.println(BinarySearch.searchFirstEQ(arr, 0));
    System.out.println(BinarySearch.searchFirstEQ(arr, 1));
    System.out.println(BinarySearch.searchFirstEQ(arr, 2));
    System.out.println(BinarySearch.searchFirstEQ(arr, 3));
    System.out.println(BinarySearch.searchFirstEQ(arr, 6));
  }
  @Test
  public void testSearchLastEQ() {
    int[] arr = {1, 2, 3, 3, 3, 5, 6, 6};
    System.out.println(BinarySearch.searchLastEQ(arr, 0));
    System.out.println(BinarySearch.searchLastEQ(arr, 1));
    System.out.println(BinarySearch.searchLastEQ(arr, 2));
    System.out.println(BinarySearch.searchLastEQ(arr, 3));
    System.out.println(BinarySearch.searchLastEQ(arr, 6));
  }
  @Test
  public void searchFirstEqualOrGreater() {
    int[] arr = {1, 2, 3, 3, 3, 5, 6, 6};
    System.out.println(BinarySearch.searchFirstEqualOrGreater(arr, 0));
    System.out.println(BinarySearch.searchFirstEqualOrGreater(arr, 1));
    System.out.println(BinarySearch.searchFirstEqualOrGreater(arr, 2));
    System.out.println(BinarySearch.searchFirstEqualOrGreater(arr, 3));
    System.out.println(BinarySearch.searchFirstEqualOrGreater(arr, 6));
  }
  @Test
  public void searchLastEqualOrLess() {
    int[] arr = {1, 2, 3, 3, 3, 5, 6, 6};
    System.out.println(BinarySearch.searchLastEqualOrLess(arr, 0));
    System.out.println(BinarySearch.searchLastEqualOrLess(arr, 1));
    System.out.println(BinarySearch.searchLastEqualOrLess(arr, 2));
    System.out.println(BinarySearch.searchLastEqualOrLess(arr, 3));
    System.out.println(BinarySearch.searchLastEqualOrLess(arr, 4));
    System.out.println(BinarySearch.searchLastEqualOrLess(arr, 6));
  }

}
