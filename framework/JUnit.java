import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.net.Socket;

import org.junit.Test;

import info.gridworld.character.CharacterU;
import info.gridworld.character.UnitArcher;
import info.gridworld.character.UnitAssassin;
import info.gridworld.character.UnitHealer;
import info.gridworld.character.UnitVanguard;
import info.gridworld.character.UnitWarrior;
import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;


/**
 * A class to test the methods of REKKER.
 *
 * @author Eric K, Rohan M, Kiran A
 * @version May 29, 2017
 * @author Period: 1
 * @author Assignment: NetTileGW
 *
 * @author Sources: None
 */
public class JUnit
{
    /**
     * Tests the Assassin character.
     */
    @Test
    public void getAssassin()
    {
        UnitAssassin test = new UnitAssassin( 1 );

        assertEquals( "Character type must be assassin", "Assassin", test.getUnitType() );
        assertEquals( "Initial health must be 5", 5, test.getHealth() );
        assertEquals( "Max health must be 5", 5, test.getMaxHealth() );
        assertEquals( "Attack must be 30", 30, test.getAtk() );
        assertEquals( "Defense must be 5", 5, test.getDef() );
        assertEquals( "Range must be 1", 1, test.attackRange() );
        assertEquals( "Team must be 1", 1, test.getTeam() );

    }


    /**
     * Tests the Healer character.
     */
    @Test
    public void getHealer()
    {
        UnitHealer test = new UnitHealer( 1 );

        assertEquals( "Character type must be healer", "Healer", test.getUnitType() );
        assertEquals( "Initial health must be 15", 15, test.getHealth() );
        assertEquals( "Max health must be 15", 15, test.getMaxHealth() );
        assertEquals( "Attack must be 7", 7, test.getAtk() );
        assertEquals( "Defense must be 3", 3, test.getDef() );
        assertEquals( "Range must be 2", 2, test.attackRange() );
        assertEquals( "Team must be 1", 1, test.getTeam() );

    }


    /**
     * Tests the Archer character.
     */
    @Test
    public void getArcher()
    {
        UnitArcher test = new UnitArcher( 1 );

        assertEquals( "Character type must be archer", "Archer", test.getUnitType() );
        assertEquals( "Initial health must be 15", 15, test.getHealth() );
        assertEquals( "Max health must be 15", 15, test.getMaxHealth() );
        assertEquals( "Attack must be 18", 18, test.getAtk() );
        assertEquals( "Defense must be 5", 5, test.getDef() );
        assertEquals( "Range must be 2", 2, test.attackRange() );
        assertEquals( "Team must be 1", 1, test.getTeam() );

    }


    /**
     * Tests the Vanguard character.
     */
    @Test
    public void getVanguard()
    {
        UnitVanguard test = new UnitVanguard( 1 );

        assertEquals( "Character type must be vanguard", "Vanguard", test.getUnitType() );
        assertEquals( "Initial health must be 50", 50, test.getHealth() );
        assertEquals( "Max health must be 50", 50, test.getMaxHealth() );

    }


    /**
     * Tests the Warrior character.
     */
    @Test
    public void getWarrior()
    {
        UnitWarrior test = new UnitWarrior( 1 );

        assertEquals( "Character type must be warrior", "Warrior", test.getUnitType() );
        assertEquals( "Initial health must be 30", 30, test.getHealth() );
        assertEquals( "Attack must be 20", 20, test.getAtk() );
        assertEquals( "Defense must be 8", 8, test.getDef() );
        assertEquals( "Range must be 1", 1, test.attackRange() );
        assertEquals( "Team must be 1", 1, test.getTeam() );
    }


    /**
     * Tests the LocationClicked method.
     */
    @Test
    public void checkLocationClicked()
    {
        TestGame test = new TestGame( true );
        Location loc = new Location( 1, 1 );

        assertEquals( "Location clicked must be valid", true, test.locationClicked( loc ) );
        assertEquals( "Object in location needs to be displayed",
            "Team 1 Archer at (1, 1)     HP: 15     Atk: 20     Def: 5     Range: 2",
            test.getMessage() );
    }


    /**
     * Tests the construction of a TestGame.
     */
    @Test
    public void checkTestGameCreation()
    {
        TestGame test = new TestGame( true );
        Location loc = new Location( 1, 1 );
        assertNotNull( test );
    }

}
