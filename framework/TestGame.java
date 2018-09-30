
/*
 * AP(r) Computer Science GridWorld Case Study: Copyright(c) 2005-2006 Cay S.
 * Horstmann (http://horstmann.com)
 *
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * @author Cay Horstmann
 */

import info.gridworld.grid.BoundedGrid;
import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;
import info.gridworld.world.World;

import info.gridworld.character.*;
import info.gridworld.gui.*;

import java.awt.Color;
import java.util.*;

import javax.swing.*;


/**
 * The class that creates the game of REKKER.
 *
 * @author Eric K, Rohan M, Kiran A
 * @version May 29, 2017
 * @author Period: 1
 * @author Assignment: NetTileGW
 *
 * @author Sources: None
 */
public class TestGame extends World implements ChatDisplay
{
    private HashMap<String, CharacterU> team1 = new HashMap<String, CharacterU>();

    private HashMap<String, CharacterU> team2 = new HashMap<String, CharacterU>();

    /** Default port to connect to on remote hosts */
    public static final int DEFAULT_PORT = 1337;

    private int port = DEFAULT_PORT;

    private String ip;

    /** Object that performs all networking and IO */
    protected ChatConnectionHandler networker;

    /** Data model for connections list */
    protected DefaultListModel connModel;

    /** List of active connections */
    protected JList connections;

    private boolean testing;


    /**
     * constructor for game
     */
    public TestGame( boolean test )
    {
        testing = test;
        // setSeed(15);
        Scanner scan = new Scanner( System.in );
        connModel = new DefaultListModel();
        connections = new JList( connModel );

        team1.put( "Assassin", new UnitAssassin( 1 ) );
        team1.put( "Healer", new UnitHealer( 1 ) );
        team1.put( "Vanguard", new UnitVanguard( 1 ) );
        team1.put( "Archer", new UnitArcher( 1 ) );
        team1.put( "Warrior", new UnitWarrior( 1 ) );

        team2.put( "Assassin", new UnitAssassin( 2 ) );
        team2.put( "Healer", new UnitHealer( 2 ) );
        team2.put( "Vanguard", new UnitVanguard( 2 ) );
        team2.put( "Archer", new UnitArcher( 2 ) );
        team2.put( "Warrior", new UnitWarrior( 2 ) );

        add( new Location( 2, 1 ), team1.get( "Vanguard" ) );
        add( new Location( 1, 1 ), team1.get( "Archer" ) );
        add( new Location( 0, 1 ), team1.get( "Healer" ) );
        add( new Location( 1, 2 ), team1.get( "Warrior" ) );
        add( new Location( 1, 0 ), team1.get( "Assassin" ) );

        add( new Location( 7, 8 ), team2.get( "Vanguard" ) );
        add( new Location( 8, 8 ), team2.get( "Archer" ) );
        add( new Location( 9, 8 ), team2.get( "Healer" ) );
        add( new Location( 8, 7 ), team2.get( "Warrior" ) );
        add( new Location( 8, 9 ), team2.get( "Assassin" ) );
        if ( testing )
        {
            ip = "127.0.0.1";
            port = 1337;

        }
        if ( testing != true )
        {
            System.out.print( "Enter IP address: " );
            ip = scan.nextLine();

            System.out.print( "Enter listen port: " );
            port = scan.nextInt();
        }

        // create a chat networking object to peform I/O
        networker = new ChatConnectionHandler( this, port );
        if ( testing != true )
        {
            System.out.print( "Enter talk port: " );
            port = scan.nextInt();
        }
        if ( testing )
        {
            port = 1338;
        }

        connect();
    }


    /**
     * overrides chatMessage from ChatDisplay, takes encoded string and
     * interprets it
     * 
     * @param name
     *            socket name
     * @param message
     *            message
     */
    public void chatMessage( SocketName name, String message )
    {
        // encoded as unitType,Row#,Col#,Team,Health

        String[] arrayOcc = message.split( ";" );

        Grid newGrid = new BoundedGrid( gr.getNumRows(), gr.getNumCols() );
        for ( String s : arrayOcc )
        {
            String[] charInfo = s.split( "," );
            Location l = new Location( Integer.parseInt( charInfo[1] ), Integer.parseInt( charInfo[2] ) );
            int team = Integer.parseInt( charInfo[3] );
            int health = Integer.parseInt( charInfo[4] );
            if ( charInfo[0].equals( "1" ) )
            {
                CharacterU c = new UnitArcher( team );
                newGrid.put( l, c );
                c.setHealth( health );
            }
            else if ( charInfo[0].equals( "2" ) )
            {
                CharacterU c = new UnitAssassin( team );
                newGrid.put( l, c );
                c.setHealth( health );
            }
            else if ( charInfo[0].equals( "3" ) )
            {
                CharacterU c = new UnitHealer( team );
                newGrid.put( l, c );
                c.setHealth( health );
            }
            else if ( charInfo[0].equals( "4" ) )
            {
                CharacterU c = new UnitVanguard( team );
                newGrid.put( l, c );
                c.setHealth( health );
            }
            else if ( charInfo[0].equals( "5" ) )
            {
                CharacterU c = new UnitWarrior( team );
                newGrid.put( l, c );
                c.setHealth( health );
            }
            setMessage( "Positions synchronized" );
        }

        if ( !newGrid.equals( gr ) )
        {
            gr = newGrid;
            this.frame.getDisplay().setGrid( gr );

            repaint();
        }
    }


    /**
     * method activates when a location is clicked
     * 
     * @param loc
     *            location
     * 
     */
    public boolean locationClicked( Location loc )
    {
        locationClicked( loc, true );
        return true;
    }


    /**
     * Sends an encoded message depending on whether the source is local or not
     * 
     * @param loc
     *            location
     * @param local
     *            which side
     */
    public void locationClicked( Location loc, boolean local )
    {
        if ( local )
        {
            // send message
            String result = "";
            int unitType = 0;
            for ( Location l : gr.getOccupiedLocations() )
            {
                CharacterU c = gr.get( l );
                if ( c.getUnitType().equals( "Archer" ) )
                {
                    unitType = 1;
                }
                else if ( c.getUnitType().equals( "Assassin" ) )
                {
                    unitType = 2;
                }
                else if ( c.getUnitType().equals( "Healer" ) )
                {
                    unitType = 3;
                }
                else if ( c.getUnitType().equals( "Vanguard" ) )
                {
                    unitType = 4;
                }
                else if ( c.getUnitType().equals( "Warrior" ) )
                {
                    unitType = 5;
                }
                else
                {
                    unitType = 0;
                }
                result += ( unitType + "," + l.getRow() + "," + l.getCol() + "," + c.getTeam() + "," + c.getHealth()
                    + ";" );
            }
            networker.send( result );
        }
        Grid current = getGrid();
        CharacterU t = current.get( loc );
        if ( t != null )
        {
            setMessage(
                "Team " + t.getTeam() + " " + t.getUnitType() + " at " + loc.toString() + "     HP: " + t.getHealth()
                    + "     Atk: " + t.getAtk() + "     Def: " + t.getDef() + "     Range: " + t.attackRange() );
            repaint();
        }
        else
        {
            int t1 = 0;
            int t2 = 0;
            for ( Location l : this.getGrid().getOccupiedLocations() )
            {
                if ( this.getGrid().get( l ) != null )
                {
                    if ( this.getGrid().get( l ).getTeam() == 1 )
                    {
                        t1++;
                    }
                    else if ( this.getGrid().get( l ).getTeam() == 2 )
                    {
                        t2++;
                    }
                }

            }
            if ( t1 == 0 )
            {
                setMessage( "Player 2 wins!" );
            }
            else if ( t2 == 0 )
            {
                setMessage( "Player 1 wins!" );
            }
        }

    }


    /**
     * Helper method to read inputs from GUI components and create a new socket
     * connection.
     */
    protected void connect()
    {
        try
        {
            SocketName sock = new SocketName( ip, port + "", "port_" + port );

            if ( connModel.contains( sock ) )
            {
                statusMessage( "Cannot connect to " + sock + ": already connected" );
            }
            else
            {
                networker.connect( sock );
                statusMessage( "Connected to " + sock );
            }
        }
        catch ( IllegalArgumentException iae )
        {
            statusMessage( "Cannot connect: " + iae.getMessage() );
        }

    }


    /**
     * Helper method to read inputs from GUI components and destroy an existing
     * socket connection.
     */
    protected void disconnect()
    {
        int index = connections.getSelectedIndex();
        if ( index > -1 )
        {
            SocketName dead = (SocketName)( connModel.elementAt( index ) );

            networker.disconnect( dead );
        }
    }


    /**
     * @see ChatDisplay#createSocket
     */
    public synchronized void createSocket( SocketName name )
    {
        connModel.addElement( name );
    }


    public void statusMessage( String message )
    {
        setMessage( message );
    }


    /**
     * @see ChatDisplay#destroySocket
     */
    public void destroySocket( SocketName name )
    {
        if ( connModel.contains( name ) )
        {
            connModel.removeElement( name );
        }
    }


    /**
     * Runs an instance of TestGame
     * 
     * @param args
     *            arguments
     */
    public static void main( String[] args )
    {
        new TestGame( true ).show();
    }
}
