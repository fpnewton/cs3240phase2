public enum TokenType
{
	BEGIN("BEGIN"), END("END"), ASSIGN("ASSIGN"), REPLACE("REPLACE"), WITH("WITH"), IN("IN"), TO("TO"), SEMI("SEMI"), PRINT("PRINT"), R_PAREN("R_PAREN"), L_PAREN("L_PAREN"), COMMA("COMMA"), POUND(
			"POUND"), FIND("FIND"), PLUS("PLUS"), MINUS("MINUS"), MULT("MULT"), DIV("DIV"), UNION("UNION"), INTERS("INTERS"), ID("ID"), INTNUM("INTNUM"), REGEX("REGEX"), ASCII_STR("ASCII_STR"), EMPTY(
			"EMPTY");

	private String	TokenName;


	private TokenType(String tokenName)
	{
		TokenName = tokenName;
	}


	public String toString()
	{
		return TokenName;
	}
}
