package com.epcm.common;

import com.epcm.enunn.StatusEnum;
import com.epcm.exception.StatusException;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class CollectionUtil {
    public static <T> boolean isListUnique(List<T> list) {
        if (CollectionUtils.isEmpty(list) || list.size() > 1) {
            throw new StatusException(StatusEnum.LIST_IS_EMPTY_OR_NOT_UNIQUE);
        }
        return true;
    }

    public static <T> T getUniqueObjectFromList(List<T> list) {
        if (isListUnique(list)) {
            return list.get(0);
        }
        throw new StatusException(StatusEnum.LIST_IS_EMPTY_OR_NOT_UNIQUE);
    }
}