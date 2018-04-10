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
import straightway.testing.flow.Empty
import straightway.testing.flow.Equal
import straightway.testing.flow.Throw
import straightway.testing.flow.Values
import straightway.testing.flow.does
import straightway.testing.flow.expect
import straightway.testing.flow.is_
import straightway.testing.flow.to_
import straightway.utils.toByteArray

class RandomChooserTest {

    private val test get() =
        Given {
            object {
                var randomStream
                    get() = sut.source
                    set(stream: Iterator<Byte>) { sut = RandomChooser(stream) }
                var sut = RandomChooser(byteStreamOf(0, 1, 2, 3, 4, 5, 6))
            }
        }

    @Test
    fun `choice from an empty collection is empty`() =
            test when_ { sut.chooseFrom(listOf<Int>(), 1) } then {
                expect(it.result is_ Empty)
            }

    @Test
    fun `choice from single item is single item`() =
            test when_ { sut.chooseFrom(listOf(83), 1) } then {
                expect(it.result is_ Equal to_ Values(83))
            }

    @Test
    fun `choice from single item with overflowing random number`() =
            test while_ {
                this.randomStream = byteStreamOf(1000)
            } when_ {
                sut.chooseFrom(listOf(83), 1)
            } then {
                expect(it.result is_ Equal to_ Values(83))
            }

    @Test
    fun `choice of zero items is empty`() =
            test when_ { sut.chooseFrom(listOf(83), 0) } then {
                expect(it.result is_ Empty)
            }

    @Test
    fun `choosing from a two element list with swapped items due to random number`() =
            test while_ { randomStream = byteStreamOf(1) } when_ {
                sut.chooseFrom(listOf(1, 2), 1)
            } then {
                expect(it.result is_ Equal to_ Values(2))
            }

    @Test
    fun `choosing from a two element list with not swapped items due to random number`() =
            test while_ { randomStream = byteStreamOf(0) } when_ {
                sut.chooseFrom(listOf(1, 2), 1)
            } then {
                expect(it.result is_ Equal to_ Values(1))
            }

    @Test
    fun `choosing from a two element list with not enough numbers in the random stream`() =
            test while_ { randomStream = byteStreamOf() } when_ {
                sut.chooseFrom(listOf(1, 2), 1)
            } then {
                expect({ it.result } does Throw.exception)
            }

    @Test
    fun `choosing two item from three element list, where first item is not swapped`() =
            test while_ { randomStream = byteStreamOf(0, 1) } when_ {
                sut.chooseFrom(listOf(1, 2, 3), 2)
            } then {
                expect(it.result is_ Equal to_ Values(1, 3))
            }

    private companion object {
        fun byteStreamOf(vararg ints: Int) = byteArrayOf(*ints).iterator()
        fun byteArrayOf(vararg ints: Int): ByteArray =
                if (ints.isEmpty())
                    ByteArray(0)
                 else ints.first().toByteArray() + byteArrayOf(*ints.sliceArray(1 until ints.size))
    }
}