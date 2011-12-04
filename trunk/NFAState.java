import java.util.ArrayList;


public class NFAState
{	
	private boolean isFinal = false;
	
	private ArrayList<NFAState>	character[]	= null;
	private ArrayList<NFAState>	empty		= null;
	
	
	public NFAState()
	{
		this(false);
	}
	
	
	public NFAState(boolean isFinal)
	{
		this.character	= new ArrayList[255];
		this.empty		= new ArrayList<NFAState>();
		
		for (int i = 0; i < character.length; i++)
		{
			this.character[i] = new ArrayList<NFAState>();
		}
		
		this.isFinal = isFinal;
	}
	
	
	public boolean isFinal()
	{
		return this.isFinal;
	}
	
	
	public void setFinal(boolean isFinal)
	{
		this.isFinal = isFinal;
	}
	
	
	public void addCharacterTransition(char c, NFAState nextState)
	{
		this.character[(int) c].add(nextState);
	}
	
	
	
	public void addEmptyTransition(NFAState nextState)
	{
		this.empty.add(nextState);
	}
	
	
	public boolean doesMatch(String string)
	{
		return doesMatch(string, new ArrayList<NFAState>());
	}
	
	
	private boolean doesMatch(String string, ArrayList<NFAState> visitedStates)
	{
		if (visitedStates.contains(this))
		{
			return false;
		}
		
		visitedStates.add(this);
		
		if (string.length() == 0)
		{
			// Check if this state is a final state
			if (this.isFinal)
			{
				return true;
			}
			
			// Check for a final state among the empty transition states
			for (NFAState nextState : this.empty)
			{
				// Found a final state
				if (nextState.doesMatch("", visitedStates))
				{
					return true;
				}
			}
			
			return false;
		}
		else
		{
			int c = (int) string.charAt(0);
			
			// Check all character transition states to see if one succeeds
			for (NFAState nextState : this.character[c])
			{
				if (nextState.doesMatch(string.substring(1)))
				{
					return true;
				}
			}
			
			// Check all empty transition states
			for (NFAState nextState : this.empty)
			{
				if (nextState.doesMatch(string, visitedStates))
				{
					return true;
				}
			}
			
			return false;
		}
	}
}
