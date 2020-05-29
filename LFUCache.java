class LFUCache {
     class Node {
        int key;
        int value;
        int freq; 
        Node next;
        Node pre;
        public Node(int key, int value,int freq){
            this.key = key;
            this.value=value;
            this.freq=freq;
            next=pre=null;
        }
        public  String toString(){
            return "Key: "+key+"Value:"+value+"Freq:"+freq;
        } 
        
    }
    class DDL{
        Node head;
        Node tail;
        int len;
        public DDL(){
            head = new Node(-1,-1,-1);
            tail = new Node(-1,-1,-1);
            head.next = tail;
            tail.pre = head;
            len = 0;
        }
        
        public void addToHead(Node node){
           // System.out.println("Adding to head:"+node.toString());
            node.next = head.next;   
            head.next.pre = node;
            head.next = node;
            node.pre=head;
            len++;
        }
        
        public void deleteNode(Node node){
           //System.out.println("Delete:"+node.toString());
           node.pre.next = node.next;
           node.next.pre = node.pre;
           len--; 
        }
        
    }
    
    private int capacity;
    private int size;
    private TreeMap<Integer,DDL> freq;
    private HashMap<Integer,Node> map;
    
    
    public LFUCache(int capacity) {
        this.capacity=capacity;
        size=0;
        freq = new TreeMap();
        map = new HashMap();
    }
    
    public int get(int key) {
        if(map.containsKey(key)){
            Node node = map.get(key);
            //System.out.println("Existing node called:"+node.toString());
            DDL ddl = freq.get(node.freq);
            ddl.deleteNode(node);
            if(ddl.len==0) freq.remove(node.freq);
            node.freq+=1;
            ddl = freq.computeIfAbsent(node.freq,k -> new DDL());
            ddl.addToHead(node);
            return node.value;
        }
        return -1;
    }
    
    public void put(int key, int value) {
        if(capacity==0) return;
        if(map.containsKey(key)){
            Node node = map.get(key);
            node.value = value;
            DDL ddl = freq.get(node.freq);
            ddl.deleteNode(node);
            if(ddl.len==0) freq.remove(node.freq);
            node.freq+=1;
            ddl = freq.computeIfAbsent(node.freq,k -> new DDL());
            ddl.addToHead(node);
        }
        else 
        {
            Node node = new Node(key,value,1);
            map.put(key,node);
            
            if(size<capacity){
                size++;
                DDL ddl = freq.computeIfAbsent(1,k -> new DDL());
                ddl.addToHead(node);
            }
            else {
                Integer lowest = freq.firstKey();
                //System.out.println("Lowest freq:"+lowest);
                DDL ddl = freq.get(lowest);
                map.remove(ddl.tail.pre.key);
                ddl.deleteNode(ddl.tail.pre);
                if(ddl.len==0 && lowest!=1 ) freq.remove(lowest);
                DDL freq_one = freq.computeIfAbsent(1,k -> new DDL());
                freq_one.addToHead(node);
            }
      }
        
    }
    
    
    
}


/**
 * Your LFUCache object will be instantiated and called as such:
 * LFUCache obj = new LFUCache(capacity);
 * int param_1 = obj.get(key);
 * obj.put(key,value);
 */
