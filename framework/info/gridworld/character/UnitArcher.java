package info.gridworld.character;

/**
 * The class representing an Archer character in REKKER.
 *
 * @author Eric K, Rohan M, Kiran A
 * @version May 29, 2017
 * @author Period: 1
 * @author Assignment: NetTileGW
 *
 * @author Sources: None
 */
public class UnitArcher extends CharacterU
{

    /**
     * constructor with team
     * 
     * @param i
     *            team number
     */
    public UnitArcher( int i )
    {
        super();
        health = 15;
        maxHealth = 15;
        atk = 18;
        def = 5;
        range = 2;
        team = i;
    }


    @Override
    public String getUnitType()
    {
        return new String( "Archer" );
    }

}