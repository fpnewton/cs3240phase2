import java.util.ArrayList;


public class NFAState
{
	public boolean				isFinal		= false;

	private ArrayList<NFAState>	onChar[]	= new ArrayList[255];
	private ArrayList<NFAState>	onEmpty		= new ArrayList<NFAState>();


	public NFAState()
	{
		for (int i = 0; i < onChar.length; i++)
		{
			onChar[i] = new ArrayList<NFAState>();
		}
	}


	// Adds a transition to another state for a character input
	public void addCharacterTransition(char character, NFAState state)
	{
		onChar[(int) character].add(state);
	}


	// Adds an empty transition to another state
	public void addEmptyTransition(NFAState state)
	{
		onEmpty.add(state);
	}


	public boolean matches(String input)
	{
		return matches(input, new ArrayList<NFAState>());
	}


	private boolean matches(String input, ArrayList<NFAState> visitedStates)
	{
		// Prevent infinite loops
		if (visitedStates.contains(this))
		{
			return false;
		}

		visitedStates.add(this);

		// Out of characters to process
		if (input.length() == 0)
		{
			if (isFinal)
			{
				return true;
			}

			// Check empty transitions
			for (NFAState next : onEmpty)
			{
				if (next.matches("", visitedStates))
				{
					return true;
				}
			}

			return false;
		}
		else
		{
			int character = (int) input.charAt(0);

			// Check character transitions
			for (NFAState next : onChar[character])
			{
				if (next.matches(input.substring(1)))
				{
					return true;
				}
			}

			// Check empty transitions
			for (NFAState next : onEmpty)
			{
				if (next.matches(input, visitedStates))
				{
					return true;
				}
			}

			return false;
		}
	}
}
