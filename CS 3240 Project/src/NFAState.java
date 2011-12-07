import java.util.ArrayList;


public class NFAState
{
	public boolean				isFinal					= false;
	private ArrayList<NFAState>	CharacterTransitions[]	= new ArrayList[255];
	private ArrayList<NFAState>	EmptyTransitions		= new ArrayList<NFAState>();


	public void addCharacterTransition(char c, NFAState next)
	{
		CharacterTransitions[(int) c].add(next);
	}


	public void addEmptyTransition(NFAState next)
	{
		EmptyTransitions.add(next);
	}


	public NFAState()
	{
		for (int i = 0; i < CharacterTransitions.length; i++)
		{
			CharacterTransitions[i] = new ArrayList<NFAState>();
		}
	}


	public boolean matches(String string)
	{
		return matches(string, new ArrayList<NFAState>());
	}


	private boolean matches(String string, ArrayList<NFAState> visited)
	{
		if (visited.contains(this))
		{
			// Found an infinite loop
			return false;
		}

		visited.add(this);

		if (string.length() == 0)
		{
			// Empty string so check if this is the final state
			if (isFinal)
			{
				return true;
			}

			// Check the empty transitions if a final state can be reached
			for (NFAState next : EmptyTransitions)
			{
				if (next.matches("", visited))
				{
					return true;
				}
			}

			return false;
		}
		else
		{
			int c = (int) string.charAt(0);
			
			// Check if the next character matches the NFA
			for (NFAState next : CharacterTransitions[c])
			{
				if (next.matches(string.substring(1)))
				{
					return true;
				}
			}

			// The next character doesn't match the NFA, so check if it matches any NFAs in the empty transitions
			for (NFAState next : EmptyTransitions)
			{
				if (next.matches(string, visited))
				{
					return true;
				}
			}
			
			// The string does not match the NFA
			return false;
		}
	}
}
