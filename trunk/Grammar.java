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
	/**
	 * Dictionary of regular expressions for character classes.
	 * 
	 * @key <code>String</code>
	 * @value <code>String</code>
	 */
	protected Dictionary<String, String>	CharacterClasses;
	/**
	 * Dictionary of identifiers and their character classes/regular expressions.
	 * 
	 * @key </code>String</code>
	 * @value </code>String</code>
	 */
	protected ArrayList<ArrayList<String>>	Identifiers;


	/**
	 * Constructor
	 * Instantiates a <code>Grammar</code> object from a <code>MutableGrammar</code> object.
	 * 
	 * @param grammar
	 * 			A <code>MutableGrammar</code> object
	 */
	public Grammar(MutableGrammar grammar)
	{
		this.CharacterClasses	= grammar.getCharacterClasses();
		this.Identifiers		= grammar.getIdentifiers();
	}


	/**
	 * Constructor Instantiates a Grammar object from a dictionary of character classes and a dictionary of identifers.
	 * 
	 * @param characterClasses
	 * 			Dictionary of character class regular expressions of type <code>Dictionary<String, String></code>
	 * @param identifiers
	 * 			Dictionary of identifier character classes/regular expressions of type <code>Dictionary<String, String></code>
	 */
	public Grammar(Dictionary<String, String> characterClasses, ArrayList<ArrayList<String>> identifiers)
	{
		this.CharacterClasses	= characterClasses;
		this.Identifiers		= identifiers;
	}


	/**
	 * Gets a character class regular expression associated with the character class name.
	 * 
	 * @param characterClass
	 * 			Dictionary key of type <code>String</code>. Name of the character class
	 * @return
	 * 		The character class regular expression of type <code>String</code>
	 */
	public String getCharacterClassRegex(String characterClass)
	{
		return (String)this.CharacterClasses.get(characterClass);
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


	/**
	 * Gets an identifier character class/regular expression associated with the name of the identifier.
	 * 
	 * @param identifier
	 * 			Dictionary key of type <code>String</code>. Name of the identifer
	 * @return
	 * 		The character class/regular expression of type <code>String</code>
	 */
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


	/**
	 * Gets the total number of character classes in the grammar.
	 * 
	 * @return
	 * 		Number of character classes of type <code>int</code>
	 */
	public int getNumCharacterClasses()
	{
		return this.CharacterClasses.size();
	}
	
	
	public String toString()
	{
		return "Character Classes:\t" + this.CharacterClasses.toString() + "\nIdentifers:\t\t" + this.Identifiers.toString();
	}
}
