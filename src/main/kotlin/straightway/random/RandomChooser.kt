/*
 * Copyright 2016 github.com/straightway
 *
 *  Licensed under the Apache License, Version 2.0 (the &quot;License&quot;);
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an &quot;AS IS&quot; BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package straightway.random

/**
 * Choose random items fro a given collection.
 */
class RandomChooser(val source: Iterator<Byte>) : Chooser {

    override fun <T> chooseFrom(collection: List<T>, itemsToChoose: Int) =
            collection.randomized(itemsToChoose).take(itemsToChoose)

    private fun <T> List<T>.randomized(itemsToRandomize: Int): List<T> {
        if (isEmpty() || itemsToRandomize <= 0) return this
        val indexToSwapWithFirstItem = randomInts.next() % size
        val swapped = swappedIndices(0, indexToSwapWithFirstItem)
        return listOf(swapped[0]) + swapped.slice(1 until size).randomized(itemsToRandomize - 1)
    }

    private fun <T> List<T>.swappedIndices(a: Int, b: Int): List<T> =
            if (a == b)
                this
            else slice(0 until a) +
                    listOf(this[b]) +
                    slice(a + 1 until b) +
                    listOf(this[a]) +
                    slice(b + 1 until size)

    private val randomInts = UniformUnsignedIntDistribution(source)
}
