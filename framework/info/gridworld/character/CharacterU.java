package info.gridworld.character;

import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;


/**
 * A base template for all of the different characters. Includes methods to
 * access back-end data structures as well as GUI elements from the user input.
 *
 * @author Eric K
 * @version May 29, 2017
 * @author Period: 1
 * @author Assignment: NetTileGW
 *
 * @author Sources: None
 */
public abstract class CharacterU
{
    /**
     * Grid character is in
     */
    protected Grid grid;

    /**
     * Attack stat
     */
    protected int atk;

    /**
     * Defense stat
     */
    protected int def;

    /**
     * maximum health limit
     */
    protected int maxHealth;

    /**
     * Health stat
     */
    protected int health;

    /**
     * range of character
     */
    protected int range;

    /**
     * location of character
     */
    protected Location location;

    /**
     * team number
     */
    protected int team;


    /**
     * gets unit type
     */
    public abstract String getUnitType();


    /**
     * default constructor
     */
    public CharacterU()
    {
        grid = null;
        location = null;
    }


    /**
     * constructor with team input
     */
    public CharacterU( int i )
    {
        grid = null;
        location = null;
        team = i;
    }


    /**
     * sets the health to a given value
     * 
     * @param i
     *            value
     * @return whether it succeeded
     */
    public boolean setHealth( int i )
    {
        if ( i >= 0 )
        {
            health = i;
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     * Gets the grid in which this character is located.
     * 
     * @return the grid of this character, or <code>null</code> if this
     *         character is not contained in a grid
     */
    public Grid getGrid()
    {
        return grid;
    }


    /**
     * gets the team number
     * 
     * @return team number
     */
    public int getTeam()
    {
        return team;
    }


    /**
     * sets the team number of a character to a given number
     * 
     * @param i
     *            team number
     */
    public void setTeam( int i )
    {
        team = i;
    }


    /**
     * sets the location of the character
     * 
     * @param loc
     *            location
     */
    public void setLocation( Location loc )
    {
        location = loc;
    }


    /**
     * sets the grid of the character
     * 
     * @param gr
     *            grid
     */
    public void setGrid( Grid gr )
    {
        grid = gr;
    }


    /**
     * Puts this character into a grid. If there is another character at the
     * given location, it is removed. <br />
     * Precondition: (1) This character is not contained in a grid (2)
     * <code>loc</code> is valid in <code>gr</code>
     * 
     * @param gr
     *            the grid into which this character should be placed
     * @param loc
     *            the location into which the character should be placed
     */
    public void putSelfInGrid( Grid gr, Location loc )
    {
        if ( grid != null )
            throw new IllegalStateException( "This character is already contained in a grid. " );

        CharacterU character = gr.get( loc );
        if ( character != null )
            character.removeSelfFromGrid();
        gr.put( loc, this );
        this.grid = gr;
        this.location = loc;
    }


    /**
     * Moves this character to a new location. If there is another character at
     * the given location, it is removed. <br />
     * Precondition: (1) This character is contained in a grid (2)
     * <code>newLocation</code> is valid in the grid of this character
     * 
     * @param newLocation
     *            the new location
     * @return newLocation new location
     */
    public Location moveTo( Location newLocation )// unused
    {
        if ( grid == null )
            throw new IllegalStateException( "This character is not in a grid." );
        if ( grid.get( location ) != this )
            throw new IllegalStateException( "The grid contains a different character at location " + location + "." );
        if ( !grid.isValid( newLocation ) )
            throw new IllegalArgumentException( "Location " + newLocation + " is not valid." );

        if ( newLocation.equals( location ) )
            return newLocation;
        grid.remove( location );
        // if ( grid.get( newLocation ) != null )
        {
            // throw new IllegalArgumentException( "Location " + newLocation + "
            // is not valid." );
            CharacterU other = grid.get( newLocation );
            if ( other != null )
                other.removeSelfFromGrid();
        }
        location = newLocation;
        grid.put( location, this );
        return newLocation;
    }


    /**
     * Removes this character from its grid. <br />
     * Precondition: This character is contained in a grid
     */
    public void removeSelfFromGrid()
    {
        if ( grid == null )
            throw new IllegalStateException( "This character is not contained in a grid. (NULL GRID ERROR)" );
        if ( grid.get( location ) != this )
            throw new IllegalStateException( "The grid contains a different character at location " + location + "." );

        grid.remove( location );
        grid = null;
        location = null;
    }


    /**
     * gets the location of the character
     * 
     * @return location
     */
    public Location getLocation()
    {
        return location;
    }


    /**
     * gets the range of the character
     * 
     * @return attack range
     */
    public int attackRange()
    {
        return range;
    }


    /**
     * gets the attack stat of the character
     * 
     * @return attack
     */
    public int getAtk()
    {
        return atk;
    }


    /**
     * gets the defense stat of the character
     * 
     * @return defense
     */
    public int getDef()
    {
        return def;
    }


    /**
     * gets the health stat of the character
     * 
     * @return health
     */
    public int getHealth()
    {
        return health;
    }


    /**
     * gets maximum health of character
     * 
     * @return max health
     */
    public int getMaxHealth()
    {
        return maxHealth;
    }


    /**
     * adjusts the health and removes character if health<0
     * 
     * @param adjust
     *            how much to adjust by
     * @return new health
     */
    public int healthAdjust( int adjust )
    {
        health += adjust;
        if ( health > maxHealth )
        {
            health = maxHealth;
        }
        if ( health <= 0 )
        {
            this.removeSelfFromGrid();
        }
        return health;
    }


    /**
     * Creates a string that describes this character.
     * 
     * @return a string with the location of this character
     */
    public String toString()
    {
        return getClass().getName() + "[location=" + location + "]";
    }

}