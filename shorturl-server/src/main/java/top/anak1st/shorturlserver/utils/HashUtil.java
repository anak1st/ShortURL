package top.anak1st.shorturlserver.utils;

import com.google.common.hash.Hashing;
import com.google.common.hash.HashFunction;
import java.nio.charset.StandardCharsets;

public class HashUtil {
    private static final char[] BASE62SET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
    private static final long BASE62SET_LENGTH = BASE62SET.length;

    private static final HashFunction MurmurHash = Hashing.murmur3_32();

    public static String convertDecToBase62(long num) {
        StringBuilder sb = new StringBuilder();
        while (num > 0) {
            int i = (int) (num % BASE62SET_LENGTH);
            sb.append(BASE62SET[i]);
            num /= BASE62SET_LENGTH;
        }
        return sb.reverse().toString();
    }

    public static String hashToBase62(String str) {
        long i = MurmurHash.hashString(str, StandardCharsets.UTF_8).asInt();
        i += (1L << 32);
        return convertDecToBase62(i);
    }
}
