package vn.ptit.judge.util;

public class AnswerComparator {

    public static boolean compareIntegers(Integer expected, Integer actual) {
        return expected != null && actual != null && expected.equals(actual);
    }

    public static boolean compareStrings(String expected, String actual) {
        return expected != null && actual != null && expected.equals(actual);
    }

    public static boolean compareDouble(double expected, double actual) {
        return Math.abs(expected - actual) <= 0.01;
    }
}
