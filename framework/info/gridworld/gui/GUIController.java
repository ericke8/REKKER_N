/*
 * AP(r) Computer Science GridWorld Case Study: Copyright(c) 2002-2006 College
 * Entrance Examination Board (http://www.collegeboard.com).
 *
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * @author Julie Zelenski
 * 
 * @author Cay Horstmann
 */

package info.gridworld.gui;

import info.gridworld.character.CharacterU;
import info.gridworld.grid.*;
import info.gridworld.world.World;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.*;
import info.gridworld.grid.Location;


/**
 * The GUIController controls the behavior in a WorldFrame. <br />
 * This code is not tested on the AP CS A and AB exams. It contains GUI
 * implementation details that are not intended to be understood by AP CS
 * students.
 */

public class GUIController
{
    public static final int INDEFINITE = 0, FIXED_STEPS = 1, PROMPT_STEPS = 2;

    private static final int MIN_DELAY_MSECS = 10, MAX_DELAY_MSECS = 1000;

    private static final int INITIAL_DELAY = MIN_DELAY_MSECS + ( MAX_DELAY_MSECS - MIN_DELAY_MSECS ) / 2;

    private Timer timer;

    private JButton moveButton, attackerButton, targetButton, cancelButton;

    private JButton newGameButton;

    private JComponent controlPanel;

    private GridPanel display;

    private WorldFrame parentFrame;

    private ResourceBundle resources;

    private DisplayMap displayMap;

    private CharacterU attacker;

    private CharacterU target;

    private CharacterU mover;

    private Location base;
    // private boolean running;

    private Set<Class> occupantClasses;


    /**
     * Creates a new controller tied to the specified display and gui frame.
     * 
     * @param parent
     *            the frame for the world window
     * @param disp
     *            the panel that displays the grid
     * @param displayMap
     *            the map for occupant displays
     * @param res
     *            the resource bundle for message display
     */
    public GUIController( WorldFrame parent, GridPanel disp, DisplayMap displayMap, ResourceBundle res )
    {
        resources = res;
        display = disp;
        parentFrame = parent;
        this.displayMap = displayMap;
        makeControls();

        occupantClasses = new TreeSet<Class>( new Comparator<Class>()
        {
            public int compare( Class a, Class b )
            {
                return a.getName().compareTo( b.getName() );
            }
        } );

        World world = parentFrame.getWorld();
        Grid gr = world.getGrid();
        for ( Location loc : gr.getOccupiedLocations() )
            addOccupant( gr.get( loc ) );
        for ( String name : world.getOccupantClasses() )
            try
            {
                occupantClasses.add( Class.forName( name ) );
            }
            catch ( Exception ex )
            {
                ex.printStackTrace();
            }

        timer = new Timer( INITIAL_DELAY, new ActionListener()
        {
            public void actionPerformed( ActionEvent evt )
            {
                attacker();
            }
        } );

        display.addMouseListener( new MouseAdapter()
        {
            public void mousePressed( MouseEvent evt )
            {
                Grid gr = parentFrame.getWorld().getGrid();
                Location loc = display.locationForPoint( evt.getPoint() );
                if ( loc != null && gr.isValid( loc ) )
                {
                    display.setCurrentLocation( loc );
                    locationClicked();
                }
            }
        } );
        // stop();
    }


    /**
     * Selects the attacker.
     */
    public void attacker()
    {
        parentFrame.getWorld().step();
        Location l = display.getCurrentLocation();
        if ( parentFrame.getWorld().getGrid().get( l ) != null )
        {
            targetButton.setEnabled( true );
            attacker = parentFrame.getWorld().getGrid().get( l );
            parentFrame.getWorld().setMessage( "Attacker Selected: Unit at " + l.toString() );
        }
    }


    /**
     * Adds an occupant to the grid.
     * 
     * @param occupant
     *            occupant
     */
    private void addOccupant( CharacterU occupant )
    {
        Class cl = occupant.getClass();
        do
        {
            if ( ( cl.getModifiers() & Modifier.ABSTRACT ) == 0 )
                occupantClasses.add( cl );
            cl = cl.getSuperclass();
        } while ( cl != Object.class );
    }


    /**
     * Selects the target.
     */
    public void target()
    {
        Location l = display.getCurrentLocation();
        if ( parentFrame.getWorld().getGrid().get( l ) != null )
        {
            target = parentFrame.getWorld().getGrid().get( l );
            parentFrame.getWorld().setMessage( "Target Selected: Unit at " + l.toString() );
            executeAttack();
        }
    }


    /**
     * Executes the attack queued
     */
    public void executeAttack()
    {
        if ( target != null && attacker != null )
        {
            if ( ( Math.abs( target.getLocation().getRow() - attacker.getLocation().getRow() ) <= attacker
                .attackRange() )

                && ( Math.abs( target.getLocation().getCol() - attacker.getLocation().getCol() ) <= attacker
                    .attackRange() ) )
            {
                if ( attacker.getTeam() != target.getTeam()
                    || ( attacker.getUnitType().equals( new String( "Healer" ) ) ) )
                {
                    if ( attacker.getUnitType().equals( new String( "Healer" ) )
                        && ( attacker.getTeam() == target.getTeam() ) )
                    {
                        target.healthAdjust( attacker.getAtk() );
                        parentFrame.getWorld().setMessage(
                            "Healer healed the " + target.getUnitType() + " for " + attacker.getAtk() + " health." );
                    }
                    else if ( target.getDef() - attacker.getAtk() < 0 )
                    {
                        target.healthAdjust( target.getDef() - attacker.getAtk() );
                        parentFrame.getWorld().setMessage( attacker.getUnitType() + " damaged the "
                            + target.getUnitType() + " for " + ( attacker.getAtk() - target.getDef() ) + " damage." );
                    }
                    else
                    {
                        parentFrame.getWorld().setMessage(
                            attacker.getUnitType() + " damaged the " + target.getUnitType() + " for 0 damage." );
                    }
                    attacker = null;
                    target = null;
                    targetButton.setEnabled( false );
                }
                else
                {
                    parentFrame.getWorld().setMessage( "Invalid target: check team" );
                    attacker = null;
                    target = null;
                    targetButton.setEnabled( false );
                }
            }
            else
            {
                attacker = null;
                target = null;
                targetButton.setEnabled( false );
                parentFrame.getWorld().setMessage( "Out of Range." );
            }
        }
        else
        {
            attacker = null;
            target = null;
            targetButton.setEnabled( false );
            parentFrame.getWorld().setMessage( "Selection invalid." );
            throw new IllegalArgumentException( "Selection invalid." );
        }
    }


    /**
     * Stops any existing timer currently carrying out steps.
     */
    public void cancel()
    {
        attacker = null;
        target = null;
        targetButton.setEnabled( false );
        base = null;
        parentFrame.getWorld().setMessage( new String( "Actions cancelled." ) );
    }


    /**
     * Moves the character to another space
     */
    public void move()
    {
        Location l = display.getCurrentLocation();
        if ( base != null )
        {
            if ( parentFrame.getWorld().getGrid().get( l ) == null )
            {
                if ( ( Math.abs( base.getRow() - l.getRow() ) <= 2 )

                    && Math.abs( base.getCol() - l.getCol() ) <= 2 )
                {
                    CharacterU c = parentFrame.getWorld().getGrid().get( base );
                    c.removeSelfFromGrid();
                    c.putSelfInGrid( parentFrame.getWorld().getGrid(), l );
                    parentFrame.repaint();
                    base = null;
                }
                else
                {
                    parentFrame.getWorld().setMessage( new String( "Selection outside of movement range." ) );
                    base = null;
                }
            }
            else
            {
                parentFrame.getWorld().setMessage( new String( "Destination already occupied." ) );
                base = null;
            }
        }
        else if ( parentFrame.getWorld().getGrid().get( l ) != null )
        {
            base = l;
        }
    }


    /**
     * Ends the turn.
     */
    public void newGame()
    {
        parentFrame.getWorld().setMessage( "You have ended your turn." );
    }


    /**
     * Builds the panel with the various controls (buttons and slider).
     */
    private void makeControls()
    {
        controlPanel = new JPanel();
        moveButton = new JButton( new String( "Move" ) );
        attackerButton = new JButton( new String( "Attacker" ) );
        targetButton = new JButton( new String( "Target" ) );
        cancelButton = new JButton( new String( "Cancel" ) );
        newGameButton = new JButton( new String( "End Turn" ) );

        controlPanel.setLayout( new BoxLayout( controlPanel, BoxLayout.X_AXIS ) );
        controlPanel.setBorder( BorderFactory.createEtchedBorder() );

        Dimension spacer = new Dimension( 10, attackerButton.getPreferredSize().height + 10 );
        Dimension largeSpacer = new Dimension( 200, attackerButton.getPreferredSize().height + 10 );

        controlPanel.add( Box.createRigidArea( spacer ) );

        controlPanel.add( moveButton );
        controlPanel.add( Box.createRigidArea( spacer ) );
        controlPanel.add( attackerButton );
        controlPanel.add( Box.createRigidArea( spacer ) );
        controlPanel.add( targetButton );
        controlPanel.add( Box.createRigidArea( spacer ) );
        controlPanel.add( cancelButton );
        controlPanel.add( Box.createRigidArea( largeSpacer ) );
        controlPanel.add( newGameButton );
        moveButton.setEnabled( true );
        newGameButton.setEnabled( true );
        targetButton.setEnabled( false );
        attackerButton.setEnabled( true );
        cancelButton.setEnabled( true );

        moveButton.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                move();
            }
        } );
        attackerButton.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                attacker();
            }
        } );
        targetButton.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                target();
            }
        } );
        cancelButton.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {

                cancel();
            }
        } );
        newGameButton.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                newGame();
            }
        } );
    }


    /**
     * Returns the panel containing the controls.
     * 
     * @return the control panel
     */
    public JComponent controlPanel()
    {
        return controlPanel;
    }


    /**
     * Callback on mousePressed when editing a grid.
     */
    private void locationClicked()
    {
        World world = parentFrame.getWorld();
        Location loc = display.getCurrentLocation();
        if ( loc != null && !world.locationClicked( loc ) )
            editLocation();
        parentFrame.repaint();
    }


    /**
     * Edits the contents of the current location, by displaying the constructor
     * or method menu.
     */
    public void editLocation()
    {
        World world = parentFrame.getWorld();

        Location loc = display.getCurrentLocation();
        if ( loc != null )
        {
            CharacterU occupant = world.getGrid().get( loc );
            if ( occupant == null )
            {
                Point p = display.pointForLocation( loc );
            }
            else
            {
                Point p = display.pointForLocation( loc );
            }
        }
        parentFrame.repaint();
    }


    /**
     * Edits the contents of the current location, by displaying the constructor
     * or method menu.
     */
    public void deleteLocation()
    {
        World world = parentFrame.getWorld();
        Location loc = display.getCurrentLocation();
        if ( loc != null )
        {
            world.remove( loc );
            parentFrame.repaint();
        }
    }
}
