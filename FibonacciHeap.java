

public class FibonacciHeap
{
	public static int links;
	public static int cuts;
	public int Trees;
	public int size;
	public int mark_number;
	public HeapNode min;
	
	public FibonacciHeap(HeapNode min) 
	{
		this.min=min;
	}
	public FibonacciHeap()  //constructor (empty heap)
	{ 
	}

	public boolean isEmpty()
    {
    	if (this.min==null)
    	{
    		return true;
    	}
    	return false;
    }
	
	public HeapNode insert(int i) 
    {
	    	HeapNode output = new HeapNode(i);
	    	insert_helper(output);
	    	this.Trees++;
	    	this.size++;
	    	return output;
	}
	
   public void deleteMin()
   {
			if(this.isEmpty())
				return;
		    Trees--;
		   	size--;
		    	
		   	if(size()==0) 
		   	{
		   		setMin(null); 
	    		return;
	    	}
		   	if(Trees!=0) 
		   	{ 
		            Trees=Trees+min.rank;
			    	HeapNode deletedNode = min;
			    	setMin(min.next); 
			    	deletedNode.getNext().setPrev(deletedNode.getPrev());
			        deletedNode.getPrev().setNext(deletedNode.getNext());
			    	FibonacciHeap some_heap = new FibonacciHeap(update_our_father(deletedNode.child)); 
			    	this.meld(some_heap);
		    		
		    
	        }
		    else 
		    	{
		    		Trees+=min.rank;
		    		setMin(update_our_father(min.getChild()));
			  
		    	}
		   	successive_linking();
		   	unmark();
	}
	   
  public HeapNode findMin()
  {
      
	  return min;
  } 
  
  
  public void meld (FibonacciHeap heap2)
  {
  	if(heap2==null)
  		return;
  	if(heap2.min==null && heap2.size()==0) 
  	{
  		return;
  	}
  	if(size==0) 
  	{ 
  		setMin(heap2.min);
  		mark_number+=heap2.mark_number;
      	Trees+=heap2.Trees;
      	size+=heap2.size();
      	return;
  	}
  	mark_number+=heap2.mark_number;
  	size+=heap2.size();
  	Trees+=heap2.Trees;
  	HeapNode temp1left = min.prev;
  	HeapNode temp1right = min;
  	HeapNode temp2left = heap2.min.prev;
  	HeapNode temp2right = heap2.min;	
  	if(heap2.min.getKey()<min.getKey()) 
  		setMin(heap2.min); 
  	temp1left.setNext(temp2right);
  	temp2right.setPrev(temp1left);
  	temp1right.setPrev(temp2left);
  	temp2left.setNext(temp1right); 		
  }

  public int size()
  {
  	return size;
  } 
  
  
  public int[] countersRep()
  {
  	if (this.min==null)
  	{
  		int[] emptyarray = new int[0];
  		return emptyarray;
  	}
  	HeapNode temp = this.min;
  	int maxdegree=temp.getRank();
  	temp=temp.getNext();
  	while(temp!=this.min)
  	{
  		if (temp.getRank()>maxdegree)
  		{
  			maxdegree=temp.getRank();
  		}
  		temp=temp.getNext();
  	}
  	
  	temp=this.min;
  	int[] arr = new int[maxdegree+1];
  	arr[temp.getRank()]+=1;
  	temp=temp.getNext();
  	while(temp!=this.min)
  	{
  		arr[temp.getRank()]+=1;
  		temp=temp.getNext();
  	}
  	return arr;
  }
    

    public void delete(HeapNode x) 
    {   
    	int delta=x.getKey()+1;
		decreaseKey(x,delta);
		deleteMin();
    }


    public void decreaseKey(HeapNode x, int delta)
    {    
    	x.setKey(x.getKey()-delta);
    	if(x.getParent()==null)
    	{
    		if(x.getKey()<min.getKey())
    			setMin(x);
    		return;
    	}
    	if(x.getKey()>x.getParent().getKey())
    		return;
        cascading_cuts(x);
    }
    
    
    public int potential() 
    {
    	if (this.min==null)
    	{
    		return 0;
    	}
    	int potential = this.Trees+2*this.mark_number;
    	return potential;
    }
    
    
    public static int totalLinks()
    {    
    	return links;
    }

    
    public static int totalCuts()
    {    
    	return cuts;
    }
        
    public static int[] kMin(FibonacciHeap H, int k)
    {    
        if(k==0 || H.min==null)
        {
        	int[] emptyarray=new int[0];
        	return emptyarray;
        }
        else if (k==1)
        {
        	int root = H.min.getKey();
        	int[] array = new int[] {root};
        	return array;
        }
        int[] arr = new int[k];
        HeapNode tmp = H.min;
        arr[0]=tmp.getKey();
        tmp=tmp.getChild();
        int keyOfFirstChild=tmp.getKey();
        FibonacciHeap helperHeap=new FibonacciHeap(); 
        
        for(int i=0;i<k-1;i++)
        {
        	while(tmp!=null)
        	{	
        		int key = tmp.getKey();
        		HeapNode Node = helperHeap.insert(key);
        		Node.realHeap=tmp;
        		tmp=tmp.getNext();
        		if(tmp.getKey()==keyOfFirstChild)
        		{
        			break;
        		}
        	}
        	HeapNode min = helperHeap.findMin();
        	arr[i+1]=min.getKey();
        	tmp=min.realHeap;
        	tmp=tmp.getChild();
        	if(tmp!=null)
        	{
        	keyOfFirstChild=tmp.getKey();
        	}
        	helperHeap.deleteMin();
        }
        return arr;
        
    }
    
    /////////////// helpers
    
    public void insert_helper(HeapNode h)
	{    
	    	if(Trees==0) 
	    	{		
	    		setMin(h);
	    		h.setNext(h);
	    		h.setPrev(h);
	    		return;
	    	}
	    	HeapNode temp = min.getPrev();
	    	h.setNext(min);
	    	h.setPrev(temp);
	       	temp.setNext(h);
	       	min.setPrev(h);
	    	if(min.getKey()>h.getKey())
	    		setMin(h);
	}
    
    public void setMin(HeapNode m)
    {
  		this.min = m;
    }
  		
    
    public void successive_linking()
    {
     	if(this.size()<2)
     		return;
     	HeapNode[] pails = new HeapNode[degrees_max()];
     	int number_of_roots=Trees;
     	HeapNode prev, in,curr = min;
     	
     	for(int i=0; i<number_of_roots; i++) 
     	{
     		if(curr.getKey()<min.getKey()) setMin(curr);
     		curr=curr.next;
     	}
     	curr=min.prev;
     	for(int i=0; i<number_of_roots; i++) 
     	{
     		if(curr.getKey()<min.getKey()) 
     			setMin(curr);
     		prev=curr.prev;
     		while(pails[curr.rank]!=null) 
     		{
     			in=pails[curr.rank];
     			pails[curr.rank]=null;
     			curr=one_link(curr, in);
     		}
     		pails[curr.rank]=curr;
     		curr=prev;
     	}
     }
  
     
     public HeapNode one_link(HeapNode A, HeapNode B) 
     {
     	HeapNode maxNode, minNode;
     	
     	if(A.getKey()>B.getKey()||((A.getKey()==B.getKey())&&B==this.min)) 
     	{
     		minNode=B;
     		maxNode=A;
     		
     	}
     	else 
     	{
     		minNode=A;
     		maxNode=B;
     		
     	}
     	
     	maxNode.getPrev().setNext(maxNode.getNext());
     	maxNode.getNext().setPrev(maxNode.getPrev());
     	minNode.increaseRank();
     	maxNode.setParent(minNode);
     	
     	if(minNode.rank!=1)
     	{ 
     	
     		maxNode.setNext(minNode.getChild());
     		maxNode.setPrev(minNode.getChild().getPrev());
     		maxNode.getNext().setPrev(maxNode);
     		maxNode.getPrev().setNext(maxNode);
     		minNode.setChild(maxNode);
     		
     	}
     	else 
     	{
     		minNode.setChild(maxNode);
     		maxNode.setNext(maxNode);
     		maxNode.setPrev(maxNode);
     		
     	}
     	
     	links++;
     	Trees--;
    
     	return minNode;
     	
     }

     
     public int degrees_max() 
     {
     	return (int)Math.ceil(1.5*Math.log(size));
     }
    
     
     public void unmark()
     {
     	if(this.isEmpty())return;
     	HeapNode temp =min;
     	for(int i=0; i<Trees; i++)
     	{
     		if(temp.isMarked()) 
     		{
     			temp.unmark();
     			mark_number--;
     		}
     		temp=temp.getNext();
     	}
     }
     
     public HeapNode update_our_father(HeapNode b)
     {
     	if(b==null) 
     		return null; 
     	HeapNode output = b;
     	while(b.getParent()!=null) 
     	{
     		b.setParent(null);
     		
     		if(b.getKey()<output.getKey()) 
     			output=b;
     		b=b.getNext();
     	}
     	return output;
     }
    
    
    public void cascading_cuts(HeapNode node)
    {
    	HeapNode parent = node.getParent();
    	single_cut(node);
    	if(parent.getParent()!=null) 
    	{
    		if(!parent.isMarked()) 
    		{
    			parent.mark();
    			mark_number++;
    		}
    		else 
    			cascading_cuts(parent);
    	}
    }
   
    
    public void single_cut(HeapNode node) 
    {
    	HeapNode parent = node.getParent();
    	node.setParent(null);
    	if(node.isMarked()) {
        	node.unmark();
        	mark_number--;
    	}
    	parent.setRank(parent.getRank()-1);
    	if(node.getNext()==node) 
    		parent.setChild(null);
    	else 
    	{
    		parent.setChild(node.getNext());
    		node.getNext().setPrev(node.getPrev());
        	node.getPrev().setNext(node.getNext());
    	}
    	insert_helper(node);
    	cuts++;
    	Trees++;
    	
    }
 
  
  ////////////////////////////////////////
    
    
  
    public class HeapNode
    {
		public HeapNode realHeap;
    	public String value;
    	public Boolean mark;
		public HeapNode child;
		public HeapNode next;
		public HeapNode prev;
		public HeapNode parent;
		public int key;
		public int rank;
		
		
		public HeapNode(int key) 
		{
			this.prev=this;
			this.mark=false;
		    this.key = key;
		    this.next=this;
			
	    }
		
	  	public int getKey() 
	  	{
		    return this.key;
		}
		public HeapNode(String info,int key)  // second constructor
		{
			this.next=this;
			this.prev=this;
			this.key=key;
			this.value=info;
			this.mark=false;
		}
		public int getRank() 
		{
			return rank;
		}
	  	
	  	public void setRank(int rank) 
	  	{
			this.rank = rank;
		}
	  	
	  	public void setKey(int key) 
	  	{
	  		this.key=key;
	  	}
	  	 
	  	public Boolean isMarked() 
	  	{
		    return this.mark;
	    }
	  	 
	  	public void increaseRank() 
	  	{
			this.rank++;
		}
	  	
	  	public void mark() 
	  	{
		    this.mark=true;
	    }
	  	
	  	public void unmark()
	  	{
		    this.mark=false;
	    }
	  	 
	  	public void setPrev(HeapNode prev)
	  	{
		    this.prev=prev;
	    }
	  	public HeapNode getParent() 
	  	{
				return parent;
		}
		
		public HeapNode getChild() 
		{
				return child;
		}
		  
	    public HeapNode getNext() 
	    {
				return next;
		}
		  	
        public HeapNode getPrev() 
        {
				return prev;
		}
         
        public void setNext(HeapNode next) 
 		{
 			this.next = next;
 		}
 	  	 
 	  	public void setParent(HeapNode parent)
 	  	{
 			this.parent = parent;
 		}
	  
	  	 
	  	public void setChild(HeapNode child)
	  	{
			this.child = child;
		}
		
	  
	

    }
}
