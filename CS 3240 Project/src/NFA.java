public class NFA
{
	public NFAState	Start;
	public NFAState	End;


	public NFA()
	{
		NFAState start	= new NFAState();
		NFAState end	= new NFAState();

		end.isFinal = true;

		Start = start;
		End = end;
	}


	public NFA(NFAState start, NFAState end)
	{
		this.Start = start;
		this.End = end;
	}


	public boolean matches(String string)
	{
		return Start.matches(string);
	}
}
