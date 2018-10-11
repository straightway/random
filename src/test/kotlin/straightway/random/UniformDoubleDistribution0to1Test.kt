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
import straightway.testing.flow.expect
import straightway.testing.flow.is_
import straightway.testing.flow.to_
import straightway.utils.toByteArray

class UniformDoubleDistribution0to1Test {

    @Test
    fun `random stream of 0 yields 0`() =
            Given {
                UniformDoubleDistribution0to1(randomStream(0))
            } when_ {
                next()
            } then {
                expect(it.result is_ Equal to_ 0.0)
            }

    @Test
    fun `random stream of 0xff yields 1`() =
            Given {
                UniformDoubleDistribution0to1(randomStream(-1L))
            } when_ {
                next()
            } then {
                expect(it.result is_ Equal to_ 1.0)
            }

    @Test
    fun `random stream of last bit 0 and all others 0xff yields 0,5`() =
            Given {
                UniformDoubleDistribution0to1(randomStream(0x7f_ffff_ffff_ffffL))
            } when_ {
                next()
            } then {
                expect(it.result is_ Equal to_ 0.5)
            }

    @Test
    fun `random stream of last twi bits 0 and all others 0xff yields 0,25`() =
            Given {
                UniformDoubleDistribution0to1(randomStream(0x3f_ffff_ffff_ffffL))
            } when_ {
                next()
            } then {
                expect(it.result is_ Equal to_ 0.25)
            }

    private fun randomStream(bits: Long) = bits.toByteArray().drop(1).iterator()
}