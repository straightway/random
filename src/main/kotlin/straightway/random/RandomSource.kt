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

import java.util.Random

/**
 * A source for random numbers. At the same time, this source yields uniformly distributed
 * signed bytes.
 */
open class RandomSource(private val generator: Random) : RandomDistribution<Byte> {
    override fun hasNext() = true
    override fun next() = ByteArray(1).let { generator.nextBytes(it); it[0] }
}