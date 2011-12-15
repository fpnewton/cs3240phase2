import java.util.HashMap;


public class ASTRunner
{
	private AbstractSyntaxTree		AST;
	private HashMap<String, String>	Variables;


	public ASTRunner(AbstractSyntaxTree ast, String[] variableNames)
	{
		AST			= ast;
		Variables	= new HashMap<String, String>();

		for (String var : variableNames)
		{
			Variables.put(var, null);
		}
	}
	
	
	public void run()
	{
		run(AST, 0);
	}
	
	
	private void run(AbstractSyntaxTree tree, int depth)
	{
		if (tree.getChildren().length == 0)
		{
			for (int i = 0; i < depth; i++)
			{
				System.out.print("  ");
			}
			System.out.println(tree.getToken());
		}
		else
		{
			for (int i = 0; i < tree.getChildren().length; i++)
			{
				run(tree.getChild(i), depth + 1);
			}
		}
	}
	
	
	private void print(AbstractSyntaxTree exp)
	{
		
	}
}
