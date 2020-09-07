/*
 * Copyright 2019-2020 David Karnok
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hu.akarnokd.kotlin.flow.impl

import hu.akarnokd.kotlin.flow.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import org.junit.Ignore
import org.junit.Test

@ExperimentalCoroutinesApi
@FlowPreview
class ParallelFlowConcatMapTest {
    @Test
    fun map() = runBlocking {
        withParallels(1) { execs ->
            arrayOf(1, 2, 3, 4, 5)
                    .asFlow()
                    .parallel(execs.size) { execs[it] }
                    .concatMap { arrayOf(it + 1).asFlow() }
                    .sequential()
                    .assertResult(2, 3, 4, 5, 6)
        }
    }

    @Test
    fun map2() = runBlocking {
        withParallels(2) { execs ->
            arrayOf(1, 2, 3, 4, 5)
                    .asFlow()
                    .parallel(execs.size) { execs[it] }
                    .concatMap { arrayOf(it + 1).asFlow() }
                    .sequential()
                    .assertResultSet(2, 3, 4, 5, 6)
        }
    }

    @Test
    fun mapError() = runBlocking {
        withParallels(1) { execs ->
            arrayOf(1, 0)
                    .asFlow()
                    .parallel(execs.size) { execs[it] }
                    .concatMap { arrayOf(it).asFlow().map { v -> 1 / v } }
                    .sequential()
                    .assertFailure(ArithmeticException::class.java, 1)
        }
    }

    @Test
    @Ignore("Parallel exceptions are still a mystery")
    fun mapError2() = runBlocking {
        withParallels(2) { execs ->
            arrayOf(1, 2, 0, 3, 4, 0)
                    .asFlow()
                    .parallel(execs.size) { execs[it] }
                    .concatMap { arrayOf(it).asFlow().map { v -> 1 / v } }
                    .sequential()
                    .assertError(ArithmeticException::class.java)
        }
    }
}