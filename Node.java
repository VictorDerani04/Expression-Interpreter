public class Node {
  public String element;
  
  public Node leftChild;
  public Node rightChild;
  
  public Node (String object) {
    this (object, null, null);
  }
  
  public Node (String object, Node l, Node r) {
    element = object;
    leftChild = l;
    rightChild = r;
  }
}