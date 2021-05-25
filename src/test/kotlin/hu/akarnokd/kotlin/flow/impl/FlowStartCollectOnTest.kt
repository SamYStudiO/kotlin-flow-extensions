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

import hu.akarnokd.kotlin.flow.assertResult
import hu.akarnokd.kotlin.flow.startCollectOn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.runBlocking
import org.junit.Test

@FlowPreview
class FlowStartCollectOnTest {
    @Test
    fun basic() = runBlocking {

        arrayOf(1, 2, 3, 4, 5)
                .asFlow()
                .startCollectOn(Dispatchers.IO)
                .assertResult(1, 2, 3, 4, 5)
    }
}