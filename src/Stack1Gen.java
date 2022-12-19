// Stack1Gen.java
// The StackGen Interface is implemented using linked list
// The linked list used is a simple generic node class called NGen.  (See NGen.java)

public class Stack1Gen <T> implements StackGen<T> {

    // constructor

    public Stack1Gen () {}

    // selectors

    public void push(T o) {
        start = new NGen<T>(o, start);
    }

    public T pop() {
        if (start == null)
            throw new RuntimeException("Tried to pop an empty stack");
        else {
            T data = start.getData();
            start = start.getNext();
            return data;
        }
    }

    public NGen top() {
        if (start == null)
            return null;
        else return start;
    }

    public boolean isEmpty() {
        if (start == null)
            return true;
        else return false;
    }

    public int size(){
        int count = 0;
        if (start == null){
            return 0;
        } else {
            NGen temp = start;
            while (temp!= null){
                temp = temp.getNext();
                count++;
            }
            return count;
        }
    }

    public void reverse(NGen head) {
        NGen trailer = null;
        NGen tempNode = head;
        NGen ptr;

        while(tempNode != null){
            ptr = tempNode.getNext();
            tempNode.setNext(trailer);
            trailer = tempNode;
            tempNode = ptr;
        }
        start = trailer;
    }

    // instance variables

    private NGen<T> start = null;

}  // Stack1Gen class
