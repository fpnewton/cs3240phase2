/**
 * Grammar Scanner Class
 * 
 * Scans a file for grammar rules.
 * 
 * @author	Fraser P. Newton
 * @date	October 29, 2011
 */

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class GrammarScanner
{
	private ArrayList<String>	GrammarLines	= null;
	private Grammar				Grammar			= null;


	public GrammarScanner()
	{
		GrammarLines = new ArrayList<String>();
	}


	public Grammar getGrammar()
	{
		return this.Grammar;
	}


	public boolean ReadGrammarFile(String path)
	{
		boolean hadError = false;
		FileReader inputStream = null;

		try
		{
			// Open the file for reading
			inputStream = new FileReader(path);

			int ch;
			StringBuilder line = new StringBuilder("");

			while ((ch = inputStream.read()) != -1)
			{
				if (((char) ch) == '\r' || ((char) ch) == '\n')
				{
					if (line.length() > 0)
					{
						this.GrammarLines.add(line.toString());

						line.delete(0, line.length());
					}
				}
				else
				{
					line.append((char) ch);
				}
			}

			// this.Buffer = new TokenBuffer(file.toString(), "\r\n", "%%");
		}
		catch (FileNotFoundException e)
		{
			System.out.println("Error: Could not open file " + path + " for reading.");

			hadError = true;
		}
		catch (IOException e)
		{
			System.out.println(e.getStackTrace());

			hadError = true;
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

					hadError = true;
				}
			}
		}

		if (hadError)
		{
			return false;
		}

		return true;
	}


	public boolean parseGrammar()
	{
		MutableGrammar mutGrammar = new MutableGrammar(); // Mutable to build
															// the correct
															// grammar object
															// before storing it
															// into a
															// non-mutable
															// grammar object

		// Iterate through each line in the grammar definition file
		for (String line : this.GrammarLines)
		{
			// Skip comment lines
			if (line.length() >= 2 && line.substring(0, 2).equals("%%"))
			{
				continue;
			}

			// String builders for the each identifier and its definition
			StringBuilder identifier = new StringBuilder("");
			StringBuilder definition = new StringBuilder("");

			// Iterate through each character in the line
			for (int i = 0; i < line.length(); i++)
			{
				if (definition.length() == 0)
				{
					// Build the identifier if no definition has been built
					if (!matchCharacter(line.charAt(i), " \t\r\n"))
					{
						identifier.append(line.charAt(i));
					}
					else
					{
						// Skip whitespace characters
						while (matchCharacter(line.charAt(i), " \t\r\n"))
						{
							i++;
						}

						definition.append(line.charAt(i));
					}
				}
				else
				{
					// Skip whitespace characters only for regular expressions
					while (matchCharacter(line.charAt(i), " \t\r\n") && definition.charAt(0) != '[')
					{
						i++;
					}

					// Build the identifier definition
					definition.append(line.charAt(i));
				}
			}

			// Make sure that the identifier is valid
			if (identifier.charAt(0) != '$')
			{
				System.out.println("Error: Invalid identifier '" + identifier + "'");

				return false;
			}

			if (definition.charAt(0) == '[')
			{
				String strs[] = expandCharacterClassDefinition(definition.toString());

				String pattern;

				if (strs.length == 3)
				{
					String exstr = mutGrammar.getCharacterClassRegex(strs[2]);

					pattern = "[" + exclude(exstr, strs[0].substring(1)) + "]";
				}
				else
				{
					pattern = strs[0];
				}

				mutGrammar.addCharacterClass(identifier.toString(), pattern);
			}
			else
			{
				mutGrammar.addIdentifier(identifier.toString(), definition.toString());
			}
		}

		this.Grammar = new Grammar(mutGrammar);

		return true;
	}


	private String exclude(String pattern, String exclude)
	{
		StringBuilder result = new StringBuilder("");

		for (int i = 1; i < pattern.length() - 1; i++)
		{
			boolean didFind = false;

			for (int j = 1; j < exclude.length() - 1; j++)
			{
				char p = pattern.charAt(i) != '\\' ? pattern.charAt(i) : pattern.charAt(i + 1);
				char e = exclude.charAt(j) != '\\' ? exclude.charAt(j) : exclude.charAt(j + 1);

				if (p == e)
				{
					didFind = true;

					break;
				}
			}

			if (!didFind)
			{
				result.append(pattern.charAt(i) != '\\' ? pattern.charAt(i) : "\\" + pattern.charAt(i + 1));
			}
		}

		return result.toString();
	}


	private String[] expandCharacterClassDefinition(String characterClass)
	{
		ArrayList<String> tokens = new ArrayList<String>();

		String tk[] = split(characterClass, ' ');

		for (String s : tk)
		{
			if (s.charAt(0) != '[')
			{
				tokens.add(s);

				continue;
			}

			StringBuilder tmp = new StringBuilder("");

			for (int i = 0; i < s.length() - 1; i++)
			{
				if (s.charAt(i + 1) == '-' && s.charAt(i) != '\\')
				{
					for (char c = s.charAt(i); c <= (s.charAt(i + 2) != '\\' ? s.charAt(i + 2) : s.charAt(i + 3)); c++)
					{
						tmp.append(c);
					}

					i += 2;

					if (i >= s.length())
					{
						break;
					}
				}
				else
				{
					tmp.append(s.charAt(i));
				}
			}

			tmp.append(']');
			tokens.add(tmp.toString());
		}

		return tokens.toArray(new String[0]);
	}


	private boolean matchCharacter(char c, String pattern)
	{
		if (pattern.length() == 0)
		{
			return false;
		}

		for (char ch : pattern.toCharArray())
		{
			if (c == ch)
			{
				return true;
			}
		}

		return false;
	}


	public String[] split(String input, char token)
	{
		ArrayList<String> tokens = new ArrayList<String>();
		StringBuilder tmp = new StringBuilder("");

		for (int i = 0; i < input.length(); i++)
		{
			if (input.charAt(i) == token)
			{
				tokens.add(tmp.toString());
				tmp.delete(0, tmp.length());

				continue;
			}

			tmp.append(input.charAt(i));
		}

		tokens.add(tmp.toString());

		return tokens.toArray(new String[0]);
	}


	public String[] tokenize(String input)
	{
		ArrayList<String> tokens = new ArrayList<String>();
		StringBuilder token = new StringBuilder();

		for (int i = 0; i < input.length(); i++)
		{
			if (input.charAt(i) == '\\' && i < (input.length() - 1))
			{
				if (matchCharacter(input.charAt(i + 1), " \\*+|[]().'\""))
				{
					i++;
					continue;
				}
			}
			else if (input.charAt(i) == ' ')
			{
				continue;
			}

			if (matchCharacter(input.charAt(i), "\\*+|[]().'\""))
			{
				if (token.length() > 0)
				{
					tokens.add(token.toString());
				}

				tokens.add(input.charAt(i) + "");

				token.delete(0, token.length());
			}
			else
			{
				token.append(input.charAt(i));
			}
		}

		return tokens.toArray(new String[0]);
	}
}
