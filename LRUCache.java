class LRUCache {
    class Node {
        int key;
        int value;
        Node next;
        Node pre;
        
        public Node(int key, int value){
            this.key = key;
            this.value=value;
            next=pre=null;
        }
        public void toStr(){
            System.out.println(key+" "+value);
        }
        
    }
    
    private int capacity;
    private int size;
    private Node head;
    private Node tail;
    private HashMap<Integer,Node> map;

    public LRUCache(int capacity) {
        this.capacity=capacity;
        size=0;
        head = new Node(0,0);
        tail = new Node(0,0);
        head.next = tail;
        tail.pre = head;
        map = new HashMap();
    }
    
    private void addToHead(Node node){
     
       node.next = head.next;
    
       head.next.pre = node;
   
       head.next = node;
    
       node.pre=head;
      
    }
    
    private void deleteNode(Node node){
        node.pre.next = node.next;
        node.next.pre = node.pre;
    }
    
    public int get(int key) {
        if(map.containsKey(key)){
            Node node = map.get(key);
            deleteNode(node);
            addToHead(node);
            return node.value;
        }
        return -1;
    }
    
    public void put(int key, int value) {
        if(map.containsKey(key)){
             Node node = map.get(key);
             node.value = value;
             deleteNode(node);
             addToHead(node);
        }
        else {
            Node node = new Node(key,value);
            addToHead(node);
            map.put(key,node);
            if(size<capacity){
                size++;
            }
            else{
                map.remove(tail.pre.key);
                deleteNode(tail.pre);
            }
        }
    }
}

/**
 * Your LRUCache object will be instantiated and called as such:
 * LRUCache obj = new LRUCache(capacity);
 * int param_1 = obj.get(key);
 * obj.put(key,value);
 */
