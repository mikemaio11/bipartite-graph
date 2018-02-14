package graphColor;
import java.util.*;
import java.io.*;

public class GraphMaker {
	//instantiating the adjacency list
	@SuppressWarnings("unchecked")
	private static LinkedList<Integer> graph[] = new LinkedList[0];

	/*This method is responsible for constructing the graph given the
	 * input files. It makes use of an adjacency list to store the graph.
	 * In order to be more efficient, the method determines whether or not
	 * the graph is two-colorable as it is built. If the graph is found to
	 * be two-colorable, a coloring of the graph is printed to the output
	 * file. Otherwise, the method uses a depth first search to find an
	 * odd cycle and prints that to the file as at least one of the reasons
	 * why the graph was not two colorable.
	 */
	@SuppressWarnings("unchecked")
	public static boolean buildGraph(String filename, File outputFile){
		String line = null;
		int lineCount = 1;
		int vertices;
		//an array to determine the colors of each vertex (default size of 4)
		String checkColor[] = new String[4];
		
		try {	
			FileReader fileReader = new FileReader(filename);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			PrintWriter writer = new PrintWriter(new FileWriter(outputFile));

			//reads each line from the input file
			while((line = bufferedReader.readLine()) != null){
				/*If reading the first line, set the size of the checkColor
				 * array and the graph to the number read+1. The +1 is used
				 * because the 0 index is not utilized in these arrays
				 */
				if(lineCount == 1){
					vertices = Integer.parseInt(line)+1;
					checkColor = new String[vertices];
					graph = new LinkedList[vertices];
					lineCount++;
				}
				else{
					String str[] = line.split(" ");
					int firstNum = Integer.parseInt(str[0]);
					int secondNum = Integer.parseInt(str[1]);
					
					/*If encountering the second line, a default color of
					 * red is set for the first number since no other colors
					 * are present. Both numbers read are added to the
					 * adjacency list.
					 */
					if(lineCount == 2){
						checkColor[firstNum] = "red";
						graph[firstNum] = new LinkedList();
						graph[firstNum].add(secondNum);
						graph[secondNum] = new LinkedList();
						graph[secondNum].add(firstNum);
						lineCount++;
					}
					else {
						/*Handling cases where either the first or second
						 * numbers are null and need their list in the
						 * adjacency list instantiated
						 */
						if(graph[firstNum] == null){
							graph[firstNum] = new LinkedList();
							if(graph[secondNum] == null)
								graph[secondNum] = new LinkedList();
							graph[firstNum].add(secondNum);
							graph[secondNum].add(firstNum);
						}
						else{
							if(graph[secondNum] == null)
								graph[secondNum] = new LinkedList();
							graph[firstNum].add(secondNum);
							graph[secondNum].add(firstNum);
						}
					}
					//ignores the last empty line in the files
					if(line.equals(""))
						break;
					/*If neither numbers have colors yet, they are given
					 * default ones. This code runs when we access an
					 * area of the graph disconnected from the rest. No
					 * other colors will be present here so it's valid to
					 * hard code the first two.
					 */
					if(checkColor[firstNum] == null && checkColor[secondNum] == null){
						checkColor[firstNum] = "red";
						checkColor[secondNum] = "blue";
					}
					/*Setting colors of the numbers to the opposite
					 * of each other's color if possible.
					 */
					else if(checkColor[firstNum] == null)
						checkColor[firstNum] = oppositeColor(checkColor[secondNum]);
					else if(checkColor[secondNum] == null)
						checkColor[secondNum] = oppositeColor(checkColor[firstNum]);
					//colors are already opposite and thus we continue on
					else if(checkColor[secondNum].equals(oppositeColor(checkColor[firstNum])))
						continue;
					/*This code will run when every other option is exhausted.
					 * That leaves the only possibility to be that two vertices
					 * that are connnected have the same color, so the graph is
					 * not two-colorable. Proceeds to run a depth first search from
					 * the first number to the second to find an odd cycle which
					 * is responisble for the graph failing.
					 */
					else{
						Stack<Integer> oddCycle = DFS(graph,firstNum,secondNum);
						writer.write("No, the graph is not bipartite\nOdd cycle: ");
						writer.write(oddCycle.toString());
						writer.close();
						return false;
					}
				}
	        }
			/*If this code is reached, then the coloring of the graph is
			 * valid and written to the file.
			 */
			writer.write("Yes, the graph is bipartite\nValid coloring:\n");
			for(int i=1;i<checkColor.length;i++){
				if(checkColor[i]!=null){
					writer.write(i +" "+ checkColor[i]+"\n");
				}
			}
			bufferedReader.close();
			fileReader.close();
			writer.close();
			return true;
		}
		//catch errors with the file
		catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + filename + "'");
		}
		catch(IOException ex) {
            System.out.println("Error reading file '" + filename + "'");                  
        }
		
		/*A default return. This will not be reached unless there's a problem
		 * with the file.
		 */
		return true;
	}
	
	/*An iterative depth first search to find a cycle > length 1
	 * containing the last two numbers read when the two-colorable
	 * status failed. Once an odd cycle is found, the stack is
	 * returned, containing the path.
	 */
	public static Stack<Integer> DFS(LinkedList<Integer>[] graph, int from, int to){
		boolean visited[] = new boolean[graph.length];
		Stack<Integer> stack = new Stack<Integer>();
		
		stack.push(from);
		visited[from] = true;
		
		while(!stack.isEmpty()){
			int num = stack.peek();
			for(int i=0;i<graph[num].size();i++){
				int curr = graph[num].get(i);
				//this prevents finding the single edge path connecting "from" and "to"
				if(!visited[curr] && curr!=to){
					stack.push(curr);
					visited[curr] = true;
					/*Stops when reaching a neighbor of "to". This indicates that
					 * the cycle can be completed and "to" is manually pushed onto
					 * the stack. The second condition of the if statement ensures
					 * the cycle is of odd length (the +1 takes the additional push
					 * to the stack into consideration).
					 */
					if(isNeighbor(curr,to) && (stack.size()+1)%2!=0){
						stack.push(to);
						return stack;
					}
					break;
				}
				stack.pop();
			}
		}
	return stack;
	}
	
	//determines if two vertices are adjacent
	public static boolean isNeighbor(int num1, int num2){
		if(graph[num1].contains(num2))
			return true;
		return false;
	}
	
	//simple method to set colors opposite when coloring the graph
	public static String oppositeColor(String color){
		if(color.equals("red"))
			return "blue";
		else if(color.equals("blue"))
			return "red";
		else
			throw new IllegalArgumentException("Color was neither red nor blue");
	}
}