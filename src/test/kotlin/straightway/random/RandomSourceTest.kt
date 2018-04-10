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

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.jupiter.api.Test
import straightway.testing.bdd.Given
import straightway.testing.flow.Equal
import straightway.testing.flow.True
import straightway.testing.flow.Values
import straightway.testing.flow.expect
import straightway.testing.flow.is_
import straightway.testing.flow.to_
import java.util.Random

class RandomSourceTest {

    private val test get() = Given {
        object {
            private val numbers = byteArrayOf(1, 2, 3, 4, 5)
            private var currNumber = 0
            val random = mock<Random> {
                on { nextBytes(any()) }.thenAnswer {
                    val outBuffer = it.arguments[0] as ByteArray
                    outBuffer.indices.forEach {
                        outBuffer[it] = numbers[currNumber]
                        currNumber = (currNumber + 1) % numbers.size
                    }
                }
            }
            val sut = RandomSource(random)
        }
    }

    @Test
    fun `hasNext is true`() =
            test when_ { sut.hasNext() } then { expect(it.result is_ True) }

    @Test
    fun `next gets next byte from generator specified in construction`() =
            test when_ { sut.next() } then {
                verify(random).nextBytes(any())
            }

    @Test
    fun `can be used with LINQ like expressions`() =
            test when_ { sut.take(2) } then {
                expect(it.result is_ Equal to_ Values(1.toByte(), 2.toByte()))
            }
}