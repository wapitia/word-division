package com.wapitia.games.worddivision.standard

object StandardCollectionUtility {

    /**
     * Produces elements from a list to be applied sequentially to a pair of consumer functions (onElement and onGap),
     * applied alternately for each element in the list, interleaving the onGap calls between the elements.
     *
     * If the list is empty nothing happens.
     * Otherwise, onElement is called once for each element in the list, onGap is called one less times for each element in the list.
     * If the list is [A,B,C],
     * then the calls will be performed in the sequence:
     * onElement(0,A), onGap(1,A,B), onElement(1,B), onGap(2,B,C), onElement(2,C)
     */
    fun <T> interleave(list: Sequence<T>, onElement: (Int, T) -> Unit, onGap: (Int, T, T) -> Unit) =
        superleave(list, onElement = onElement, onGap = onGap)

    /**
     * Calls consumer functions before, during, and after each element in a sequence, allowing
     * the user to perform interstitials, such as dashes between letterCache, whatever.
     * <p>
     * onElement is called once for each element in the seq, onGap is called one less times for each element in the seq.
     * If the seq is [A,B,C],
     * then the calls will be performed in the sequence:
     * onElement(0,A), onGap(1,A,B), onElement(1,B), onGap(2,B,C), onElement(2,C)
     *
     * @param seq sequence through which to iterate
     * @param onEmpty function called once only when `seq is initially empty, ignored otherwise.
     *                Consumer function takes no arguments.
     * @param onElement function called once for each element in the sequence.
     *                  `onGap is called once between each call to `onElement
     *                  Consumer function takes the element's 0-based index and the element itself.
     * @param onGap function called once between each pair of `onElement calls in the sequence.
     *              `onGap is called {@code onGap.size - 1 } times during execution.
     *              Consumer function takes the following element's 0-based index (so starts at 1),
     *              the preceding element, and the following element
     * @param beforeFirst function called once if there are elements in the sequence and before the first
     *                    `onElement call. `beforeFirst is not called if the sequence is empty.
     *                    Consumer function takes the first element as a parameter.
     * @param afterLast function called once if there are elements in the sequence and after the last
     *                    `onElement call. `afterLast is not called if the sequence is empty.
     *                    Consumer function takes the size of the sequence and the last element itself.
     */
    fun <T> superleave(seq: Sequence<T>,
                       onElement: (Int, T) -> Unit = { _, _ -> },
                       onGap: (Int, T, T) -> Unit = { _, _, _ -> },
                       beforeFirst: (T) -> Unit = { _ -> },
                       afterLast: (Int, T) -> Unit = { _, _ -> },
                       onEmpty: () -> Unit = {} )
    {
        val iter: Iterator<IndexedValue<T>> = seq.withIndex().iterator()

        tailrec fun interR(current: IndexedValue<T>) {
            onElement(current.index, current.value)
            if (!iter.hasNext()) {
                afterLast(current.index + 1, current.value)
                return
            }
            val next : IndexedValue<T> = iter.next()
            onGap(next.index, current.value, next.value)
            interR(next)
        }

        if (iter.hasNext()) {
            val current : IndexedValue<T> = iter.next()
            beforeFirst(current.value)
            interR(current)
        }
        else {
            onEmpty()
        }
    }

    /**
     * Builder class for the superleave call above
     */
    data class SuperleaveBuilder<T>(var onElement: (Int, T) -> Unit = { _, _ -> },
                                    var onGap: (Int, T, T) -> Unit = { _, _, _ -> },
                                    var beforeFirst: (T) -> Unit = { _ -> },
                                    var afterLast: (Int, T) -> Unit = { _, _ -> },
                                    var onEmpty: () -> Unit = {} )
    {
        fun onElement(onElement: (Int, T) -> Unit) = apply { this.onElement = onElement }
        fun onGap(onGap: (Int, T, T) -> Unit) = apply { this.onGap = onGap }
        fun beforeFirst(beforeFirst: (T) -> Unit) = apply { this.beforeFirst = beforeFirst }
        fun afterLast(afterLast: (Int, T) -> Unit) = apply { this.afterLast = afterLast }
        fun onEmpty(onEmpty: () -> Unit) = apply { this.onEmpty = onEmpty }

        fun build(seq: Sequence<T>) = superleave(seq, onElement, onGap, beforeFirst, afterLast, onEmpty)
    }

    /**
     * Return the value from the cache corresponding to the given key.
     * If not found, create a new value using the producer, returning the newly produced value,
     * and also storing that into the cache for the next fecth call.
     */
    fun <K,V> mapFetch(cache: MutableMap<K,V>, key: K, producer: (K) -> V): V =
        cache[key] ?: producer(key) . also { cache[key] = it }

}