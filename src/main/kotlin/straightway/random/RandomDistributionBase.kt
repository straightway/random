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
 * Base implementation for a random distribution taking random bytes from a
 * random source and converting them to the desired target type.
 */
abstract class RandomDistributionBase<T>(
        private val source: Iterator<Byte>,
        private val numberOfBytesPerValue: Int)
: RandomDistribution<T> {

    override fun hasNext() = isBufferFilled || fillBuffer()

    override fun next() = getNextBytes().byteArrayConverter()

    abstract fun ByteArray.byteArrayConverter(): T

    private fun getNextBytes(): ByteArray {
        if (isBufferFilled) {
            isBufferFilled = false
            return buffer
        }

        val result = mutableListOf<Byte>()
        for (i in 1..numberOfBytesPerValue)
            result += source.next()

        return result.toByteArray()
    }

    private fun fillBuffer(): Boolean {
        for (i in 0 until numberOfBytesPerValue)
            isBufferFilled = fillNextBufferItemIfAvailableAt(i)

        return isBufferFilled
    }

    private fun fillNextBufferItemIfAvailableAt(i: Int) =
            if (source.hasNext()) {
                buffer[i] = source.next()
                true
            } else false

    private var isBufferFilled = false
    private val buffer = ByteArray(numberOfBytesPerValue)
}