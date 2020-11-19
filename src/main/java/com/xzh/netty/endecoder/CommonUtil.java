package com.xzh.netty.endecoder;

public class CommonUtil {
    public static final int FIXED_LENGTH_FRAME_LENGTH = 256;

    public static String formatStr(String str, int assignLength) {
        int intStrlen = 0;
        if (str != null) {
            intStrlen = str.length();
        }
        if (intStrlen >= assignLength) {
            return str;
        } else {
            StringBuilder strSpace = new StringBuilder();
            for (int i = 0, num = assignLength - intStrlen; i < num; i++) {
                strSpace.append(" ");
            }
            return str + strSpace;

        }

    }
}
