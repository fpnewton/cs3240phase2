/**
 * Mutable Grammar Class
 * 
 * Mutable data structure to hold grammar character class regular expressions and identifiers.
 * 
 * @author	Fraser P. Newton
 * @date	October 29, 2011
 */

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;


public class MutableGrammar extends Grammar
{
	/**
	 * Constructor.
	 * Instantiate a <code>MutableGrammar</code> object with empty character class and identifier dictionaries.
	 */
	public MutableGrammar()
	{
		super(new Hashtable<String, String>(), new ArrayList<ArrayList<String>>());
	}


	/**
	 * Constructor.
	 * Instantiate a new <code>MutableGrammar</code> object from a character class and an identifier dictionary.
	 * 
	 * @param characterClasses
	 *            Character class dictionary of type <code>Dictionary<String, String></code>
	 * @param identifiers
	 *            Identifier dictionary of type <code>Dictionary<String, String></code>
	 */
	public MutableGrammar(Dictionary<String, String> characterClasses, ArrayList<ArrayList<String>> identifiers)
	{
		super(characterClasses, identifiers);
	}


	/**
	 * Gets the character class dictionary.
	 * 
	 * @return
	 * 		A pointer to the character class dictionary of type <code>Dictionary<String, String></code>
	 */
	public Dictionary<String, String> getCharacterClasses()
	{
		return super.CharacterClasses;
	}


	/**
	 * Gets the identifier dictionary.
	 * 
	 * @return
	 * 		A pointer to the identiier dictionary of type <code>Dictionary<String, String></code>
	 */
	public ArrayList<ArrayList<String>> getIdentifiers()
	{
		return super.Identifiers;
	}
	
	
	/**
	 * Adds a character class regular expression with key type <code>String</code> and value type <code>String</code> to the character class dictionary.
	 * 
	 * @param characterClassName
	 * 			The name of the character class to add of type <code>String</code>
	 * @param characterClassRegex
	 * 			The regular expression of the character class to add of type <code>String</code>
	 */
	public void addCharacterClass(String characterClassName, String characterClassRegex)
	{
		super.CharacterClasses.put(characterClassName, characterClassRegex);
	}
	
	
	/**
	 * Adds an identifier regular expression with key type <code>String</code> and value type <code>String</code> to the identifier dictionary.
	 * 
	 * @param identifierName
	 * 			The name of the identifer to add of type <code>String</code>
	 * @param identifierRegex
	 * 			The regular expression of the identifer to add of type <code>String</code>
	 */
	public void addIdentifier(String identifierName, String identifierRegex)
	{
		ArrayList<String> contents = new ArrayList<String>();
		
		contents.add(identifierName);
		contents.add(identifierRegex);
		
		super.Identifiers.add(contents);
	}
	
	
	/**
	 * Removes a character class by the character class's associated name.
	 * 
	 * @param characterClassName
	 * 			The name of the character class to remove of type <code>String</code>
	 * @return
	 * 		The regular expression of the removed character class, of type <code>String</code>
	 */
	public String removeCharacterClass(String characterClassName)
	{
		return super.CharacterClasses.remove(characterClassName);
	}
	
	
	/**
	 * Removes an identifier by the identifier's associated name.
	 * 
	 * @param identifierName
	 * 			The name of the identifier to remove of type <code>String</code>
	 * @return
	 * 		The regular expression of the removed identifier, of type <code>String</code>
	 */
	public String removeIdentifier(String identifierName)
	{
		String removed = null;
		
		for (int i = 0; i < this.getNumIdentifiers(); i++)
		{
			if (super.getIdentifier(i)[0].compareTo(identifierName) == 0)
			{
				removed = super.getIdentifiers().remove(i).get(0);
				break;
			}
		}
		
		return removed;
	}
	
	
	/**
	 * Clears the character classes dictionary.
	 */
	public void clearCharacterClasses()
	{
		((Hashtable<String, String>)super.CharacterClasses).clear();
	}
	
	
	/**
	 * Clears the identifiers dictionary.
	 */
	public void clearIdentifiers()
	{
		super.Identifiers.clear();
	}
}
