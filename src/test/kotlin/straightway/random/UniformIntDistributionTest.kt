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
import straightway.utils.toByteArray

class UniformIntDistributionTest {

    private val test get() =
        Given {
            object {
                val source =
                        0x01020304.toByteArray() +
                        0x05060708.toByteArray()
                val sut = UniformIntDistribution(source.iterator())
            }
        }

    @Test
    fun `first number comes from random source`() =
            test when_ { sut.next() } then { expect(it.result is_ Equal to_ 0x01020304) }

    @Test
    fun `second number comes from random source`() =
            test while_ { sut.next() } when_ { sut.next() } then {
                expect(it.result is_ Equal to_ 0x05060708)
            }

    @Test
    fun `exception is thrown if not enough bytes available`() =
        Given { UniformIntDistribution(listOf<Byte>(1).iterator()) } when_ { next() } then {
            expect({ it.result } does Throw.exception)
        }

    @Test
    fun `hasNext returns false if source has no next element`() =
            Given { UniformIntDistribution(listOf<Byte>().iterator()) } when_ { hasNext() } then {
                expect(it.result is_ False)
            }

    @Test
    fun `can be used with LINQ like expressions`() =
            test when_ { sut.take(2) } then {
                expect(it.result is_ Equal to_ Values(0x01020304, 0x05060708))
            }
}