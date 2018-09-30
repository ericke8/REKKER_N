package info.gridworld.character;

/**
 * The class representing an Healer character in REKKER.
 *
 * @author Eric K, Rohan M, Kiran A
 * @version May 29, 2017
 * @author Period: 1
 * @author Assignment: NetTileGW
 *
 * @author Sources: None
 */
public class UnitHealer extends CharacterU
{
    /**
     * constructor with team
     * 
     * @param i
     *            team number
     */
    public UnitHealer( int i )
    {
        super();
        health = 15;
        maxHealth = 15;
        atk = 7;
        def = 3;
        range = 2;
        team = i;
    }


    @Override
    public String getUnitType()
    {
        return new String( "Healer" );
    }

}