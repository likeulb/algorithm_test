package stanfordPart2;


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class ClusterBits {
	int[] allNodes;
	int numCluster;
	final int max = (int) Math.pow(2,24);
	

	//read all lines of string from text file. represent each node as integer
	//eliminate those duplicates, and those not exist in the input
	public ClusterBits(String path){
		allNodes = new int[max];
		numCluster=0;
		int k=0;//number of integers to read
		try{
			BufferedReader buffer = new BufferedReader(new FileReader(path));
			String line = "";
			while((line=buffer.readLine())!=null&&k<200000){
				String[] value = line.split(" ");
				//System.out.println(line);
				int curNode = 0;
				for(int i=0;i<value.length;i++){
					curNode = (curNode<<1)+Integer.valueOf(value[i]);
				}
				if(allNodes[curNode]==0){ //0 means this node has not been checked
				allNodes[curNode]=-2;//-2 means this index exists in the input string
				numCluster++;
				}
				k++;
			}
			System.out.println("There are totally "+numCluster+" different nodes from input");
			buffer.close();
		}catch(Exception ex){
			System.out.println("issue with files");
		}
		
	}
	
	//use bfs to check the distance between different nodes, and add them to the same group
	//if the distance is less than 3
	public int bfs2(){
		HashSet<Integer> xoWith = new HashSet<Integer>();
		//xoWith is the set for all the possible integer x that i^x=j 
		//if j is in the input array, j can be comined in the same group of i
		for(int i=0;i<24;i++){
		//for(int i=0;i<4;i++){
			int tmp = 1<<i;
			xoWith.add(tmp);
			for(int j=i-1;j>=0;j--){
				xoWith.add(tmp+(1<<j));
			}
		}
		//bfs
		Queue<Integer> que = new LinkedList<Integer>();
		int i=0;
		while(i<max&&allNodes[i]==0){
			i++;
		}
		if(i<max){
			que.add(i);
			allNodes[i]=0;//find the first valid node, 0 means this value has been checked
		}
		else{
			return -1111;//return some impossible number，just for debugging
		}
		
		while(!que.isEmpty()){
			int cur = que.poll();
			for(int j:xoWith){//only need to check every integer with integers in xoWith
				int validNode = cur^j;
				if(allNodes[validNode]==0) continue;
				que.add(validNode);
				allNodes[validNode]=0;
				numCluster--;
			}
			if(que.isEmpty()){
				while(i<max&&allNodes[i]==0){
						i++;
				}
				if(i<max){
					que.add(i);
					allNodes[i]=0;
				}
				else{
					return numCluster;
				}
				System.out.println("the number of cluster after BFS of one connected component is "+numCluster);
			}
				
		}
		return numCluster;
		
	}
	
	
	
	/*public int[] find(int[] iAndcount){
		//System.out.println(allNodes.get(iAndcount[0]));
		if(allNodes.get(iAndcount[0])<0) return iAndcount;
		iAndcount[1]++;
		iAndcount[0]=allNodes.get(iAndcount[0]);
		return find(iAndcount); 
		//if(allNodes.get(i)==-1) return i;
		//return find(allNodes.get(i));
		
	}*/
	
	
	/*public boolean union(int i, int j){
		int[] ifind = {i,0};
		int[] jfind = {j,0};
		int[] ihead = find(ifind);
		int[] jhead = find(jfind);
		System.out.println(i+ " lead is "+ihead[0]+" "+j+"  leader is "+jhead[0]);
		if(ihead[0]==jhead[0]) return false;
		if(ihead[1]<=jhead[1]) {
			allNodes.put(ihead[0],jhead[0]);
			System.out.println("set "+ihead[0]+" with head "+jhead[0]);
			//System.out.println(allNodes.get(ihead[0]));
		}
		else {
			allNodes.put(jhead[0],ihead[0]);
			//System.out.println("set "+jhead[0]+" with head "+ihead[0]);
		}
		return true;
		
		//allNodes.put(allNodes.get(i),j);
	}*/
	
	/*public int cluster() throws EmptyHeapException{
		BinaryHeap heap = BinaryHeap.buildHeap(validPairs); 
		
		while(((bitsPair)heap.getMin()).getDis()<=2){
			bitsPair curPair = (bitsPair) heap.deleteMin();
			int i=curPair.getFirst();
			int j=curPair.getSecond();
			if(union(i,j)) {
				numCluster--;
				System.out.println(numCluster);
			}
			
		}
		return numCluster;
		
	}*/
	/*public int HammingDis(int i, int j){
		int tmp = i^j;
		int count = 0;
		while(tmp>0){
			tmp = tmp&(tmp-1);
			count++;
		}
		return count;
	}
	*/
	public static void main(String[] args) throws EmptyHeapException{
		ClusterBits test = new ClusterBits("/../clustering_big.txt");
		System.out.println(test.bfs2());
	}
}
