package com.jsn.play
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test
import java.util.*

class AlgorithmJvmTest{
    @Test
    fun lruCacheTest() {
        val cache=LRUCache(2)
        cache.put(1,1); cache.put(2,2); cache.get(1); cache.put(3,3); cache.get(2); cache.put(4,4);
        cache.get(1);cache.get(3);cache.get(4)
        assertThat(cache.get(4) ,`is` (4))
    }
}
class LRUCache(capacity: Int) {
    val capa=capacity
    var data= mutableMapOf<Int,Int>()
    var list= mutableListOf<Pair<Int,Int>>()
    fun get(key: Int): Int {
        if(!data.containsKey(key)) return -1
        val value =data.get(key)
        put(key,value!!)
        return value
    }
    fun put(key: Int, value: Int) {
        if(data.containsKey(key)) {
            val new =Pair(key,value)
            data.remove(key)
            data.plusAssign(Pair(key,value))
            val updated = list.filter {
                it.first == key
            }.first()
            list.remove(updated)
            list.add(new)
        }
        //not contain
        else if(list.size<capa){
            val new =Pair(key,value)
            data.plusAssign(Pair(key,value))
            list.add(new)
        }else{
            data.remove(list[0].first)
            list.removeAt(0)
            val new =Pair(key,value)
            data.plusAssign(Pair(key,value))
            list.add(new)
        }
    }

}


/*s = "3[a]2[bc]", 返回 "aaabcbc".
s = "3[a2[c]]", 返回 "accaccacc".
s = "2[abc]3[cd]ef", 返回 "abcabccdcdcdef".

你可以认为输入字符串总是有效的；输入字符串中没有额外的空格，且输入的方括号总是符合格式要求的。
此外，你可以认为原始数据不包含数字，所有的数字只表示重复的次数 k ，例如不会出现像 3a 或 2[4] 的输入。
*/
class Solution {
    @Test
    fun test(){
        val decoded = decodeString("3[a]2[bc]")
        assertThat(decoded, `is`("aaabcbc"))
        val decoded1 = decodeString("3[a2[c]]")
        assertThat(decoded1, `is`("accaccacc"))
        val decoded2 = decodeString("2[abc]3[cd]ef")
        assertThat(decoded2, `is`("abcabccdcdcdef"))
        val res=decodeString("100[leetcode]")
        assertThat(res, `is`("leetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcode"))
    }
    fun decodeString(s: String): String {
        val len=s.length
        //val build=StringBuilder()
        val stack=Stack<String>()
        var index=0
        while(index<len){
            if(!s[index].toString().equals("]") ) {
                stack.push(s[index].toString()) //push a single char
                index++
                continue
            } else{ //met a "]"
                stack.push("]")
                var distance = stack.search("[")
                val unit =StringBuilder() //2[aaa]
                val clone = stack.clone() as Stack<String>
                repeat(distance){
                    clone.pop()
                }
                var numCount=0
                while(!clone.isEmpty()&&isDigitsOnly(clone.pop())){
                    numCount++
                }
                repeat(distance+numCount){
                    unit.insert(0,stack.pop()) //12[aaa]
                }
                val unitResult=StringBuilder()
                repeat(unit.toString().substring(0,numCount).toInt()){
                    unitResult.append(unit.substring(numCount+1,unit.length-1))
                }
                unitResult.forEach {
                    stack.push(it.toString())
                }
            }
            index++
        }

        return StringBuilder().run {
            repeat(stack.size){ insert(0,stack.pop()) }
            toString()
        }
    }

    fun isDigitsOnly(str: CharSequence): Boolean {
        val len = str.length
        var cp: Int
        var i = 0
        while (i < len) {
            cp = Character.codePointAt(str, i)
            if (!Character.isDigit(cp)) {
                return false
            }
            i += Character.charCount(cp)
        }
        return true
    }
}


