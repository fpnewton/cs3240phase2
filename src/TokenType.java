public enum TokenType
{
	BEGIN("BEGIN"), END("END"), ASSIGN("ASSIGN"), REPLACE("REPLACE"), WITH("WITH"), IN("IN"), TO("TO"), SEMICOLON("SEMICOLON"), PRINT("PRINT"), RPAREN("RPAREN"), LPAREN("LPAREN"), COMMA("COMMA"), POUND(
			"POUND"), FIND("FIND"), PLUS("PLUS"), MINUS("MINUS"), MULTIPLY("MULTIPLY"), DIVIDE("DIVIDE"), DIFF("DIFF"), UNION("UNION"), INTERS("INTERS"), SOURCE_FILE("SOURCE-FILE"), DESTINATION_FILE(
			"DEST-FILE"),

	ID("ID"), INTNUM("INTNUM"),

	MAXFREQSTRING("MAXFREQSTRING"),

	REGEX("REGEX"), ASCII_STR("ASCII_STR"), OUTPUT_FILE("OUTPUT_FILE"),

	EMPTY("EMPTY"), NULL("NULL");

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
