package de.tum.util;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class HashCollection<T, V> {

    private Map<T, HashSet<V>> set = new HashMap<>();

    public void put(T key, V value) {
        this.getSet(key).add(value);
    }

    public HashSet<V> get(T key) {
        return this.getSet(key);
    }

    public Set<V> values() {
        return this.set.values().stream().flatMap(HashSet::stream).collect(Collectors.toSet());
    }

    private HashSet<V> getSet(T key) {
        this.set.putIfAbsent(key, new HashSet<>());
        return this.set.get(key);
    }

}

