package com.wapitia.games.worddivision.standard

/**
 * A mutable simple map for accumulating indexed values hander for textBuilder classes
 */
class BuildCache<K,V>
    // primary constructor
    (val cache : MutableMap<K,V>, val producer: (K) -> V)
{
    constructor (producer: (K) -> V) : this(mutableMapOf(), producer)

    operator fun get(key: K): V {
        return StandardCollectionUtility.mapFetch(cache, key, producer)
    }
}
