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

import org.junit.jupiter.api.Test
import straightway.testing.bdd.Given
import straightway.testing.flow.Equal
import straightway.testing.flow.False
import straightway.testing.flow.Throw
import straightway.testing.flow.Values
import straightway.testing.flow.does
import straightway.testing.flow.expect
import straightway.testing.flow.is_
import straightway.testing.flow.to_

class RandomDistributionBaseTest {

    private class Derived(source: Iterator<Byte>) : RandomDistributionBase<Int>(source, 2) {
        val converterCalls = mutableListOf<ByteArray>()
        override fun ByteArray.byteArrayConverter(): Int {
            converterCalls += this
            return 1
        }
    }

    private val test get() =
        Given {
            object {
                fun setSource(src: List<Byte>) {
                    sut = Derived(src.iterator())
                }
                lateinit var sut: Derived
                init {
                    setSource(listOf(1, 2, 3, 4, 5, 6, 7, 8))
                }
            }
        }

    @Test
    fun `first number comes from random source`() =
            test when_ { sut.next() } then {
                expect(sut.converterCalls.single() contentEquals byteArrayOf(1, 2))
            }

    @Test
    fun `second number comes from random source`() =
            test while_ { sut.next() } when_ { sut.next() } then {
                expect(sut.converterCalls.size is_ Equal to_ 2)
                expect(sut.converterCalls.last() contentEquals byteArrayOf(3, 4))
            }

    @Test
    fun `exception is thrown if not enough bytes available`() =
        test while_ { setSource(listOf(1)) } when_ { sut.next() } then {
            expect({ it.result } does Throw.exception)
        }

    @Test
    fun `hasNext returns false if source has no next element`() =
            test while_ { setSource(listOf()) } when_ { sut.hasNext() } then {
                expect(it.result is_ False)
            }

    @Test
    fun `hasNext returns false if source has not enough elements`() =
            test while_ { setSource(listOf(1)) } when_ { sut.hasNext() } then {
                expect(it.result is_ False)
            }

    @Test
    fun `next() after hasNext() returns previously checked elements`() =
            test while_ { sut.hasNext() } when_ { sut.next() } then {
                expect(sut.converterCalls.single() contentEquals byteArrayOf(1, 2))
            }

    @Test
    fun `next() after calling hasNext() twice returns previously checked elements`() =
            test while_ { sut.hasNext(); sut.hasNext() } when_ { sut.next() } then {
                expect(sut.converterCalls.single() contentEquals byteArrayOf(1, 2))
            }

    @Test
    fun `calling hasNext() twice returns false both times`() =
            test while_ {
                setSource(listOf(1))
                sut.hasNext()
            } when_ {
                sut.hasNext()
            } then {
                expect(it.result is_ False)
            }

    @Test
    fun `can be used with LINQ like expressions`() =
            test when_ { sut.take(2) } then {
                expect(it.result is_ Equal to_ Values(1, 1))
            }
}