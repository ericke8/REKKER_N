package info.gridworld.character;

/**
 * The class representing an Vanguard character in REKKER.
 *
 * @author Eric K, Rohan M, Kiran A
 * @version May 29, 2017
 * @author Period: 1
 * @author Assignment: NetTileGW
 *
 * @author Sources: None
 */
public class UnitVanguard extends CharacterU
{
    /**
     * constructor with team
     * 
     * @param i
     *            team number
     */
    public UnitVanguard( int i )
    {
        super();
        health = 50;
        maxHealth = 50;
        atk = 12;
        def = 10;
        range = 1;
        team = i;
    }


    @Override
    public String getUnitType()
    {
        return new String( "Vanguard" );
    }

}