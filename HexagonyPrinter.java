//Takes input in the form of a single argument

import java.util.*;
class HexagonyPrinter 
{
	static final boolean MINIFY_SOURCE   = true;
	static final String  CHARSET         = "UTF-8";
	static final char[]  SPECIAL_CHARS   = {
		'\t', '\n', '', '\f', '\r', ' ',  '!',
		'\"', '#',  '$', '%',  '&',  '\'', '(',
		')',  '*',  '+', ',',  '-',  '.',  '/',
		':',  ';',  '<',  '=', '>',  '?',  '@',
		'[',  '\\', ']',  '^', '_',  '`',  '{', 
		'|',  '}',  '~'};
	static final char[]  NUMBERS         = {	
		'0',  '1',  '2', '3',  '4',  '5',  '6',
		'7',  '8',  '9'};
	
	public static void main(String[] args)
	{
		if(args.length < 1)
		{
			System.out.print("No arguments were inputted");
			System.exit(0);
		}

		int[]  bytes        = getBytes(args[0]);
		String hexagonyCode = generateHexagonyCode(bytes);
		System.out.print(hexagonyCode);
		
	}

	private static String generateHexagonyCode(int[] dataBytes) 
	{
		int      sideLength    = getSideLength(dataBytes.length);
		char[][] hexCodeArray  = makeEmptyHexagon(sideLength);
		hexCodeArray           = addOps(hexCodeArray,sideLength);
		hexCodeArray           = addData(hexCodeArray,dataBytes,sideLength);
		hexCodeArray           = replaceNumbers(hexCodeArray,sideLength);
		String   hexCodeString = writeOutputString(sideLength, hexCodeArray);
		
		return hexCodeString;
	}

	private static char[][] replaceNumbers(char[][] hexGrid, int sideLength) 
	{
		for(int row = 0; row < hexGrid.length; row++)
		{
			for(int col = 0; col < hexGrid[row].length; col++)
			{
				if(hexGrid[row][col]==';')
				{
					char    p1      = hexGrid[row][col - 1];
					char    p2      = hexGrid[row + 1][col + (row >= sideLength - 1 ? 0 : 1)];
					char    p3      = hexGrid[row - 1][col + (row > sideLength - 1 ? 1 : 0)];
					boolean allNums = 
							(p1 >= ('1'+256) && p1 <= ('8'+256)) &&
							(p2 >= ('1'+256) && p2 <= ('8'+256)) &&
							(p3 >= ('1'+256) && p3 <= ('8'+256));
					if(allNums)
					{
						hexGrid[row][col] = '!';
						hexGrid[row][col - 1] = (char)(hexGrid[row][col - 1]-304);
						hexGrid[row + 1][col + (row >= sideLength - 1 ? 0 : 1)] = (char)(hexGrid[row + 1][col + (row >= sideLength - 1 ? 0 : 1)]-304);
						hexGrid[row - 1][col + (row > sideLength - 1 ? 1 : 0)] = (char)(hexGrid[row - 1][col + (row > sideLength - 1 ? 1 : 0)]-304);
					}
				}
			}
		}
		return hexGrid;
	}

	private static String writeOutputString(int sideLength, char[][] hexGrid) {
		String ret = "";
		if(MINIFY_SOURCE)
		{
			for(char[] i : hexGrid)
				for(char j : i)
					ret+=""+j;
			ret=ret.substring(0,ret.length()-2);
		}
		else
			for(char[] i : hexGrid)
			{
				for(int j = 0; j < sideLength * 2 - i.length; j++)
					ret+=" ";
				for(char j : i)
					ret += " " + j;
				ret += "\n";
			}
		return ret;
	}

	private static char[][] addData(char[][] hexGrid,int[] dataBytes,int sideLength)
	{
		boolean pastDataEnd = false;
		String direction  = "E";
		int    currentRow = 0;
		int    currentCol = 0;
		char   currentChr = '.';
		int    lastRow    = 0;
		int    lastCol    = 0;
		char   lastChr    = '.';
		int    dataIndex  = 0;
		char   memValue   = '.';
		while(true)
		{
			if(currentChr == '@' && lastChr != '$')
			{break;}
			if(currentChr == ';' && lastChr == '.')
			{
					if(dataIndex<dataBytes.length)
						hexGrid[lastRow][lastCol] = (char)dataBytes[dataIndex++];
					else if(memValue != 'Ġ')
					{
						hexGrid[lastRow][lastCol] = 'Ġ';
					}
					else if(!pastDataEnd)
					{
						hexGrid[lastRow][lastCol] = 'Ġ';
						pastDataEnd = true;
					}
			}
			else if (currentChr == '/')
			{
				direction =
						direction.equals("E")  ? "NW" :
						direction.equals("SE") ? "W"  :
						direction.equals("SW") ? "SW" :
						direction.equals("W")  ? "SE" :
						direction.equals("NW") ? "E"  :
						direction.equals("NE") ? "NE" :
							"Error";
			}
			else if (currentChr == '_')
			{
				direction =
						direction.equals("E")  ? "E"  :
						direction.equals("SE") ? "NE" :
						direction.equals("SW") ? "NW" :
						direction.equals("W")  ? "W"  :
						direction.equals("NW") ? "SW" :
						direction.equals("NE") ? "SE" :
							"Error";
			}
			else if (currentChr == '\\')
			{
				direction =
						direction.equals("E")  ? "SW" :
						direction.equals("SE") ? "SE" :
						direction.equals("SW") ? "E"  :
						direction.equals("W")  ? "NE" :
						direction.equals("NW") ? "NW" :
						direction.equals("NE") ? "W"  :
							"Error";		
			}
			lastRow = currentRow;
			lastCol = currentCol;
			lastChr = currentChr;
			if(direction == "E")
			{
				currentCol++;
				if(currentCol > hexGrid[currentRow].length - 1)
				{
					currentCol = 0;
					if(currentRow == sideLength - 1)
						currentRow = hexGrid.length - 1;
					else if(currentRow == 0 || currentRow == hexGrid.length - 1)
						currentRow = sideLength - 1;
					else if(currentRow > sideLength - 1)
						currentRow = currentRow - (sideLength - 1);
					else if(currentRow < sideLength - 1)
						currentRow = currentRow + (sideLength - 1);
				}
			}
			else if(direction == "W")
			{
				currentCol--;
				if(currentCol < 0)
				{
					
					if(currentRow == sideLength - 1)
						currentRow = 0;
					else if(currentRow == 0 || currentRow == hexGrid.length - 1)
						currentRow = sideLength - 1;
					else if(currentRow>sideLength - 1)
						currentRow = currentRow - (sideLength - 1);
					else if(currentRow<sideLength - 1)
						currentRow=currentRow + (sideLength - 1);
					currentCol = hexGrid[currentRow].length;
				}
			}
			else if(direction == "NE")
			{
				if(currentRow > sideLength - 1)
					currentCol++;
				currentRow--;
				if(currentRow < 0)
					currentRow = hexGrid.length-1;
				else if(currentCol > hexGrid[currentRow].length)
				{
					currentCol = 0;
					currentRow = sideLength + currentRow;
				}
				
			}
			else if(direction == "SE")
			{
				if(currentRow < sideLength - 1)
					currentCol++;
				currentRow++;
				if(currentRow > hexGrid.length - 1)
					currentRow = 0;
				else if(currentCol > hexGrid[currentRow].length)
				{
					currentCol = 0;
					currentRow = currentRow - sideLength + 1;
				}
				
			}
			else if(direction == "NW")
			{
				if(currentRow <= sideLength - 1 && currentRow != 0)
					currentCol--;
				currentRow--;
				if(currentRow < 0)
					currentRow = hexGrid.length - 1;
				if(currentCol < 0)
				{
					currentRow = sideLength + currentRow;
					currentCol = hexGrid[currentRow].length - 1;
				}
			}
			else if(direction == "SW")
			{
				if(currentRow >= sideLength - 1 && currentRow != hexGrid.length - 1)
					currentCol--;
				currentRow++;
				if(currentRow > hexGrid.length - 1)
					currentRow = 0;
				else if(currentCol < 0)
				{
					currentRow = currentRow - sideLength;
					currentCol = hexGrid[currentRow].length - 1;
				}
			}
			boolean newMemValue = true;
			for (char i : SPECIAL_CHARS)
				newMemValue = newMemValue && i != currentChr;
			if (newMemValue)
				memValue = currentChr;
			
			currentChr = hexGrid[currentRow][currentCol];
		}
		return hexGrid;
	}
	
	private static char[][] addOps(char[][] hexCodeArray, int sideLength) 
	{
		//Add Mirrors
		for(int i=0;i<sideLength-1;i++)
			hexCodeArray[i][0]='_';
		for(int i=1;i<sideLength;i++)
			hexCodeArray[i][hexCodeArray[i].length-1]='/';
		for(int i=0;i<hexCodeArray[hexCodeArray.length-1].length-2;i++)
			hexCodeArray[hexCodeArray.length-1][i]='\\';
		
		
		//Add printers and terminator
		int row = 2, col = 2;
		for(row = 2; row < sideLength * 2 - 2; row += 2)
			for(col = 2; col < hexCodeArray[row].length - 2; col += 2)
				hexCodeArray[row][col] = ';';
		row -= 2;
		col -= 2;
		hexCodeArray[row][col]   = '@';
		hexCodeArray[row][col-1] = '$';
		hexCodeArray[row+1][col] = '$';
		
		return hexCodeArray;
	}

	private static char[][] makeEmptyHexagon(int sideLength) 
	{
		char[][] hexCodeArray = new char[sideLength * 2 - 1][];
		for(int i = 0; i < sideLength; i++)
			hexCodeArray[i] = new char[sideLength + i];
		for(int i = 0; i < sideLength - 1; i++)
			hexCodeArray[sideLength + i] = new char[2 * sideLength - i - 2];
		for (char[] i: hexCodeArray)
			Arrays.fill(i, '.');
		return hexCodeArray;
	}

	private static int getSideLength(int length) 
	{
		//The number of print operators will we a centered hexagonal number minus 1.
		//Each print operator can print 3 bytes.
		//The side length of the entire hexagon is 2 times the length of the 
		//  hexagon formed by the print operators plus one.
		//Encoding for even-numbered side lengths isn't implemented yet.
		int i = 1;
		while(true)
		{
			
			int centeredHexNum = (3 * i * (i - 1)) + 1;
			if((centeredHexNum - 1) * 3 >= length)
				return i*2+1;
			i++;
		}
	}

	private static int[] getBytes(String in) 
	{
		byte[] bytes = {};
		try 
		{
			bytes = in.getBytes(CHARSET);
		} 
		catch (java.io.UnsupportedEncodingException e) 
		{
			e.printStackTrace();
		}
		int[] ret = new int[bytes.length];
		for(int i = 0; i < bytes.length; i++)
		{
			int byteAsInt = (int)bytes[i];
			if (byteAsInt < 0)
				byteAsInt += 256;
			for(int j : SPECIAL_CHARS)
				if(byteAsInt == j)
					byteAsInt += 256;
			for(int j : NUMBERS)
				if(byteAsInt == j)
					byteAsInt += 256;
			ret[i] = byteAsInt;
		}
		return ret;
	}
}
