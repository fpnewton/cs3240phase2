import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class MiniREParser
{
	ArrayList<Token> Tokens;
	ArrayList<String> Variables;
	
	public MiniREParser()
	{
		Tokens = new ArrayList<Token>();
		Variables = new ArrayList<String>();
	}
	
	
	public boolean parseProgram(String filepath)
	{
		try
		{
			FileReader inputStream = new FileReader(filepath);
			
			StringBuilder tmp = new StringBuilder();
			
			char character = '\0';
			int c;
			
			while ((c = inputStream.read()) != -1)
			{
				character = (char) c;
				
				if (character == '\r')
				{
					continue;
				}
				
				if (character == '%')
				{
					while (character != '\r' || character != '\n')
					{
						character = (char) inputStream.read();
					}
				}
				else if (character == '\"')
				{
					Tokens.add(new Token(TokenType.ASCII_STR, parseString(inputStream)));
				}
				else if (character == '\'')
				{
					Tokens.add(new Token(TokenType.REGEX, parseRegularExpression(inputStream)));
				}
				else if (character == '(' || character == ')' || character == ';' || character == '=' || character == '+' || character == '-' || character == '*' || character == '/' || character == '#')
				{
					if (tmp.length() > 0)
					{
						if (!addToken(tmp.toString()))
						{
							Tokens.add(new Token(TokenType.ID, tmp.toString()));
							
							if (!Variables.contains(tmp.toString()))
							{
								Variables.add(tmp.toString());
							}
							
							tmp.delete(0, tmp.length());
						}
					}
					
					addToken("" + character);
				}
				else if (character == '>')
				{
					character = (char) inputStream.read();
					
					if (character == '!')
					{
						addToken(">!");
					}
					else
					{
						tmp.append('>');
						tmp.append(character);
					}
				}
				else if (character == ' ' || character == '\r' || character == '\n')
				{
					if (tmp.length() == 0)
					{
						continue;
					}
					
					if (!addToken(tmp.toString()))
					{
						Tokens.add(new Token(TokenType.ID, tmp.toString()));
						
						if (!Variables.contains(tmp.toString()))
						{
							Variables.add(tmp.toString());
						}
					}
					
					tmp.delete(0, tmp.length());
				}
				else
				{
					tmp.append(character);
				}
			}
			
			if (tmp.length() > 0)
			{
				if (!addToken(tmp.toString()))
				{
					Tokens.add(new Token(TokenType.ID, tmp.toString()));
					
					if (!Variables.contains(tmp.toString()))
					{
						Variables.add(tmp.toString());
					}
					
					tmp.delete(0, tmp.length());
				}
			}
			
			inputStream.close();
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}
	
	
	private String parseString(FileReader inputStream) throws IOException
	{
		char character = (char) inputStream.read();
		StringBuilder tmp = new StringBuilder();
		
		while (character != '\"')
		{
			tmp.append(character);
			
			if (character == '\\')
			{
				tmp.append((char) inputStream.read());
			}
			
			character = (char) inputStream.read();
		}
		
		return tmp.toString();
	}
	
	
	private String parseRegularExpression(FileReader inputStream) throws IOException
	{
		char character = (char) inputStream.read();
		StringBuilder tmp = new StringBuilder();
		
		while (character != '\'')
		{
			tmp.append(character);
			
			if (character == '\\')
			{
				tmp.append((char) inputStream.read());
			}
			
			character = (char) inputStream.read();
		}
		
		return tmp.toString();
	}
	
	
	private boolean addToken(String token)
	{
		if (token == null)
		{
			Tokens.add(new Token(TokenType.NULL, null));

			return true;
		}
		else if (token.compareToIgnoreCase("BEGIN") == 0)
		{
			Tokens.add(new Token(TokenType.BEGIN, null));

			return true;
		}
		else if (token.compareToIgnoreCase("END") == 0)
		{
			Tokens.add(new Token(TokenType.END, null));

			return true;
		}
		else if (token.compareToIgnoreCase("=") == 0)
		{
			Tokens.add(new Token(TokenType.ASSIGN, null));

			return true;
		}
		else if (token.compareToIgnoreCase("REPLACE") == 0)
		{
			Tokens.add(new Token(TokenType.REPLACE, null));

			return true;
		}
		else if (token.compareToIgnoreCase("(") == 0)
		{
			Tokens.add(new Token(TokenType.LPAREN, null));

			return true;
		}
		else if (token.compareToIgnoreCase(")") == 0)
		{
			Tokens.add(new Token(TokenType.RPAREN, null));

			return true;
		}
		else if (token.compareToIgnoreCase("UNION") == 0)
		{
			Tokens.add(new Token(TokenType.UNION, null));

			return true;
		}
		else if (token.compareToIgnoreCase("INTERSEC") == 0)
		{
			Tokens.add(new Token(TokenType.INTERS, null));

			return true;
		}
		else if (token.compareToIgnoreCase("DIFF") == 0)
		{
			Tokens.add(new Token(TokenType.DIFF, null));

			return true;
		}
		else if (token.compareToIgnoreCase("PRINT") == 0)
		{
			Tokens.add(new Token(TokenType.PRINT, null));

			return true;
		}
		else if (token.compareToIgnoreCase("FIND") == 0)
		{
			Tokens.add(new Token(TokenType.FIND, null));
			
			return true;
		}
		else if (token.compareToIgnoreCase("") == 0)
		{
			Tokens.add(new Token(TokenType.EMPTY, null));

			return true;
		}
		else if (token.compareToIgnoreCase("WITH") == 0)
		{
			Tokens.add(new Token(TokenType.WITH, null));

			return true;
		}
		else if (token.compareToIgnoreCase("IN") == 0)
		{
			Tokens.add(new Token(TokenType.IN, null));

			return true;
		}
		else if (token.compareToIgnoreCase("TO") == 0)
		{
			Tokens.add(new Token(TokenType.TO, null));

			return true;
		}
		else if (token.compareToIgnoreCase(";") == 0)
		{
			Tokens.add(new Token(TokenType.SEMICOLON, null));

			return true;
		}
		else if (token.compareToIgnoreCase(",") == 0)
		{
			Tokens.add(new Token(TokenType.COMMA, null));

			return true;
		}
		else if (token.compareToIgnoreCase("#") == 0)
		{
			Tokens.add(new Token(TokenType.POUND, null));

			return true;
		}
		else if (token.compareToIgnoreCase("+") == 0)
		{
			Tokens.add(new Token(TokenType.PLUS, null));

			return true;
		}
		else if (token.compareToIgnoreCase("-") == 0)
		{
			Tokens.add(new Token(TokenType.MINUS, null));

			return true;
		}
		else if (token.compareToIgnoreCase("*") == 0)
		{
			Tokens.add(new Token(TokenType.MULTIPLY, null));

			return true;
		}
		else if (token.compareToIgnoreCase("/") == 0)
		{
			Tokens.add(new Token(TokenType.DIVIDE, null));

			return true;
		}
		else if (token.compareToIgnoreCase("MAXFREQSTRING") == 0)
		{
			Tokens.add(new Token(TokenType.MAXFREQSTRING, null));

			return true;
		}
		else if (token.compareToIgnoreCase(">!") == 0)
		{
			Tokens.add(new Token(TokenType.OUTPUT_FILE, null));
			
			return true;
		}
		
		return false;
	}
	
	
	public Token[] getTokens()
	{
		return Tokens.toArray(new Token[0]);
	}
	
	
	public String[] getVariables()
	{
		return Variables.toArray(new String[0]);
	}
}
