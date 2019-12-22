package top.zbeboy.zone.web.util;

import java.util.Arrays;
import java.util.List;

public class SmallPropsUtil {

    /**
     * string id to list string
     *
     * @param ids ids
     * @return list
     */
    public static List<String> StringIdsToStringList(String ids) {
        return Arrays.asList(ids.split(","));
    }
}
