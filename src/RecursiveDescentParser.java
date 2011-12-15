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


	private Token peekToken()
	{
		if (Tokens.size() == 0)
		{
			return new Token(TokenType.NULL, null);
		}

		return Tokens.get(0);
	}


	private Token popToken()
	{
		if (Tokens.size() == 0)
		{
			return new Token(TokenType.NULL, null);
		}

		return Tokens.remove(0);
	}


	private Token matchToken(TokenType type)
	{
		Token token = peekToken();

		if (token.getTokenType() != type)
		{
			return null;
		}
		
		return popToken();
	}


	public AbstractSyntaxTree miniREProgram()
	{
		// <MiniRE-program> -> BEGIN <statement-list> END
		if (matchToken(TokenType.BEGIN) == null)
		{
			return null;
		}
		
		AbstractSyntaxTree ast = statementList();
		
		if (matchToken(TokenType.END) == null)
		{
			return null;
		}
		
		return ast;
	}


	private AbstractSyntaxTree statementList()
	{
		// <statement-list> -> <statement> <statement-list-tail>
		AbstractSyntaxTree	ast 				= new AbstractSyntaxTree(new Token(TokenType.STATEMENT_LIST, null));
		AbstractSyntaxTree	stmtTree			= statement();
		AbstractSyntaxTree	stmtListTailTree	= statementListTail();
		
		if (stmtTree == null || stmtListTailTree == null)
		{
			return null;
		}
		
		ast.addChild(stmtTree);
		ast.addChild(stmtListTailTree);
		
		return ast;
	}
	
	
	private AbstractSyntaxTree statement4()
	{
		if (matchToken(TokenType.REPLACE) == null)
		{
			return null;
		}
		
		AbstractSyntaxTree	ast 			= new AbstractSyntaxTree(new Token(TokenType.REPLACE, null));
		AbstractSyntaxTree	regexTree		= new AbstractSyntaxTree(matchToken(TokenType.REGEX));
		AbstractSyntaxTree	withTree		= new AbstractSyntaxTree(matchToken(TokenType.WITH));
		AbstractSyntaxTree	strTree			= new AbstractSyntaxTree(matchToken(TokenType.ASCII_STR));
		AbstractSyntaxTree	inTree			= new AbstractSyntaxTree(matchToken(TokenType.IN));
		AbstractSyntaxTree	fileNamesTree	= fileNames();
		
		if (regexTree == null || withTree == null || strTree == null || inTree == null || fileNamesTree == null || matchToken(TokenType.SEMICOLON) == null)
		{
			return null;
		}
		
		ast.addChild(regexTree);
		ast.addChild(withTree);
		ast.addChild(strTree);
		ast.addChild(inTree);
		ast.addChild(fileNamesTree);
		
		return ast;
	}
	
	
	private AbstractSyntaxTree statement5()
	{
		if (matchToken(TokenType.RECURSIVE_REPLACE) == null)
		{
			return null;
		}
		
		AbstractSyntaxTree	ast				= new AbstractSyntaxTree(new Token(TokenType.RECURSIVE_REPLACE, null));
		AbstractSyntaxTree	regexTree		= new AbstractSyntaxTree(matchToken(TokenType.REGEX));
		AbstractSyntaxTree	withTree		= new AbstractSyntaxTree(matchToken(TokenType.WITH));
		AbstractSyntaxTree	strTree			= new AbstractSyntaxTree(matchToken(TokenType.ASCII_STR));
		AbstractSyntaxTree	inTree			= new AbstractSyntaxTree(matchToken(TokenType.IN));
		AbstractSyntaxTree	fileNamesTree	= fileNames();
		
		if (regexTree == null || withTree == null || strTree == null || inTree == null || fileNamesTree == null)
		{
			return null;
		}
		
		ast.addChild(regexTree);
		ast.addChild(withTree);
		ast.addChild(strTree);
		ast.addChild(inTree);
		ast.addChild(fileNamesTree);
		
		return ast;
	}
	
	
	private AbstractSyntaxTree statement6()
	{
		if (matchToken(TokenType.PRINT) == null || matchToken(TokenType.LPAREN) == null)
		{
			return null;
		}
		
		AbstractSyntaxTree ast = expressionList();
		
		if (matchToken(TokenType.RPAREN) == null || matchToken(TokenType.SEMICOLON) == null)
		{
			return null;
		}
		
		return ast;
	}


	private AbstractSyntaxTree statement()
	{
		AbstractSyntaxTree	ast	= new AbstractSyntaxTree(new Token(TokenType.STATEMENT, null));
		
		if (peekToken().getTokenType() == TokenType.ID)
		{
			ast.addChild(new AbstractSyntaxTree(matchToken(TokenType.ID)));
			
			if (peekToken().getTokenType() == TokenType.ASSIGN)
			{
				ast.addChild(new AbstractSyntaxTree(matchToken(TokenType.ASSIGN)));
				
				if (peekToken().getTokenType() == TokenType.POUND)
				{
					// <statement> -> ID=#<exp>;
					ast.addChild(new AbstractSyntaxTree(matchToken(TokenType.POUND)));
					
					AbstractSyntaxTree expTree = expression();
					
					if (expTree == null || matchToken(TokenType.SEMICOLON) == null)
					{
						return null;
					}
					
					ast.addChild(expTree);
					
					return ast;
				}
				else if (peekToken().getTokenType() == TokenType.MAXFREQSTRING)
				{
					// <statement> -> ID=MAXFREQSTRING(ID);
					ast.addChild(new AbstractSyntaxTree(matchToken(TokenType.MAXFREQSTRING)));
					ast.addChild(new AbstractSyntaxTree(matchToken(TokenType.ID)));
					
					return ast;
				}
				else
				{
					// <statement> -> ID=<exp>;
					AbstractSyntaxTree expTree = expression();
					
					if (expTree == null || matchToken(TokenType.SEMICOLON) == null)
					{
						return null;
					}
					
					ast.addChild(expTree);
					
					return ast;
				}
			}
		}
		else if (peekToken().getTokenType() == TokenType.REPLACE)
		{
			// <statement> -> REPLACE <REGEX> WITH <ASCII-STR> IN <file-names>;
			AbstractSyntaxTree child = statement4();
			
			if (child == null)
			{
				return null;
			}
			
			ast.addChild(child);
			
			return ast;
		}
		else if (peekToken().getTokenType() == TokenType.RECURSIVE_REPLACE)
		{
			// <statement> -> RECURSIVE_REPLACE <REGEX> WITH <ASCII-STR> IN <file-names>;
			AbstractSyntaxTree child = statement5();
			
			if (child == null)
			{
				return null;
			}
			
			ast.addChild(child);
			
			return ast;
		}
		else if (peekToken().getTokenType() == TokenType.PRINT)
		{
			// <statement> -> PRINT(<exp-list>);
			AbstractSyntaxTree child = statement6();
			
			if (child == null)
			{
				return null;
			}
			
			ast.addChild(child);
			
			return ast;
		}

		return null;
	}


	private AbstractSyntaxTree statementListTail()
	{
		// <statement-list-tail> -> <statement><statement-list-tail> | e
		AbstractSyntaxTree	ast 		= new AbstractSyntaxTree(new Token(TokenType.STATEMENT_LIST_TAIL, null));
		AbstractSyntaxTree	stmtTree	= statement();
		
		if (stmtTree != null)
		{
			ast.addChild(stmtTree);
			ast.addChild(statementListTail());
		}

		return ast;
	}


	private AbstractSyntaxTree fileNames()
	{
		// <file-names> -> <source-file> >! <destination-file>
		AbstractSyntaxTree	ast				= new AbstractSyntaxTree(new Token(TokenType.FILE_NAMES, null));
		AbstractSyntaxTree	srcFileTree		= sourceFile();
		AbstractSyntaxTree	outputTokenTree	= new AbstractSyntaxTree(matchToken(TokenType.OUTPUT_FILE));
		AbstractSyntaxTree	dstFileTree		= destinationFile();
		
		if (srcFileTree == null || outputTokenTree == null || dstFileTree == null)
		{
			return null;
		}
		
		ast.addChild(srcFileTree);
		ast.addChild(outputTokenTree);
		ast.addChild(dstFileTree);
		
		return ast;
	}


	private AbstractSyntaxTree sourceFile()
	{
		// <source-file> -> ASCII-STR
		AbstractSyntaxTree	ast		= null;
		Token				token	= matchToken(TokenType.ASCII_STR);

		if (token != null)
		{
			ast = new AbstractSyntaxTree(token);
		}

		return ast;
	}


	private AbstractSyntaxTree destinationFile()
	{
		// <dest-file> -> ASCII-STR
		AbstractSyntaxTree	ast		= null;
		Token				token	= matchToken(TokenType.ASCII_STR);
		
		
		if (token != null)
		{
			ast = new AbstractSyntaxTree(token);
		}

		return ast;
	}


	private AbstractSyntaxTree expressionList()
	{
		// <exp-list> -> <exp> <exp-list-tail>
		AbstractSyntaxTree	ast			= new AbstractSyntaxTree(new Token(TokenType.EXPRESSION_LIST, null));
		AbstractSyntaxTree	expTree		= expression();
		AbstractSyntaxTree	expListTail	= expressionListTail();
		
		if (expTree == null || expListTail == null)
		{
			return null;
		}
		
		ast.addChild(expTree);
		ast.addChild(expListTail);
		
		return ast;
	}


	private AbstractSyntaxTree expressionListTail()
	{
		// <exp-list-tail> -> , <exp> <exp-list-tail>
		AbstractSyntaxTree	ast 			= new AbstractSyntaxTree(new Token(TokenType.EXPRESSION_LIST_TAIL, null));
		AbstractSyntaxTree	expTree			= null;
		AbstractSyntaxTree	expListTailTree	= null;
		Token				token			= matchToken(TokenType.COMMA);
		
		if (token == null)
		{
			return ast;
		}
		
		expTree			= expression();
		expListTailTree	= expressionListTail();
		
		if (token == null || expTree == null || expListTailTree == null)
		{
			return null;
		}
		
		ast.addChild(new AbstractSyntaxTree(token));
		ast.addChild(expTree);
		ast.addChild(expListTailTree);

		return ast;
	}


	private AbstractSyntaxTree expression()
	{
		// <exp> -> ID | INTNUM | ( <exp> )
		// <exp> -> <term><exp-tail>
		AbstractSyntaxTree	ast =	new AbstractSyntaxTree(new Token(TokenType.EXPRESSION, null));

		Token token = peekToken();

		if (token.getTokenType() == TokenType.ID)
		{
			// <exp> -> ID
			ast.addChild(new AbstractSyntaxTree(matchToken(TokenType.ID)));
		}
		else if (token.getTokenType() == TokenType.INTNUM)
		{
			// <exp> -> INTNUM
			ast.addChild(new AbstractSyntaxTree(matchToken(TokenType.INTNUM)));
		}
		else if (token.getTokenType() == TokenType.LPAREN)
		{
			// <exp> -> ( <exp> )
			AbstractSyntaxTree	lParenTree	= new AbstractSyntaxTree(matchToken(TokenType.LPAREN));
			AbstractSyntaxTree	expTree		= expression();
			Token				tkRParen	= matchToken(TokenType.RPAREN);
			
			if (lParenTree == null || expTree == null || tkRParen == null)
			{
				return null;
			}
			
			ast.addChild(lParenTree);
			ast.addChild(expTree);
			ast.addChild(new AbstractSyntaxTree(tkRParen));
		}
		else
		{
			// <exp> -> <term><exp-tail>
			AbstractSyntaxTree	termTree	= term();
			AbstractSyntaxTree	expTailTree	= expressionTail();
			
			if (termTree == null || expTailTree == null)
			{
				return null;
			}
			
			ast.addChild(termTree);
			ast.addChild(expTailTree);
		}

		return ast;
	}


	private AbstractSyntaxTree expressionTail()
	{
		//<exp-tail> -> <bin-op><term><exp-tail>
		AbstractSyntaxTree	ast			= new AbstractSyntaxTree(new Token(TokenType.EXPRESSION_TAIL, null));
		AbstractSyntaxTree	binopTree	= binaryOperator();
		
		if (binopTree != null)
		{
			AbstractSyntaxTree	termTree	= term();
			AbstractSyntaxTree	expTailTree	= expressionTail();
			
			if (termTree == null || expTailTree == null)
			{
				return null;
			}
			
			ast.addChild(termTree);
			ast.addChild(binopTree);
		}

		return ast;
	}


	private AbstractSyntaxTree term()
	{
		// <term> -> FIND <REGEX> IN <file-name>
		AbstractSyntaxTree	ast				= new AbstractSyntaxTree(new Token(TokenType.TERM, null));
		AbstractSyntaxTree	filenameTree	= null;
		TokenType			types[]			= { TokenType.FIND, TokenType.REGEX, TokenType.IN };
		
		for (TokenType type : types)
		{
			AbstractSyntaxTree tmp = new AbstractSyntaxTree(matchToken(type));
			
			if (tmp == null)
			{				
				return null;
			}
			
			ast.addChild(tmp);
		}
		
		filenameTree = fileName();
		
		if (filenameTree == null)
		{
			return null;
		}
		
		ast.addChild(filenameTree);
		
		return ast;
	}


	private AbstractSyntaxTree fileName()
	{
		// <file-name> -> ASCII-STR
		AbstractSyntaxTree	ast		= null;
		Token				token	= matchToken(TokenType.ASCII_STR);
		
		
		if (token != null)
		{
			ast = new AbstractSyntaxTree(token);
		}

		return ast;
	}


	private AbstractSyntaxTree binaryOperator()
	{
		// <bin-op> -> DIFF|UNION|INTERS
		AbstractSyntaxTree	ast		= null;
		Token				token	= null;
		TokenType			types[]	= { TokenType.DIFF, TokenType.UNION, TokenType.INTERS };
		
		for (TokenType type : types)
		{
			token = matchToken(type);
			
			if (token != null)
			{
				break;
			}
		}
		
		if (token != null)
		{
			ast = new AbstractSyntaxTree(token);
		}
		
		return ast;
	}
}
