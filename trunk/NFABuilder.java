
public class NFABuilder
{
	public NFA empty()
	{
		NFAState start	= new NFAState();
		NFAState end	= new NFAState();
		
		end.setFinal(true);		
		start.addEmptyTransition(end);
		
		return new NFA(start, end);
	}
	
	
	public NFA character(Character c)
	{
		NFAState start	= new NFAState();
		NFAState end	= new NFAState();
		
		end.setFinal(true);
		start.addCharacterTransition(c, end);
		
		return new NFA(start, end);
	}
	
	
	public NFA string(String str)
	{
		NFA pattern = this.character(str.charAt(0));
		
		for (int i = 1; i < str.length(); i++)
		{
			pattern = this.and(pattern, this.character(str.charAt(i)));
		}
		
		return pattern;
	}
	
	
	public NFA and(NFA a, NFA b)
	{
		a.getEnd().setFinal(false);
		b.getEnd().setFinal(true);
		
		a.getEnd().addEmptyTransition(b.getStart());
		
		return new NFA(a.getStart(), b.getEnd());
	}
	
	
	public NFA or(NFA a, NFA b)
	{
		a.getEnd().setFinal(false);
		b.getEnd().setFinal(false);
		
		NFAState start = new NFAState();
		NFAState end  = new NFAState();
		
		end.setFinal(true);
		start.addEmptyTransition(a.getStart());
		start.addEmptyTransition(b.getStart());
		a.getEnd().addEmptyTransition(end);
		b.getEnd().addEmptyTransition(end);
		
		return new NFA(start, end);
	}
	
	
	public NFA star(NFA a)
	{
		a.getEnd().addEmptyTransition(a.getStart()) ;
        a.getStart().addEmptyTransition(a.getEnd()) ;
        
        return a;
	}
}
