   /**
     * Runs a TSP Approximation Algorithm utilizing depth-first-search on a Minimum 
     * Spanning Tree
     */
    public void MSTalgorithm(){
    	 shortestFromMST = Double.MAX_VALUE;
    	
    	//begin Kruksal's MST algorithm
		Node[][] set = makeSet(AdjMatrix);
		Edge[] edges = sortEdges(AdjMatrix);
		ArrayList<Edge> pick = new ArrayList<Edge>();
		
		//find all edges in the MST
		int i = 0;
		while(i < edges.length){
			if(findSet(edges[i].vertex1, set) != findSet(edges[i].vertex2, set)){
				pick.add(edges[i]);
				setUnion(set, edges[i].vertex1, edges[i].vertex2);
				if(pick.size() == AdjMatrix.length - 1){
					break;
				}
			}
			i++;
		}
		// Create MSTGraph which identifies Edges in MST with a T value
		boolean MSTGraph[][] = new boolean[pick.size()+1][pick.size()+1];
		int v1;
		int v2;
		
		for(int j = 0 ; j < pick.size() ; j++){
			v1 = pick.get(j).vertex1;
			v2 = pick.get(j).vertex2;
			MSTGraph[v1][v2] = true;
			MSTGraph[v2][v1] = true;
		}
		
		// Run depth first search from each vertex in the MST and pick the lowest distance
		for(int z = 0 ; z < AdjMatrix.length ; z++){
			
				double ans = 0 ;
			
				v1 = z;
				//sequence determines the order vertices are traversed in the MST 
				//^ sequene is determined from depth-first-search.
				ArrayList<Integer> sequence = new ArrayList<Integer>();
				boolean[] visited = new boolean[pick.size()+1];
				dfs(visited, MSTGraph, v1, sequence);
		
				//add the edge weights
		
				for(i = 0 ; i < sequence.size() - 1 ; i++){
					ans += AdjMatrix[sequence.get(i)][sequence.get(i+1)];
				}
				// add edge weight from last vertex to first vertex since we are dealing
				// with hamiltonian circuits/cycles
				ans += AdjMatrix[sequence.get(0)][sequence.get(sequence.size()-1)];
				// update shortestFromMST if ans is the least found so far
				if(ans < shortestFromMST){
					shortestFromMST = ans;
					pathFromMST = sequence;
					pathFromMST.add(sequence.get(0));
				}
		}
		
		
    }
    
 
    /**
     * Makes Sets, which are connected to each other in the picking of edges. 
     * Crucial to the formation of the Minimum Spanning Tree.
     * @param matrix
     * @return
     */
    
    private Node[][] makeSet(double[][] matrix){
		Node[][] set = new Node[matrix.length][1];
		for(int i = 0 ; i < set.length ; i++){
			set[i][0] = new Node(i, null, null );
			set[i][0].prev = set[i][0];
		}
		
		return set;
	}
    
   /**
    * Find the set to which a vertex belongs to.
    * Determines the acceptance or rejection of an edge in the Minimum Spanning Tree.
    * @param vertex
    * @param set
    * @return
    */
	private Node findSet(int vertex, Node[][] set){
		return set[vertex][0].prev;
	}
	
	/**
	 * Conjoin the sets vertex1 and vertex2 belong to into one set.
	 * @param set
	 * @param vertex1
	 * @param vertex2
	 */
	private void setUnion(Node[][] set, int vertex1, int vertex2){
		if(set[vertex1][0].prev.length <= set[vertex2][0].prev.length){
			//adjust length of larger set
			set[vertex2][0].prev.length += set[vertex1][0].prev.length;
			
			//unify the two linked lists
			set[vertex2][0].prev.end.next = set[vertex1][0].prev;
			set[vertex2][0].prev.end = set[vertex1][0].prev.end;
			
			//change the prev pointer to the head of the larger set
			for(Node ptr = set[vertex1][0].prev ; ptr != null ; ptr = ptr.next){
				ptr.prev = set[vertex2][0].prev;
			}
			
		
		}else{
			//same format as above
		
			set[vertex1][0].prev.length += set[vertex2][0].prev.length;
		
			set[vertex1][0].prev.end.next = set[vertex2][0].prev;
			set[vertex1][0].prev.end = set[vertex2][0].prev.end;
		
			for(Node ptr = set[vertex2][0].prev ; ptr != null ; ptr = ptr.next){
				ptr.prev = set[vertex1][0].prev;
			}
		}
		
	}
	
	/**
	 * Sort Edges from least to greatest. 
	 * @param matrix
	 * @return
	 */
	private Edge[] sortEdges(double[][] matrix){
		Edge[] edges = new Edge[((matrix.length*matrix.length) - matrix.length)/2];
		int count = 0;
		for( int i = 0 ; i < matrix.length ; i++){
			for( int j = 0 ; j < i ; j++){
				edges[count] = new Edge(matrix[i][j], i, j);
				count++;
			}
		}
		
		Quicksort.sort(edges);
		return edges;
	}
	/**
	 * Perform Depth First Search, which is an algorithm that goes as deep
	 * into the graph as can, and then backtracks, until it backtrackst to the starting point
	 * @param visited
	 * @param filled
	 * @param i
	 * @param seq
	 */
	private void dfs(boolean[] visited, boolean filled[][], int i, ArrayList<Integer> seq){
		visited[i] = true;
		seq.add(i);
		for(int j = 0 ; j < filled[i].length ; j++){
			if(filled[i][j] && !visited[j]){
				dfs(visited, filled, j, seq);
			}
		}
	}
