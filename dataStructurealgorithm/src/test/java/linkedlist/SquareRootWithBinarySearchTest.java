package linkedlist;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 利用二分法求一个数的平方根
 */
public class SquareRootWithBinarySearchTest {

    @Test
    public void test() {
        for (int i = 0; i < 11; i++) {
            System.out.println(squareRoot(new BigDecimal(3), i));
        }
    }


    /**
     * 求一个数的平方根
     * @param input 数据
     * @param scale 结果保留的小数位
     */
    public static BigDecimal squareRoot(BigDecimal input, int scale) {
        // input <= 0
        if (input.compareTo(new BigDecimal(0)) != 1) {
            throw new RuntimeException("不能输入负数或0");
        }

        // input = 1
        if (input.compareTo(new BigDecimal(1)) == 0) {
            return new BigDecimal(1);
        }

        //申请结果范围为(start, end)
        BigDecimal start, end;
        if (input.compareTo(new BigDecimal(1)) == 1) {//input > 1，结果范围为(1, input)
            start = new BigDecimal(1);
            end = input;
        } else {//0 < input < 1, 结果范围为(1, input)
            start = input;
            end = new BigDecimal(1);
        }

        return findSquareRoot(input, start, end, scale + 1, 1)
            .setScale(scale, RoundingMode.HALF_UP);

    }

    /**
     * 利用二分法查找，求一个数的平方根
     */
    public static BigDecimal findSquareRoot(BigDecimal squareValue, BigDecimal min, BigDecimal max,
        int scale, int deep) {

        //采用二分法，middle = （start + end）/2
        BigDecimal middle = min.add(max).divide(new BigDecimal(2));

        //递归
        int result = checkSquare(squareValue, middle, scale);
        switch (result) {
            case 0:
                System.out.println("递归深度为 = " + deep);
                return middle.setScale(scale, RoundingMode.HALF_UP);
            case 1:
                return findSquareRoot(squareValue, min, middle, scale, ++deep);
            default:
                return findSquareRoot(squareValue, middle, max, scale, ++deep);
        }

    }

  /**
   * 校验 input * input == squareValue
   * <p>
   * 返回0， input * input == squareValue
   * 返回1， input * input > squareValue
   * 返回-1，input * input < squareValue
   */
  private static int checkSquare(BigDecimal squareValue, BigDecimal input, int scale) {
    /** 浮动的确定 **/
    BigDecimal floatRange = new BigDecimal(1);//结果误差范围值
    for (int i = 1; i <= scale + 2; i++) {//多保留精确两位，确保所需要的小数点后面一位数据是真实的
      floatRange = floatRange.divide(new BigDecimal(10));
    }
    BigDecimal upInput = input.add(floatRange);
    BigDecimal upSquare = upInput.multiply(upInput);
    BigDecimal downInput = input.subtract(floatRange);
    BigDecimal downSquare = downInput.multiply(downInput);
    BigDecimal middleSquareValue = input.multiply(input);

    /** 结果判断 **/
    if (upSquare.compareTo(squareValue) == 1 && downSquare.compareTo(squareValue) == -1) {//输入值在误差范围内
      return 0;
    } else if (middleSquareValue.compareTo(squareValue) == 1) {//输入值过大
      return 1;
    } else {//输入值过小
      return -1;
    }
  }

    //返回有多少个小数点
    private static int getScale(Number input) {
        String s = input.toString();
        String substring = input.toString().substring(s.indexOf(".") + 1, s.length());
        int length = substring.length();
        return length;
    }


}
