/*

*/

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class nameGenerator
{
	private static double _markovChainMatrix [][]; //Alphabet Matrix for first name

	private final static int _ALPHA = 26; //The number of letters in the alphabet
	private static Random _randomGenerator; //Random Number Generator

	public nameGenerator(String filePath) throws IOException //Constructor
	{
		_markovChainMatrix = _loadMatrixData(new File(filePath)); //Propagates the Markov Chain Matrix

		_randomGenerator = new Random(); //Initializes the random function
	}

	/*
	void printNames: Method
	3 input variables
	O(n) => n equals number of desired names based off the numberOfNames variable
	minNameLength: the minimum length of any name created
	maxNameLength: the maximum length of any name created
	numberOfNames: the number of names to be created
	*/

	public void printNames(int minNameLength, int maxNameLength, int numberOfNames)
	{
		for(int i = 0; i < numberOfNames; i++)
		{
			System.out.println("Name " + (i+1) + ": " +nameCreator(minNameLength,maxNameLength));
		}

		return;
	}

	public String nameCreator(int minNameLength, int maxNameLength)
	{
		String name = "";
		int nameLength = _randomGenerator.nextInt(maxNameLength-minNameLength+1)+minNameLength;

		for(int i = 0; i < nameLength; i++)
		{
			if(i == 0)
			{
				name += (char)(_randomGenerator.nextInt(26)+65);

				continue;
			}

			name+=_findNextLetter(name, i);
		}

		return _capitalize(name);
	}

	private static char _findNextLetter(String name, int i)
	{
		double randomValue = _randomGenerator.nextDouble();
		double tempIncValue = 0;
		int letterStep = 0;
		while(tempIncValue < randomValue)
		{
			tempIncValue += _markovChainMatrix[(name.charAt(i-1)-65)][letterStep];
			letterStep++;
		}

		return (char)(letterStep-1+65);
		}

	private static String _capitalize(String name)
	{
		name = name.toLowerCase();
		char firstLetter = name.charAt(0);
		name = name.substring(1, name.length());
		name = ((char)(firstLetter-32) + name);
		return name;
	}

	private static double[][] _loadMatrixData(File nameList) throws IOException
	{
		double tempMarkovArray[][] = new double[_ALPHA][_ALPHA];
		Scanner sc = new Scanner(nameList);

		int alphaCountArray[] = new int[_ALPHA];

		while(sc.hasNext())
		{
			String name = sc.next();
			name = name.toUpperCase();

			//System.out.println(s);
			for(int i = 1; i < name.length(); i++)
			{
				if(
					name.charAt(i) < 'A' ||
					name.charAt(i) > 'Z' ||
					name.charAt(i-1) < 'A' ||
					name.charAt(i-1) > 'Z'
					)
					continue;
				tempMarkovArray[name.charAt(i-1)-65][name.charAt(i)-65]++;
				alphaCountArray[name.charAt(i-1)-65]++;
			}
		}

		sc.close();

		return _divideAverage(tempMarkovArray, alphaCountArray);
	}

	private static double[][] _divideAverage(double[][] tempMarkovArray, int[] alphaCountArray)
	{
		for(int i = 0; i < tempMarkovArray.length; i++)
		{
			for(int j = 0; j < tempMarkovArray.length; j++)
			{
				tempMarkovArray[i][j]/=alphaCountArray[i];
			}
		}

		return tempMarkovArray;
	}
}
