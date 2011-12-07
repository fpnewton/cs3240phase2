import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Main
 * Application entry point.
 * 
 * @author	Fraser P. Newton
 * @date	October 29, 2011
 */

public class Main
{
	static Hashtable<String, NFA> NFAs = new Hashtable<String, NFA>();
	/**
	 * Application entry point.
	 * 
	 * @param args Program argument of type <code>String</code>
	 */
	public static void main(String[] args)
	{
//		if (args.length != 3)
//		{
//			System.out.println("Usage: java Main.class path_to_grammar.txt path_to_file_to_scan.txt");
//			System.exit(-1);
//		}
		
		GrammarScanner scanner = new GrammarScanner();
		
		//scanner.ReadGrammarFile(args[0]);
		scanner.ReadGrammarFile("C:\\Users\\Fraser\\Eclipse Workspace\\CS 3240 Project\\src\\sample_input_specification.txt");
		scanner.parseGrammar();
		
		Grammar g = scanner.getGrammar();	
		
		
		FileReader inputStream	= null;
		ArrayList<String> tokens = new ArrayList<String>();

		try
		{
			// Open the file for reading
			//inputStream = new FileReader(args[1]);
			inputStream = new FileReader("C:\\Users\\Fraser\\Eclipse Workspace\\CS 3240 Project\\src\\sample_code.txt");

			int				ch;
			StringBuilder	line = new StringBuilder("");

			while ((ch = inputStream.read()) != -1)
			{
				if (ch == ' ')
				{
					tokens.add(line.toString());
					line.delete(0, line.length());
					ch = inputStream.read();
				}
				else
				{
					line.append((char) ch);
				}
			}
		}
		catch (FileNotFoundException e)
		{
			//System.out.println("Error: Could not open file " + path + " for reading.");
		}
		catch (IOException e)
		{
			System.out.println(e.getStackTrace());
		}
		finally
		{
			if (inputStream != null)
			{
				try
				{
					inputStream.close();
				}
				catch (IOException e)
				{
					System.out.println(e.getStackTrace());
				}
			}
		}
		
		generateCharacterClassNFAs(g);
		generateIdentifierNFAs(g);
	}
	
	
	private static void generateCharacterClassNFAs(Grammar g)
	{
		String keys[] = g.getCharacterClassKeys();
		
		for (String key : keys)
		{
			String pattern = g.getCharacterClassRegex(key);

			NFA nfa = NFABuilder.character(pattern.charAt(0));
			
			for (int i = 1; i < pattern.length(); i++)
			{
				nfa = NFABuilder.or(nfa, NFABuilder.character(pattern.charAt(i)));
			}
			
			NFAs.put(key, nfa);
		}
	}
	
	
	private static void generateIdentifierNFAs(Grammar g)
	{		
		for (int k = 0; k < g.getNumIdentifiers(); k++)
		{
			String key = g.getIdentifier(k)[0];
			String pattern = g.getIdentifier(k)[1];
			
			ArrayList<String> tokens = new ArrayList<String>();
			StringBuilder tmp = new StringBuilder("");
			
			for (int i = 0; i < pattern.length(); i++)
			{
				char c = pattern.charAt(i);
				
				if (c == '[' || c == ']')
				{
					if (tmp.length() > 0)
					{
						tokens.add(tmp.toString());
						tmp.delete(0, tmp.length());
					}
					
					continue;
				}
				
				if (c == '(' || c == ')' || c == '|' || c == '*' || c == '+')
				{
					if (tmp.length() > 0)
					{
						tokens.add(tmp.toString());
						tmp.delete(0, tmp.length());
					}
					
					StringBuilder s = new StringBuilder("");
					s.append(c);
					
					tokens.add(s.toString());
					tmp.delete(0, tmp.length());
					
					continue;
				}
				
				if (c == '\\')
				{
					if (tmp.length() > 0)
					{
						tokens.add(tmp.toString());
						tmp.delete(0, tmp.length());
					}
					
					tokens.add("" + c + pattern.charAt(++i));
					tmp.delete(0, tmp.length());
					
					continue;
				}
				
				if (c == '$')
				{
					if (tmp.length() > 0)
					{
						tokens.add(tmp.toString());
						tmp.delete(0, tmp.length());
					}
					
					for (int j = i; j < pattern.length(); j++)
					{
						char d = pattern.charAt(j);
						
						if (d == '(' || d == ')' || d == '|' || d == '*' || d == '+' || d == '\\' || d == '[' || d == ']')
						{							
							i = j - 1;
							
							break;
						}
						
						tmp.append(d);
					}
					
					tokens.add(tmp.toString());
					tmp.delete(0, tmp.length());
					
					continue;
				}
				
				tmp.append(c);
			}
			
			if (tmp.length() > 0)
			{
				tokens.add(tmp.toString());
				tmp.delete(0, tmp.length());
			}
			
			generateNFA(tokens);
		}
	}
	
	
	private static void generateNFA(ArrayList<String> pattern)
	{
		ArrayList<String> tmp = new ArrayList<String>();//(ArrayList<String>) pattern.clone();
		ArrayList<String> tests = new ArrayList<String>();
		
		tmp.add("$SMALLCASE");
		
		tests.add("a");
		tests.add("N");
		tests.add("an");
		tests.add("An");
		
		NFA nfa = generateNFA(tmp, new NFA());
		
		for (String test : tests)
		{
			System.out.println("Test: " + test + "\t\tResult: " + nfa.matches(test));
		}
		
//		NFA nfa = generateNFA(pattern, new NFA());
//		
//		System.out.println(tmp + "\t\t" + nfa.doesMatch("q5y"));
//		System.out.println(tmp + "\t\t" + nfa.doesMatch("BB5"));
//		System.out.println(tmp + "\t\t" + nfa.doesMatch("4"));
//		System.out.println(tmp + "\t\t" + nfa.doesMatch("3.14159"));
	}
	
	
	private static NFA generateNFA(ArrayList<String> pattern, NFA chain)
	{
		if (pattern.size() == 0)
		{
			return chain;
		}
		
		NFA tmp = null;
		String token = pattern.remove(0);
		
		if (token.compareTo("(") == 0)
		{
			tmp = generateNFA(pattern, chain);
			
			return generateNFA(pattern, tmp);
		}
		
		if (token.compareTo(")") == 0)
		{
			return chain;
		}
		
		if (token.compareTo("|") == 0)
		{
			return NFABuilder.or(chain, generateNFA(pattern, chain));
		}
		
		if (token.compareTo("*") == 0 || token.compareTo("+") == 0)
		{
			tmp = NFABuilder.repetition(chain);
			
			return generateNFA(pattern, tmp);
		}
		
		if (token.charAt(0) == '$')
		{
			NFA charClass = NFAs.get(token);
			
			if (charClass != null)
			{
				tmp = charClass;
				
				return generateNFA(pattern, tmp);
			}
		}
		else
		{
			tmp = NFABuilder.character(token.charAt(0));
			
			for (int i = 1; i < token.length(); i++)
			{
				if (token.charAt(i) == '\\')
				{
					tmp = NFABuilder.concatenation(tmp, NFABuilder.character(token.charAt(++i)));
				}
				else
				{
					tmp = NFABuilder.concatenation(tmp, NFABuilder.character(token.charAt(i)));
				}
			}
			
			return generateNFA(pattern, tmp);
		}
		
		
		return generateNFA(pattern, chain);
	}
}
