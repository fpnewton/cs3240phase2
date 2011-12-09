public class Token
{
	private TokenType	Type;
	private String		Data;


	public Token(TokenType type, String data)
	{
		Type = type;
		Data = data;
	}


	public TokenType getTokenType()
	{
		return Type;
	}


	public String getData()
	{
		return Data;
	}


	public String toString()
	{
		return Type.toString() + " : " + Data;
	}
}
