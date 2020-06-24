package com.jsn.play

import org.junit.Test

import org.junit.Assert.*
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    val stack=Stack<Int>()

    fun findOrder(numCourses: Int, prerequisites: Array<IntArray>): IntArray {

        repeat(numCourses){ node ->
            val find = prerequisites.find { it.first() == node }
            if(find==null) prerequisites.plus(IntArray(node))
        }

        prerequisites.forEach {
            val toList = it.toList()
            val node=toList.first()
            val denpendencies=toList.subList(1,toList.size)
            dealWithNode(node,denpendencies,prerequisites,numCourses)
        }

        repeat(numCourses){ node ->
            val toList=prerequisites.find {
               it.first()==node
            }?.toList()
            if(toList==null) return@repeat
            val denpendencies=toList.subList(1,toList.size)
            dealWithNode(node,denpendencies,prerequisites,numCourses)
        }
        return stack.toList().toIntArray()
    }

    private fun dealWithNode(
        node: Int,
        denpendencies: List<Int>,
        prerequisites: Array<IntArray>,
        numCourses: Int
    ) {
        if(stack.contains(node)) return

        if(stack.size>=numCourses) return

        if(denpendencies.isEmpty()){
            stack.push(node)
            return
        }
        denpendencies.forEach{ denpendency ->
            val find=prerequisites.find {
                it[0]==denpendency
            }
            if(find==null){stack.push(denpendency); return@forEach}

            dealWithNode(
                denpendency,
                find!!.toList().subList(1,find!!.toList().size),
                prerequisites,
                numCourses)

        }
        stack.push(node)
    }

    @Test
    fun testFindOrder(){
        val result = findOrder(1, emptyArray())
        assertEquals(result, arrayOf(0))
    }
}

