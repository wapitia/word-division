package worddivision.standard

object StandardCollectionUtility {

    /**
     * Produces elements from a list to be applied sequentially to a pair of consumer functions (ef and gf),
     * applied alternately for each element in the list, interleaving the gf calls between the elements.
     *
     * If the list is empty nothing happens.
     * Otherwise, ef is called once for each element in the list, gf is called one less times for each element in the list.
     * If the list is [A,B,C],
     * then the calls will be performed in the sequence:
     * ef(0,A), gf(1,A,B), ef(1,B), gf(2,B,C), ef(2,C)
     */
    fun <T> interleave(list: Array<T>, ef: (Int,T) -> Unit, gf: (Int,T,T) -> Unit) {
        val iter: Iterator<IndexedValue<T>> = list.withIndex().iterator()

        tailrec fun interR(current: IndexedValue<T>) {
            ef(current.index, current.value)
            if (iter.hasNext()) {
                val next : IndexedValue<T> = iter.next()
                gf(next.index, current.value, next.value)
                interR(next)
            }
        }

        if (iter.hasNext()) {
            val current : IndexedValue<T> = iter.next()
            interR(current)
        }
    }

    /**
     * Return the value from the map corresponding to the given key.
     * If not found, create a new value using the producer, and put that in the map instead,
     * and return the new value, not the old value.
     */
    fun <K,V> mapFetch(map: MutableMap<K,V>, key: K, producer: (K) -> V): V = map[key]
        ?: producer(key) . apply { map.put(key, this) }

}