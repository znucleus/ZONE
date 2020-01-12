package top.zbeboy.zone.web.util;

import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
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

    /**
     * string id to list number
     *
     * @param ids ids
     * @return list
     */
    public static List<Integer> StringIdsToNumberList(String ids) {
        List<Integer> newIds = new ArrayList<>();
        Arrays.stream(ids.split(",")).filter(NumberUtils::isDigits).forEach(id ->
                newIds.add(NumberUtils.toInt(id)));
        return newIds;
    }
}
