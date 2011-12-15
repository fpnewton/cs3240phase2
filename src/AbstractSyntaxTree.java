import java.util.ArrayList;


public class AbstractSyntaxTree
{
	private Token							Token;
	private ArrayList<AbstractSyntaxTree>	Children;
	private NodeClass						Class;


	public AbstractSyntaxTree(NodeClass nodeClass)
	{
		this(null, nodeClass);
	}


	public AbstractSyntaxTree(Token token, NodeClass nodeClass)
	{
		Token = token;
		Children = new ArrayList<AbstractSyntaxTree>();
		Class = nodeClass;
	}


	public Token getToken()
	{
		return Token;
	}


	public AbstractSyntaxTree[] getChildren()
	{
		return Children.toArray(new AbstractSyntaxTree[0]);
	}


	public NodeClass getNodeClass()
	{
		return Class;
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

		if (Class == NodeClass.EMPTY)
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
		String output = "Node: " + Class + "\n";

		if (Token != null)
		{
			output += "Token: " + Token + "\n";
		}
		else
		{
			if (Children.size() > 0)
			{
				for (AbstractSyntaxTree child : Children)
				{
					output += "\t" + child.toString();
				}
			}
		}

		return output;
	}
}
