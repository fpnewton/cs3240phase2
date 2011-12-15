import java.util.ArrayList;


public class AbstractSyntaxTree
{
	private Token							Token;
	private ArrayList<AbstractSyntaxTree>	Children;


	public AbstractSyntaxTree()
	{
		this(null);
	}


	public AbstractSyntaxTree(Token token)
	{
		Children	= new ArrayList<AbstractSyntaxTree>();
		Token		= token;
	}


	public Token getToken()
	{
		return Token;
	}


	public AbstractSyntaxTree[] getChildren()
	{
		return Children.toArray(new AbstractSyntaxTree[0]);
	}


	public void addChild(AbstractSyntaxTree child)
	{
		Children.add(child);
	}


	public AbstractSyntaxTree getChild(int index)
	{
		return Children.get(index);
	}


	public int getSize()
	{
		int size = 0;

		if (Token.getTokenType() == TokenType.EMPTY)
		{
			return 0;
		}
		else if (Token != null)
		{
			return 1;
		}

		for (AbstractSyntaxTree child : Children)
		{
			size += child.getSize();
		}

		return size;
	}


	public String toString()
	{
		return toString(0);
	}
	
	
	private String toString(int depth)
	{
String output = "";
		
		if (Token != null)
		{
			output += "Token: " + Token + "\n";
		}
		
		if (Children.size() > 0)
		{
			for (AbstractSyntaxTree child : Children)
			{
				for (int i = 0; i < depth; i++)
				{
					output += "  ";
				}
				
				output += child.toString(depth++);
			}
			
			depth--;
		}

		return output;
	}
}
