/**
 * Grammar Class
 * 
 * Immutable data structure to hold grammar character class regular expressions and identifiers.
 * 
 * @author	Fraser P. Newton
 * @date	October 29, 2011
 */

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;


public class Grammar
{
	protected Dictionary<String, String>	CharacterClasses;
	protected ArrayList<ArrayList<String>>	Identifiers;

	protected ArrayList<String>				IdentifierNFANames;
	protected ArrayList<NFA>				IdentifierNFAs;


	public Grammar(MutableGrammar grammar)
	{
		this.CharacterClasses = grammar.getCharacterClasses();
		this.Identifiers = grammar.getIdentifiers();

		this.IdentifierNFANames = new ArrayList<String>(20);
		this.IdentifierNFAs = new ArrayList<NFA>(20);
	}


	public Grammar(Dictionary<String, String> characterClasses, ArrayList<ArrayList<String>> identifiers)
	{
		this.CharacterClasses = characterClasses;
		this.Identifiers = identifiers;

		this.IdentifierNFANames = new ArrayList<String>();
		this.IdentifierNFAs = new ArrayList<NFA>();
	}


	public String getCharacterClassRegex(String characterClass)
	{
		return (String) this.CharacterClasses.get(characterClass);
	}


	public String[] getCharacterClassKeys()
	{
		ArrayList<String> keys = new ArrayList<String>();
		Enumeration<String> charClassKeys = this.CharacterClasses.keys();

		while (charClassKeys.hasMoreElements())
		{
			keys.add(charClassKeys.nextElement());
		}

		return keys.toArray(new String[0]);
	}


	public Dictionary<String, String> getCharacterClasses()
	{
		return this.CharacterClasses;
	}


	public String[] getIdentifier(int index)
	{
		return this.Identifiers.get(index).toArray(new String[0]);
	}


	public int getNumIdentifiers()
	{
		return this.Identifiers.size();
	}


	public ArrayList<ArrayList<String>> getIdentifiers()
	{
		return this.Identifiers;
	}


	public int getNumCharacterClasses()
	{
		return this.CharacterClasses.size();
	}


	public void addIdentifierNFA(String name, NFA nfa)
	{
		int i = IdentifierNFANames.size();

		IdentifierNFANames.add(i, name);
		IdentifierNFAs.add(i, nfa);
	}


	public NFA[] getIdentifierNFAs(String name)
	{
		ArrayList<NFA> tmp = new ArrayList<NFA>();

		for (int i = 0; i < IdentifierNFANames.size(); i++)
		{
			if (IdentifierNFANames.get(i).compareTo(name) == 0)
			{
				tmp.add(IdentifierNFAs.get(i));
			}
		}

		return tmp.toArray(new NFA[0]);
	}
	
	
	public String[] getIdentifierNFAKeys()
	{
		ArrayList<String> tmp = new ArrayList<String>();
		
		for (String key : IdentifierNFANames)
		{
			tmp.add(key);
		}
		
		return tmp.toArray(new String[0]);
	}
	
	
	
	public NFA[] getIdentifierNFAs()
	{
		ArrayList<NFA> tmp = new ArrayList<NFA>();
		
		for (String key : IdentifierNFANames)
		{
			NFA nfas[] = getIdentifierNFAs(key);
			
			for (NFA nfa : nfas)
			{
				tmp.add(nfa);
			}
		}
		
		return (NFA[]) tmp.toArray();
	}


	public String toString()
	{
		return "Character Classes:\t" + this.CharacterClasses.toString() + "\nIdentifers:\t\t" + this.Identifiers.toString();
	}
}
