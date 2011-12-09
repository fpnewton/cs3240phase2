import java.util.ArrayList;


public class NFAParser
{
	private Grammar G = null;
	
	public NFAParser(Grammar g)
	{
		G = g;
	}
	
	private NFA generateCharacterClassNFA(String key)
	{
		String pattern = G.getCharacterClassRegex(key);

		if (pattern == null)
		{
			return new NFA();
		}

		NFA nfa = NFABuilder.character(pattern.charAt(0));

		for (int i = 1; i < pattern.length(); i++)
		{
			nfa = NFABuilder.or(nfa, NFABuilder.character(pattern.charAt(i)));
		}

		return nfa;
	}


	public void generateIdentifierNFAs()
	{
		for (int k = 0; k < G.getNumIdentifiers(); k++)
		{
			String key = G.getIdentifier(k)[0];
			String pattern = G.getIdentifier(k)[1];

			ArrayList<String> tokens = new ArrayList<String>();
			StringBuilder tmp = new StringBuilder("");

			for (int i = 0; i < pattern.length(); i++)
			{
				char c = pattern.charAt(i);

				if (c == '[' || c == ']')
				{
					if (tmp.length() > 0)
					{
						tokens.add(tmp.toString());
						tmp.delete(0, tmp.length());
					}

					continue;
				}
				else if (c == '(' || c == ')' || c == '|' || c == '*' || c == '+')
				{
					if (tmp.length() > 0)
					{
						tokens.add(tmp.toString());
						tmp.delete(0, tmp.length());
					}

					StringBuilder s = new StringBuilder("");
					s.append(c);

					tokens.add(s.toString());
					tmp.delete(0, tmp.length());

					continue;
				}
				else if (c == '\\')
				{
					if (tmp.length() > 0)
					{
						tokens.add(tmp.toString());
						tmp.delete(0, tmp.length());
					}

					tokens.add("" + c + pattern.charAt(++i));
					tmp.delete(0, tmp.length());

					continue;
				}
				else if (c == '$')
				{
					if (tmp.length() > 0)
					{
						tokens.add(tmp.toString());
						tmp.delete(0, tmp.length());
					}

					for (int j = i; j < pattern.length(); j++)
					{
						char d = pattern.charAt(j);

						if (d == '(' || d == ')' || d == '|' || d == '*' || d == '+' || d == '\\' || d == '[' || d == ']')
						{
							i--;

							break;
						}

						i++;

						tmp.append(d);
					}

					tokens.add(tmp.toString());
					tmp.delete(0, tmp.length());

					continue;
				}

				tmp.append(c);
			}

			if (tmp.length() > 0)
			{
				tokens.add(tmp.toString());
				tmp.delete(0, tmp.length());
			}

			NFA nfa = generateNFA(tokens);
			
			G.addIdentifierNFA(key, nfa);
		}
	}


	private NFA generateNFA(ArrayList<String> pattern)
	{
		ArrayList<String> tmp = (ArrayList<String>) pattern.clone();

		NFA nfa = generateNFA(tmp, null);

		while (tmp.size() > 0)
		{
			nfa = NFABuilder.concatenate(nfa, generateNFA(tmp, null));
		}

		return nfa;
	}


	private NFA generateNFA(ArrayList<String> pattern, NFA chain)
	{
		if (pattern.size() == 0)
		{
			return chain;
		}

		String token = pattern.remove(0);

		if (token.compareTo("(") == 0)
		{
			NFA tmp = generateNFA(pattern, null);

			if (chain == null)
			{
				return generateNFA(pattern, tmp);
			}

			return NFABuilder.concatenate(chain, generateNFA(pattern, tmp));
		}
		else if (token.compareTo(")") == 0)
		{
			return chain;
		}
		else if (token.compareTo("*") == 0 || token.compareTo("+") == 0)
		{
			return NFABuilder.repetition(chain);
		}
		else if (token.compareTo("|") == 0)
		{
			NFA tmp = generateNFA(pattern, null);

			return NFABuilder.or(chain, tmp);
		}
		else if (token.charAt(0) == '$')
		{
			NFA charClass = generateCharacterClassNFA(token);

			if (charClass != null)
			{
				NFA tmp = generateNFA(pattern, charClass);

				return tmp;
			}
		}
		else
		{
			NFA tmp = null;

			if (token.charAt(0) == '\\')
			{
				tmp = NFABuilder.character(token.charAt(1));
			}
			else
			{
				tmp = NFABuilder.character(token.charAt(0));
			}

			for (int i = (token.charAt(0) == '\\' ? 2 : 1); i < token.length(); i++)
			{
				if (token.charAt(i) == '\\')
				{
					NFA character = NFABuilder.character(token.charAt(++i));

					tmp = NFABuilder.concatenate(tmp, character);
				}
				else
				{
					NFA character = NFABuilder.character(token.charAt(i));

					tmp = NFABuilder.concatenate(tmp, character);
				}
			}

			return tmp;
		}

		return chain;
	}
}
