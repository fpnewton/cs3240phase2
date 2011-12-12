import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;


/**
 * Main Application entry point.
 * 
 * @author Fraser P. Newton
 * @date October 29, 2011
 */

public class Main
{
	//static Hashtable<String, NFA>	NFAs	= new Hashtable<String, NFA>();
	static Grammar G;

	/**
	 * Application entry point.
	 * 
	 * @param args
	 *            Program argument of type <code>String</code>
	 */
	public static void main(String[] args)
	{
		// if (args.length != 3)
		// {
		// System.out.println("Usage: java Main.class path_to_grammar.txt path_to_file_to_scan.txt");
		// System.exit(-1);
		// }

		GrammarScanner scanner = new GrammarScanner();

		// scanner.ReadGrammarFile(args[0]);
		scanner.ReadGrammarFile(System.getProperty("java.class.path") + System.getProperty("file.separator") + "sample_input_specification.txt");
		scanner.parseGrammar();

		G = scanner.getGrammar();

		FileReader inputStream = null;
		ArrayList<String> tokens = new ArrayList<String>();

		try
		{
			// Open the file for reading
			// inputStream = new FileReader(args[1]);
			inputStream = new FileReader(System.getProperty("java.class.path") + System.getProperty("file.separator") + "sample_code.txt");

			int ch;
			StringBuilder line = new StringBuilder("");

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
			System.out.println("Error: Could not open code file for reading.");
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
		
		NFAParser nfaParser = new NFAParser(G);
		nfaParser.generateIdentifierNFAs();
		
		
		RecursiveDescentParser p = new RecursiveDescentParser();
		p.run();
	}
}
