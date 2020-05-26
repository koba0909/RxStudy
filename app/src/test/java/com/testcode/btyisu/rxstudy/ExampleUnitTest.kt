package com.testcode.btyisu.rxstudy

import com.testcode.btyisu.rxstudy.example.AdaptOperator
import com.testcode.btyisu.rxstudy.example.RxOperator
import com.testcode.btyisu.rxstudy.example.Scheduler
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    val rxOperator = RxOperator()
    val adaptOperator = AdaptOperator()
    val scheduler = Scheduler()

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun flipExample() = scheduler.flipExample()

    @Test
    fun schedulerExample() = scheduler.newSchedulerExample()

    @Test
    fun testComputationScheduler() = scheduler.computationSchedulerExample()

//    @Test
//    fun testIoScheduler() = scheduler.ioSchedulerExample()

    @Test
    fun trampolineScheduler() = scheduler.trampolineScheduler()

    @Test
    fun singleScheduler() = scheduler.singleThreadScheduler()

    @Test
    fun testExecutorScheduler() = scheduler.executorSchedulerExample()

    @Test
    fun testCallbackHeaven() = scheduler.callBackHeavenByZip()
}
