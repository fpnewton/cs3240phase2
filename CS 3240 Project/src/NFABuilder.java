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


	// Creates an NFA that matches Kleene repetition of another NGA
	public static final NFA repetition(NFA nfa)
	{
		nfa.End.addEmptyTransition(nfa.Start);
		nfa.Start.addEmptyTransition(nfa.End);

		return nfa;
	}


	// Creates an NFA that is concatenation of two NFAs
	public static final NFA concatenation(NFA a, NFA b)
	{
		a.End.isFinal = false;
		b.End.isFinal = true;

		a.End.addEmptyTransition(b.Start);

		return new NFA(a.Start, b.End);
	}


	// Creates an NFA that is the logical or of two NFAs
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
}
