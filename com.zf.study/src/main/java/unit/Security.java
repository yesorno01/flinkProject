package unit;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Security {
    public static void main(String[] args) {
        String pspt_id = "201010203010201021";
        //加密
        String pspt_id_64 = Base64.getEncoder().encodeToString(pspt_id.getBytes(StandardCharsets.UTF_8));
        System.out.println("加密后的字符串为:" + pspt_id_64);

        //解密
        String unDecodeStr = new String(Base64.getDecoder().decode(pspt_id_64), StandardCharsets.UTF_8);
        System.out.println("解密后的字符串为" + unDecodeStr);
    }
}
