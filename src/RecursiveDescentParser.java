import java.util.ArrayList;


public class RecursiveDescentParser
{
	ArrayList<Token>	tokens	= new ArrayList<Token>();


	void run()
	{
		tokens.add(new Token(TokenType.INTNUM, "1"));
		tokens.add(new Token(TokenType.PLUS, "+"));
		tokens.add(new Token(TokenType.INTNUM, "2"));
		tokens.add(new Token(TokenType.MULT, "*"));
		tokens.add(new Token(TokenType.INTNUM, "3"));
		
		expr();
	}


	Token peekToken()
	{
		if (tokens.size() == 0)
		{
			return null;
		}
		
		return tokens.get(0);
	}
	
	
	Token popToken()
	{
		if (tokens.size() == 0)
		{
			return null;
		}
		
		return tokens.remove(0);
	}
	
	
	void term()
	{
		factor();
		t_tail();
	}


	void expr()
	{
		term();
		e_tail();
	}


	void e_tail()
	{
		Token token = peekToken();

		if (token.getTokenType() == TokenType.PLUS || token.getTokenType() == TokenType.MINUS)
		{
			matchToken(token);

			term();
			e_tail();
		}
		else
		{
			return;
		}
	}


	void t_tail()
	{
		Token token = peekToken();

		if (token.getTokenType() == TokenType.MULT)
		{
			matchToken(token);

			term();
			t_tail();
		}
		else
		{
			return;
		}
	}


	void factor()
	{
		Token token = peekToken();
		
		if (token.getTokenType() == TokenType.L_PAREN)
		{
			matchToken(token);
			
			expr();
			
			matchToken(token);
		}
		else if (token.getTokenType() == TokenType.INTNUM)
		{
			matchToken(token);
		}
		else if (token.getTokenType() == TokenType.ID)
		{
			matchToken(token);
		}
	}
	
	void matchToken(Token token)
	{
		System.out.println(popToken());
	}
}
