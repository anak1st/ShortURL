package top.anak1st.shorturlserver.utils;

public class HashFunction {
    private static char[] BASE62SET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
    private static int BASE62SET_LENGTH = BASE62SET.length;
    private static int BASE = 256;

    public static String encode(int num) {
        StringBuilder sb = new StringBuilder();
        num += BASE;
        while (num > 0) {
            sb.append(BASE62SET[num % BASE62SET_LENGTH]);
            num /= BASE62SET_LENGTH;
        }
        return sb.reverse().toString();
    }
}
