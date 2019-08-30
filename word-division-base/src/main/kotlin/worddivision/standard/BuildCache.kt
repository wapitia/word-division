package com.wapitia.games.worddivision.standard

/**
 * A mutable simple map for accumulating indexed values for textBuilder classes
 */
class BuildCache<K,V>
    // primary constructor
    (val cache : MutableMap<K,V>, val producer: (K) -> V)
{
    /**
     * A useful constructor where the cache is initially empty and only
     * the producer lambda is provided
     */
    constructor (producer: (K) -> V) : this(mutableMapOf(), producer)

    operator fun get(key: K): V {
        return StandardCollectionUtility.mapFetch(cache, key, producer)
    }
}
