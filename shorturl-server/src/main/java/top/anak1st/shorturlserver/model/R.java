package top.anak1st.shorturlserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class R {
    private String message;
    private Object data;


    public static R ok(Object data) {
        return new R("OK", data);
    }

    public static R error(String message) {
        return new R(message, null);
    }
}
