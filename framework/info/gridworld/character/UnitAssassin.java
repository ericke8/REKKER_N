package info.gridworld.character;

import info.gridworld.grid.Location;


/**
 * The class representing an Assassin character in REKKER.
 *
 * @author Eric K, Rohan M, Kiran A
 * @version May 29, 2017
 * @author Period: 1
 * @author Assignment: NetTileGW
 *
 * @author Sources: None
 */
public class UnitAssassin extends CharacterU
{
    /**
     * constructor with team
     * 
     * @param i
     *            team number
     */
    public UnitAssassin( int i )
    {

        super();
        health = 5;
        maxHealth = 5;
        atk = 30;
        def = 5;
        range = 1;
        team = i;
    }


    @Override
    public String getUnitType()
    {
        return new String( "Assassin" );
    }

}