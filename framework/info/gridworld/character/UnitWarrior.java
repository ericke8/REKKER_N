package info.gridworld.character;

/**
 * The class representing an Warrior character in REKKER.
 *
 * @author Eric K, Rohan M, Kiran A
 * @version May 29, 2017
 * @author Period: 1
 * @author Assignment: NetTileGW
 *
 * @author Sources: None
 */
public class UnitWarrior extends CharacterU
{
    /**
     * constructor with team
     * 
     * @param i
     *            team number
     */
    public UnitWarrior( int i )
    {
        super();
        health = 30;
        maxHealth = 30;
        atk = 20;
        def = 8;
        range = 1;
        team = i;
    }


    @Override
    public String getUnitType()
    {
        return new String( "Warrior" );
    }

}