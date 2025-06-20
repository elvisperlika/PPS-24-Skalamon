package it.unibo.skalamon.model.data

object Stacks:

  case class Stack[A](private val elements: List[A])

  object Stack:
    /** Create an empty stack.
      * @tparam A
      *   Type of elements in the stack
      * @return
      *   Stack with no elements
      */
    def empty[A]: Stack[A] = Stack(Nil)

    def apply[A](items: A*): Stack[A] = Stack(items.toList)

    extension [A](stack: Stack[A])
      /** Add element at the top of the struct.
        * @param elem
        *   Element to add
        * @return
        *   Stack with new element at the top
        */
      def push(elem: A): Stack[A] = Stack(elem :: stack.elements)

      /** Remove the element at the top of the structure.
        * @return
        *   Stack without the element at the top.
        */
      def pop: Option[Stack[A]] =
        stack.elements match
          case _ :: tail => Some(Stack(tail))
          case Nil       => None

      /** Get the element at the top of the structure, if its present.
        * @return
        *   Element if present, None otherwise
        */
      def peek: Option[A] =
        stack.elements.headOption

      /** Check if the stack has no elements.
        * @return
        *   True if there aren't elements in the stack, False otherwise
        */
      def isEmpty: Boolean = stack.elements.isEmpty

      /** Get the stack's size.
        * @return
        *   Size
        */
      def size: Int = stack.elements.size

      /** Convert the Stack in a List.
        * @return
        *   List with same elements of the Stack.
        */
      def toList: List[A] = stack.elements
