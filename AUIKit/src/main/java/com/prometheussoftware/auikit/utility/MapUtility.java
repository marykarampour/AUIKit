package com.prometheussoftware.auikit.utility;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class MapUtility {
     public static <K, O> Map<K, O> mapWithObjectForKeys (List<O> objects, List<K> keys) {
        if (objects == null || keys == null || objects.size() != keys.size())
            return Collections.emptyMap();

        HashMap<K, O> map = new HashMap<>();
        for (O obj : objects) {
            map.put(keys.get(objects.indexOf(obj)), obj);
        }
        return map;
    }

    public static <K, O> Set<K> allKeysForObject (Map<K, O> map, O obj) {
         return map.entrySet().stream().filter(e -> Objects.equals(e.getValue(), obj)).map(Map.Entry::getKey).collect(Collectors.toSet());
    }
}
