public class NFABuilder
{
	// Creates an NFA that matches a single character
	public static final NFA character(char character)
	{
		NFAState start = new NFAState();
		NFAState end = new NFAState();

		end.isFinal = true;
		start.addCharacterTransition(character, end);

		return new NFA(start, end);
	}


	// Creates an NFA that matches an empty string
	public static final NFA empty()
	{
		NFAState start = new NFAState();
		NFAState end = new NFAState();

		start.addEmptyTransition(end);
		end.isFinal = true;

		return new NFA(start, end);
	}


	// Creates an NFA that matches zero or more repetitions of an NFA
	public static final NFA repetition(NFA nfa)
	{
		nfa.End.addEmptyTransition(nfa.Start);
		nfa.Start.addEmptyTransition(nfa.End);

		return nfa;
	}


	// Creates an NFA that matches two concatenated NFAs
	public static final NFA concatenate(NFA first, NFA second)
	{
		first.End.isFinal = false;
		second.End.isFinal = true;
		first.End.addEmptyTransition(second.Start);

		return new NFA(first.Start, second.End);
	}


	// Creates an NFA that matches the logical or of two NFAs
	public static final NFA or(NFA a, NFA b)
	{
		a.End.isFinal = false;
		b.End.isFinal = false;

		NFAState start = new NFAState();
		NFAState end = new NFAState();

		end.isFinal = true;

		start.addEmptyTransition(a.Start);
		start.addEmptyTransition(b.Start);
		a.End.addEmptyTransition(end);
		b.End.addEmptyTransition(end);

		return new NFA(start, end);
	}


	/* Syntactic sugar. */
	public static final NFA re(Object o)
	{
		if (o instanceof NFA)
			return (NFA) o;
		else if (o instanceof Character)
			return character((Character) o);
		else if (o instanceof String)
			return fromString((String) o);
		else
		{
			throw new RuntimeException("bad regexp");
		}
	}


	public static final NFA fromString(String str)
	{
		if (str.length() == 0)
		{
			return empty();
		}
		else
		{
			return concatenate(character(str.charAt(0)), fromString(str.substring(1)));
		}
	}
}
