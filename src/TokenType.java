public enum TokenType
{
	BEGIN("BEGIN"),
	END("END"),
	ASSIGN("ASSIGN"),
	RECURSIVE_REPLACE("RECURSIVE-REPLACE"),
	REPLACE("REPLACE"),
	WITH("WITH"),
	IN("IN"),
	TO("TO"),
	SEMICOLON("SEMICOLON"),
	PRINT("PRINT"),
	RPAREN("RPAREN"),
	LPAREN("LPAREN"),
	COMMA("COMMA"),
	POUND("POUND"),
	FIND("FIND"),
	PLUS("PLUS"),
	MINUS("MINUS"),
	MULTIPLY("MULTIPLY"),
	DIVIDE("DIVIDE"),
	DIFF("DIFF"),
	UNION("UNION"),
	INTERS("INTERS"),
	SOURCE_FILE("SRC-FILE"),
	DESTINATION_FILE("DEST-FILE"),
	ID("ID"),
	INTNUM("INTNUM"),
	MAXFREQSTRING("MAXFREQSTRING"),
	REGEX("REGEX"),
	ASCII_STR("ASCII_STR"),
	OUTPUT_FILE("OUTPUT_FILE"),
	FILE_NAMES("FILE_NAMES"),
	TERM("TERM"),
	EMPTY("EMPTY"),
	EXPRESSION("EXPRESSION"),
	EXPRESSION_TAIL("EXP-TAIL"),
	EXPRESSION_LIST("EXP-LIST"),
	EXPRESSION_LIST_TAIL("EXP-LIST-TAIL"),
	STATEMENT("STMT"),
	STATEMENT_LIST("STMT-LIST"),
	STATEMENT_LIST_TAIL("STMT-LIST-TAIL"),
	NULL("NULL");

	
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
