package it.unibo.skalamon.model.data

import it.unibo.skalamon.model.data.Stacks.Stack
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class StackTest extends AnyFlatSpec with should.Matchers:

  "Empty stack" should "not have elements" in:
    val empty: Stack[Int] = Stack.empty
    empty.toList shouldEqual Nil

  it should "return None if empty on pop" in:
    val empty: Stack[Int] = Stack.empty
    empty.pop shouldEqual None

  it should "return None if empty on peek" in:
    val empty: Stack[Int] = Stack.empty
    empty.peek shouldEqual None

  it should "return True if empty" in:
    val empty: Stack[Int] = Stack.empty
    empty.isEmpty shouldBe true

  "Stack" should "push new elements" in:
    var stack: Stack[Char] = Stack.empty
    stack = stack.push('a').push('b').push('c')
    stack.toList shouldEqual 'c' :: 'b' :: 'a' :: Nil

  it should "return False if not empty" in:
    var stack: Stack[Char] = Stack.empty
    stack = stack.push('a').push('b').push('c')
    stack.isEmpty shouldBe false
