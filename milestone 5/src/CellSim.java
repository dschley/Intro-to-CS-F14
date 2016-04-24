import java.awt.Color;



public class CellSim{


	public static void main(String[] args){
	
		//Create a n x n 2-D character array representing the tissue sample
		// You can pick n
		
		
		//Write your code to test your methods here
		System.out.println("Enter the size of the array (n*n): ");
		int n=IO.readInt();
		
		System.out.println("Enter the percentage of blank spaces: ");
		int percentBlank=IO.readInt();
		
		System.out.println("Enter the percentage of X's: ");
		int percentX=IO.readInt();
		
		System.out.println("Enter the percentage threshold of like neighbors to satisfy the cell: ");
		int threshold=IO.readInt();
		
		System.out.println("Enter the maximum number of rounds you would like moveAllUnsatisfied to preform: ");
		int maxRounds=IO.readInt();
		
		System.out.println("Enter the frequency you would like the board to be printed (every n rounds): ");
		int frequency=IO.readInt();

		int numOfMoves = 0;
		int totalX=0;
		int totalO=0;
		int size=300;
		char[][] tissue=new char[n][n];
		int p;
		
		if(n>25){
			p=1;
		}
		else{
			p=100;
		}
		
		CellSimGUI csg=new CellSimGUI(n,p);
		
		
		
		assignCellTypes(tissue,percentBlank,percentX);
		System.out.println("Original tissue:");
		printTissue(tissue);
		System.out.println("The board is: "+boardSatisfied(tissue,threshold));
		
		for(int i=0;i<tissue.length;i++){
			for(int j=0;j<tissue.length;j++){
				
				if(tissue[i][j]=='X'){
				csg.setCell(i,j, Color.red);
				}
				if(tissue[i][j]=='O'){
					csg.setCell(i, j, Color.green);
				}
			}
			
		}
		
		for(int m=1;m<=maxRounds;m++){
			
			if(boardSatisfied(tissue,threshold)==true){
				m--;
			}
			numOfMoves+=moveAllUnsatisfied(tissue,threshold);
			
			for(int i=0;i<tissue.length;i++){
				for(int j=0;j<tissue.length;j++){
					
					if(tissue[i][j]=='X'){
					csg.setCell(i,j, Color.red);
					}
					if(tissue[i][j]=='O'){
						csg.setCell(i, j, Color.green);
					}
					if(tissue[i][j]==' '){
						csg.setCell(i, j, Color.white);
					}
				}
				
			}
			
			
			
			if(m%frequency==0){
			System.out.println();
			printTissue(tissue);
			System.out.println("The board is: "+boardSatisfied(tissue,threshold));
			}
			
			if(boardSatisfied(tissue,threshold)==true){
				System.out.println();
				System.out.println("Final tissue:");
				printTissue(tissue);
				System.out.println("The number of rounds that moveAllUnsatisfied has made: "+m);
				System.out.println("The number of moves is: "+numOfMoves);
				break;
			}
			
			if(m==maxRounds&&boardSatisfied(tissue,threshold)==false){
				
				
				int unsatisfied=0;
		    	for(int i=0;i<tissue.length;i++){
		    		for(int j=0;j<tissue.length;j++){
		    			
		    			if(j < 0 || i < 0 || i > tissue.length || j > tissue[i].length) {
							continue;
						}
		    			if(tissue[i][j]=='X'){
		    				totalX++;
		    			}
		    			if(tissue[i][j]=='O'){
		    				totalO++;
		    			}
		    			
		    			if(isSatisfied(tissue,i,j,threshold)==false){
		    				unsatisfied++;
		    			}
		    		}
		    	}
		    	System.out.println();
		    	System.out.println("Final tissue:");
				printTissue(tissue);
				System.out.println("The board is: "+boardSatisfied(tissue,threshold));
				double u=(((totalX+totalO)-unsatisfied)*100)/(n*n);
				System.out.println("The number of moves is: "+numOfMoves);
				System.out.println("Max rounds completed. The percentace of satisfied cells is: "+u+"%");
			}
		}
		
		
		
		//System.out.println(boardSatisfied(tissue,threshold));
		//System.out.println("The board is "+boardSatisfied(tissue, threshold));
		
		//System.out.println("Cell 0,0 is "+isSatisfied(tissue,0,0,threshold));
		
		
		
	}
	
	
	/**
	* Given a tissue sample, prints the cell make up in grid form
	*
	* @param tissue a 2-D character array representing a tissue sample
	* 
	***/
	public static void printTissue(char[][] tissue){
		//Your code goes here
		//this goes to every cell and prints out single quotes around the value of the cell
	for(int i=0;i<tissue.length;i++){
		for(int j=0;j<tissue.length;j++){
			System.out.print("'"+tissue[i][j]+"'");
			
		}
		System.out.println();
	}
	
	}

	/**
	* Given a blank tissue sample, populate it with the correct cell makeup given the parameters. 
	* Cell type 'X' will be represented by the character 'X'
	* Cell type 'O' will be represented by the character 'O'
	* Vacant spaces will be represented by the character ' '
	*
	* Phase I: alternate X and O cells throughout, vacant cells at the "end" (50% credit)
	*		e.g.:	'X' 'O' 'X' 'O' 'X'
	*				'O' 'X' 'O' 'X' 'O'
	*				'X' 'O' 'X' 'O' 'X'
	*				' ' ' ' ' ' ' ' ' '
	*				' ' ' ' ' ' ' ' ' '
	*
	* Phase II: Random assignment of all cells (100% credit)
	*
	* @param tissue a 2-D character array that has been initialized
	* @param percentBlank the percentage of blank cells that should appear in the tissue
	* @param percentX Of the remaining cells, not blank, the percentage of X cells that should appear in the tissue. Round up if not a whole number
	*
	**/
	public static void assignCellTypes(char[][] tissue, int percentBlank, int percentX){
		char x='X';
		char o='O';
		char space=' ';
		
		int numBlank=(int)Math.ceil((percentBlank/100.0)*tissue.length*tissue.length);
		
		int numX=(int)Math.ceil((percentX/100.0)*(tissue.length*tissue.length-numBlank));
		
		
		
		
		for(int countS = 0; countS <numBlank; countS++ ){//this assigns random cells a space value
			int i=(int)Math.round((Math.random()*(tissue.length-1)));//.nextInt(tissue.length);
			int j=(int)Math.round((Math.random()*(tissue.length-1)));//.nextInt(tissue.length);
			if(tissue[i][j]!=' '){//this makes sure the cell is not already filled
				tissue[i][j]=space;
			}
			else{
				countS--;//if the cell is filled, this makes sure that the count doesnt increment
			}
			
		}
		for(int countX = 0; countX <numX; countX++ ){//this assigns random cells an X value
			int i=(int)Math.round((Math.random()*(tissue.length-1)));//.nextInt(tissue.length);
			int j=(int)Math.round((Math.random()*(tissue.length-1)));//.nextInt(tissue.length);
			if(tissue[i][j]!='X'&&tissue[i][j]!=' '){//this makes sure the cell is not already filled
				tissue[i][j]=x;
			}
			else{
				countX--;//if the cell is filled, this makes sure that the count doesnt increment
			}
			
		}
		for(int i=0;i<tissue.length;i++){//this checks every cell to see if they have a value and fills
										 //empty remaining cells with an O value 
			for(int j=0;j<tissue.length;j++){
				
				if(tissue[i][j]!=space&&tissue[i][j]!=x){
					tissue[i][j]=o;
					
				}
			}
		}
		
		//Your code goes here
		
}
	/**
	    * Given a tissue sample, and a (row,col) index into the array, determines if the agent at that location is satisfied.
	    * Note: Blank cells are always satisfied (as there is no agent)
	    *
	    * @param tissue a 2-D character array that has been initialized
	    * @param row the row index of the agent
	    * @param col the col index of the agent
	    * @param threshold the percentage of like agents that must surround the agent to be satisfied
	    * @return boolean indicating if given agent is satisfied
	    *
	    **/
	    public static boolean isSatisfied(char[][] tissue, int row, int col, int threshold){
			
			int neighborX=0;
			int neighborO=0;
			int neighborNotSpace=0;
			int originalTempRow=row;
			int originalTempCol=col;
			
			
			
				for(row=row-1;row<=originalTempRow+1;row++){
					
					for(col=col-1;col<=originalTempCol+1;col++){
						//System.out.println("Checking " + row + ", " + col);
					
						if(col < 0 || row < 0 || row > tissue.length-1 || col > tissue[row].length-1) {
							continue;
						}

						if(tissue[row][col]=='X'){
						
							neighborNotSpace++;
						}
						if(tissue[row][col]=='O'){
							
							neighborNotSpace++;
						}
						
					}
					col=originalTempCol;
				}
				
			
			int likeCharacter=(int) Math.ceil((threshold/100.0)*neighborNotSpace);
			
			row=originalTempRow;
			col=originalTempCol;
			
			if(tissue[row][col]==' '){
				return true;
			}
			 if(tissue[row][col]=='X'){
				
			
				for(row=row-1;row<=originalTempRow+1;row++){
				
					for(col=col-1;col<=originalTempCol+1;col++){
						
						if(col < 0 || row < 0 || row > tissue.length-1 || col > tissue[row].length-1) {
							continue;
						}
						
						if(tissue[row][col]=='X'){
						
							neighborX++;
						}
					}
					col=originalTempCol;
				}
				row=originalTempRow;
				
				if(neighborX>likeCharacter){
					return true;
				}
				else{
					return false;
				}
			}
			if(tissue[row][col]=='O'){
				
				
				for(row=row-1;row<=originalTempRow+1;row++){
				
					for(col=col-1;col<=originalTempCol+1;col++){
					
						if(col < 0 || row < 0 || row > tissue.length-1 || col > tissue[row].length-1) {
							continue;
						}
						
						if(tissue[row][col]=='O'){
						
							neighborO++;
						}
					}
					col=originalTempCol;
				}
				row=originalTempRow;
				
				if(neighborO>likeCharacter){
					return true;
				}
				else{
					return false;
				}
			}
			return false;
			
	    }
	    
	    /**
	    * Given a tissue sample, determines if all agents are satisfied.
	    * Note: Blank cells are always satisfied (as there is no agent)
	    *
	    * @param tissue a 2-D character array that has been initialized
	    * @return boolean indicating whether entire board has been satisfied (all agents)
	    **/
	    public static boolean boardSatisfied(char[][] tissue, int threshold){
			int unsatisfied=0;
	    	for(int i=0;i<tissue.length;i++){
	    		for(int j=0;j<tissue.length;j++){
	    			
	    			if(j < 0 || i < 0 || i > tissue.length || j > tissue[i].length) {
						continue;
					}
	    			
	    			if(isSatisfied(tissue,i,j,threshold)==false){
	    				unsatisfied++;
	    			}
	    		}
	    	}
	    	if(unsatisfied>0){
	    		
	    	return false;
	    	}
	    	else{
	    		return true;
	    	}
	    }
	    
	    /**
	     * Given a tissue sample, move all unsatisfied agents to a vacant cell
	     *
	     * @param tissue a 2-D character array that has been initialized
	     * @param threshold the percentage of like agents that must surround the agent to be satisfied
	     * @return an integer representing how many cells were moved in this round
	     **/
	     public static int moveAllUnsatisfied(char[][] tissue, int threshold){
	    	 int count=0;
	    	 int r;
	    	 int c;
	    	
	    	 
	    	 if(boardSatisfied(tissue,threshold)==false){
	    		 
	    		 for(int i=0;i<tissue.length;i++){
	 	    		for(int j=0;j<tissue.length;j++){
	 	    			
	 	    			if(j < 0 || i < 0 || i > tissue.length || j > tissue[i].length) {
	 						continue;
	 					}
	 	    			
	 	    			if(isSatisfied(tissue,i,j,threshold)==false){
	 	    				if(tissue[i][j]=='X'){
	 	    					tissue[i][j]='x';
	 	    					
	 	    				}
	 	    				if(tissue[i][j]=='O'){
	 	    					tissue[i][j]='o';
	 	    					
	 	    				}
	 	    				count++;
	 	    			}
	 	    			
	 	    		}
	 	    	} 
	    		 
	    	for(int i=0;i<tissue.length;i++){
	    		for(int j=0;j<tissue.length;j++){
	    			if(tissue[i][j]=='x'){
	    				r=(int)Math.round((Math.random()*(tissue.length-1)));
	    				c=(int)Math.round((Math.random()*(tissue.length-1)));
	    				if(tissue[r][c]==' '){
	    					tissue[i][j]=' ';
	    					tissue[r][c]='X';
	    					
	    				}else{
	    					j--;
	    				}
	    				
	    			}else if(tissue[i][j]=='o'){
	    				r=(int)Math.round((Math.random()*(tissue.length-1)));
	    				c=(int)Math.round((Math.random()*(tissue.length-1)));
	    				if(tissue[r][c]==' '){
	    					tissue[i][j]=' ';
	    					tissue[r][c]='O';
	    				
	    				}else{
	    					j--;
	    				}
	    			}
	    			
	    		}
	    	}
	    	 return count;
	    	 }else
	    	 
	     return 0;
	     }
}
