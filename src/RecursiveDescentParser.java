import java.util.ArrayList;


public class RecursiveDescentParser
{
	ArrayList<Token>	Tokens;
	
	
	public RecursiveDescentParser(Token tokens[])
	{
		Tokens = new ArrayList<Token>();
		
		for (Token token : tokens)
		{
			Tokens.add(token);
		}
	}


	void run()
	{
		Tokens.add(new Token(TokenType.BEGIN, null));
		Tokens.add(new Token(TokenType.ID, "a"));
		Tokens.add(new Token(TokenType.ASSIGN, "="));
		Tokens.add(new Token(TokenType.INTNUM, "2"));
		Tokens.add(new Token(TokenType.SEMICOLON, ";"));
		Tokens.add(new Token(TokenType.END, null));

		miniREProgram();
	}


	Token peekToken()
	{
		if (Tokens.size() == 0)
		{
			return new Token(TokenType.NULL, null);
		}

		return Tokens.get(0);
	}


	Token popToken()
	{
		if (Tokens.size() == 0)
		{
			return new Token(TokenType.NULL, null);
		}

		return Tokens.remove(0);
	}


	public boolean matchToken(TokenType type)
	{
		Token token = popToken();

		if (token.getTokenType() != type)
		{
			return false;
		}

		// TODO
		System.out.println(token);

		return true;
	}


	boolean miniREProgram()
	{
		// <MiniRE-program> -> BEGIN <statement-list> END

		if (matchToken(TokenType.BEGIN))
		{
			return statementList() & matchToken(TokenType.END);
		}

		return false;
	}


	boolean statementList()
	{
		// <statement-list> -> <statement> <statement-list-tail>

		if (statement())
		{
			return statementListTail();
		}

		return false;
	}


	boolean statement()
	{
		if (matchToken(TokenType.ID))
		{
			if (matchToken(TokenType.ASSIGN))
			{
				if (expression())
				{
					// <statement> -> ID=<exp>;
					return matchToken(TokenType.SEMICOLON);
				}
				else if (matchToken(TokenType.POUND))
				{
					// <statement> -> ID=#<exp>;
					return expression() & matchToken(TokenType.SEMICOLON);
				}
				else if (matchToken(TokenType.MAXFREQSTRING))
				{
					// <statement> -> ID = MAXFREQSTRING(ID);
					return matchToken(TokenType.LPAREN) & matchToken(TokenType.ID) & matchToken(TokenType.RPAREN);
				}
			}
		}
		else if (matchToken(TokenType.REPLACE))
		{
			// <statement> -> REPLACE <REGEX> WITH <ASCII-STR> IN <file-names>;
			return matchToken(TokenType.REGEX) & matchToken(TokenType.WITH) & matchToken(TokenType.ASCII_STR) & matchToken(TokenType.IN) & fileNames();
		}
		else if (matchToken(TokenType.PRINT))
		{
			// <statement> -> PRINT(<exp-list>);
			return matchToken(TokenType.LPAREN) & expressionList() & matchToken(TokenType.LPAREN) & matchToken(TokenType.SEMICOLON);
		}

		return false;
	}


	boolean statementListTail()
	{
		// <statement-list-tail> -> <statement><statement-list-tail> | e

		if (statement())
		{
			return statementListTail();
		}
		else
		{
			return matchToken(TokenType.EMPTY);
		}
	}


	boolean fileNames()
	{
		// <file-names> -> <source-file> >! <destination-file>
		return matchToken(TokenType.OUTPUT_FILE) & destinationFile();
	}


	boolean sourceFile()
	{
		// <source-file> -> ASCII-STR
		return matchToken(TokenType.ASCII_STR);
	}


	boolean destinationFile()
	{
		// <dest-file> -> ASCII-STR
		return matchToken(TokenType.ASCII_STR);
	}


	boolean expressionList()
	{
		// <exp-list> -> <exp> <exp-list-tail>

		if (expression())
		{
			return expressionListTail();
		}

		return false;
	}


	boolean expressionListTail()
	{
		// <exp-list-tail> -> , <exp> <exp-list-tail>

		if (matchToken(TokenType.COMMA))
		{
			return expression() & expressionListTail();
		}

		return false;
	}


	boolean expression()
	{
		// <exp> -> ID | INTNUM | ( <exp> )
		// <exp> -> <term><exp-tail>

		Token token = peekToken();

		if (token.getTokenType() == TokenType.ID)
		{
			// <exp> -> ID
			return matchToken(TokenType.ID);
		}
		else if (token.getTokenType() == TokenType.INTNUM)
		{
			// <exp> -> INTNUM
			return matchToken(TokenType.INTNUM);
		}
		else if (token.getTokenType() == TokenType.LPAREN)
		{
			// <exp> -> ( <exp> )

			if (matchToken(TokenType.LPAREN))
			{
				return expression() & matchToken(TokenType.RPAREN);
			}
		}
		else
		{
			// <exp> -> <term><exp-tail>

			if (term())
			{
				return expressionTail();
			}
		}

		return false;
	}


	boolean expressionTail()
	{
		if (binaryOperator())
		{
			return term() & expressionTail();
		}
		else
		{
			return matchToken(TokenType.EMPTY);
		}
	}


	boolean term()
	{
		// <term> -> FIND <REGEX> IN <file-name>

		if (matchToken(TokenType.FIND))
		{
			return matchToken(TokenType.REGEX) & matchToken(TokenType.IN) & fileName();
		}

		return false;
	}


	boolean fileName()
	{
		// <file-name> -> ASCII-STR

		return matchToken(TokenType.ASCII_STR);
	}


	boolean binaryOperator()
	{
		// <bin-op> -> DIFF|UNION|INTERS

		Token token = peekToken();

		if (token.getTokenType() == TokenType.DIFF)
		{
			return matchToken(TokenType.DIFF);
		}
		else if (token.getTokenType() == TokenType.UNION)
		{
			return matchToken(TokenType.UNION);
		}
		else if (token.getTokenType() == TokenType.INTERS)
		{
			return matchToken(TokenType.INTERS);
		}

		return false;
	}
}
