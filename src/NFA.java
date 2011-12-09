public class NFA
{
	public NFAState	Start;
	public NFAState	End;
	
	
	public NFA()
	{
		this(new NFAState(), new NFAState());
		
		End.isFinal = true;
		
		Start.addEmptyTransition(End);
	}


	public NFA(NFAState start, NFAState end)
	{
		Start = start;
		End = end;
	}


	public boolean matches(String input)
	{
		return Start.matches(input);
	}
}
