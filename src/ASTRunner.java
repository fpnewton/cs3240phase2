import java.util.ArrayList;
import java.util.HashMap;


public class ASTRunner
{
	private ArrayList<Token>		Tokens;
	private HashMap<String, String>	Variables;


	public ASTRunner(Token tokens[], String[] variableNames)
	{
		Tokens		= new ArrayList<Token>();
		Variables	= new HashMap<String, String>();
		
		for (Token tk : tokens)
		{
			Tokens.add(tk);
		}

		for (String var : variableNames)
		{
			Variables.put(var, null);
		}
	}
	
	
	public void run()
	{
		while (Tokens.size() > 0)
		{
			Token token = Tokens.remove(0);
			
			if (token.getTokenType() == TokenType.BEGIN)
			{
				continue;
			}
			else if (token.getTokenType() == TokenType.ID)
			{
				statement();
			}
		}
	}
	
	
	private void statement()
	{
		Token id = Tokens.remove(0);
		Tokens.remove(0);
		
		if (Tokens.get(0).getTokenType() == TokenType.POUND)
		{
			pound();
		}
	}
	
	
	private void pound()
	{
		
	}
}
