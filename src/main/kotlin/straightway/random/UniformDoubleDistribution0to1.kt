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

import straightway.utils.getLong
import kotlin.math.pow

/**
 * A uniform random distribution of double precision floating point
 * values between 0 and 1 (inclusive).
 */
class UniformDoubleDistribution0to1(source: Iterator<Byte>)
    : RandomDistributionBase<Double>(source, BYTES_TO_USE) {

    private companion object {
        const val BYTES_TO_USE: Int = 7
        const val BITS_TO_USE = BYTES_TO_USE * java.lang.Byte.SIZE
        const val RANGE_MASK = (1L shl BITS_TO_USE) - 1L
        val FACTOR = 2.0.pow(-BITS_TO_USE)
    }

    override fun ByteArray.byteArrayConverter() = (getLong() and RANGE_MASK) * FACTOR
}
