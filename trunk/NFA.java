
public class NFA
{
	private NFAState Start;
	private NFAState End;
	
	
	public NFA()
	{
		this(new NFAState(), new NFAState(true));
	}
	
	
	public NFA(NFAState start, NFAState end)
	{
		this.Start	= start;
		this.End	= end;
	}
	
	
	public NFAState getStart()
	{
		return this.Start;
	}
	
	
	public NFAState getEnd()
	{
		return this.End;
	}
	
	
	public void setStart(NFAState start)
	{
		this.Start = start;
	}
	
	
	public void setEnd(NFAState end)
	{
		this.End = end;
	}
	
	
	public boolean doesMatch(String str)
	{
		return Start.doesMatch(str);
	}
}
