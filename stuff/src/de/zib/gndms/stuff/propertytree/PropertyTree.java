package de.zib.gndms.stuff.propertytree;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

/**
 * A tree struction to access java properties in a herachical manner.
 *
 * Given properties:
 * <PRE>
 *  A=a
 *  A.B.C=c
 *  B=b
 *  E.F=f
 *  E.G=f
 * </PRE>
 * The resulting tree will look like:
 * <PRE>
 *          ()
 *        / | \
 *       A  B  E
 *       |     | \
 *       B     F  G
 *       |
 *       C
 * </PRE>
 * As one may notice the root node is always empty. Calling
 * <TT>getValue</TT> on node A,C,F or G will return the property
 * value.
 *
 * This tree can be accessed like classic properties or only subtrees may taken into account.
 *
 * @author Maik Jorra <jorra@zib.de>
 * @version $Id$
 *
 *          User: mjorra, Date: 06.01.2009, Time: 17:28:32
 *
 * todo: Use custom exceptions instead of standard runtime exceptions.
 */
public class PropertyTree {


    private HashMap<String,PropertyTree> nodes;

    /** Label of the current tree.
     * NOTE: every tree must have a label. If a tree doesn't have one,
     * its considered the root node of the tree which in reality is a forest. */
    private String label;

    /** value of the current tree.
      It haven't to be a leaf to own a value. */
    private String value;


    /**
     * Creates a new Property tree.
     *
     * Which may be extended with new trees by calling the addPath method.
     */
    public PropertyTree( ) {

    }


    /**
     * This constructur is for internal uses only!!.
     * To created a tree with a label call the addPath method on a existing tree.
     *
     * @param label The label of the new tree.
     */
    protected PropertyTree( String label ) {
        this.label = label;
    }


    /**
     * Delivers the property tree at the end of <EM>pth</EM>.
     *
     * @param pth The path to the desired subtree.
     * @return A property tree or null if the path doesn't exist.
     *         If <EM>pth</EM> is null or empty <EM>this</EM> will be delivered.
     */
    public PropertyTree subTree( String pth ) {

        if( pth == null || pth.trim().length() == 0 )
            return this;

        Pair p = splitPath( pth );

        if( nodes != null && nodes.containsKey( p.head ) )
                return nodes.get( p.head ).subTree( p.tail );

        return null;
    }


    /**
     * Addes a new path to the property tree.
     *
     * If path allready exists nothing will change.
     * If the first nodes exist, only new parts will be appended.
     *
     * @param pth The path i.e. a sinlge node or a list of nodes sperated by dotes (.)
     * @return The "leaf" of the newly created path.
     */
    public PropertyTree addPath( String pth ) {

        if( pth == null || pth.trim().length() == 0 )
            return this;

        Pair p = splitPath( pth );

        if( nodes == null || !nodes.containsKey( p.head ) )
            createSubTree( p.head );

        return nodes.get( p.head ).addPath( p.tail );
    }


    /**
     * Addes a property tree a subtree to this tree.
     *
     * If an other property tree exists under tree it will be overwritten.
     *
     * @param key The key under which the tree should be stored.
     *            It must neither start nor end with '.' .
     * @param pt The tree itself.
     * @note pt must no have a label, i.e. it must not be a subtree.
     */
    public void addTree( String key, PropertyTree pt ) {

        int idx = key.lastIndexOf( "." );

        PropertyTree tpt = this;

        if( idx > -1 ) {
            String nkey = key.substring( 0, idx );
            tpt = addPath( nkey );
            key = key.substring( idx + 1, key.length( ) );
        }

        tpt.addSubTree( key, pt, false );
    }


    /**
     * Converts this tree into java properties.
     *
     * For each value which lays on a path in the tree an entry will be generated.
     *
     * @param root If <EM>true</EM> this node is treated as the root of the property document, i.e. its own #value will be suppressed and
     *             the properties of this trees children will not be prefixed with #label.
     * @return A properties object representing this tree.
     */
    public Properties asProperties( boolean root ) {

        Properties this_props = new Properties( );
        addToPropeties( this_props, null, root );
        return this_props;
    }


    /**
     * Like the above method with <EM>root</EM> set to <EM>false</EM>.
     *
     * @return A properties object representing this tree.
     */
    public Properties asProperties( ) {
        return asProperties( false );
    }



    /**
     * Converts this tree into java properties and addes them to an existing property object.
     *
     * For each value which lays on a path in the tree an entry will be generated.
     *
     * @param props The target properties object.
     * @param pre A possible prefix for the property keys. It will be separated by '.' from the property-keys of the subtrees.
     * @param root If <EM>true</EM> this node is treated as the root of the property document, i.e. its own #value will be suppressed and
     *             the properties of this trees children will not be prefixed with #label.
     */
    public void addToPropeties( @NotNull Properties props, String pre, boolean root ) {

        String prefix;
        if( pre != null && pre.trim().length() != 0 ) {
            prefix = pre;
            if(! prefix.endsWith( ".") )
                prefix += ".";
        } else {
            prefix = "";
        }

        if( !root && getLabel( ) != null ) {
            prefix += getLabel( );
            if ( getValue( ) != null )
                props.setProperty( prefix, getValue() );
        }

        if( nodes != null ) {
            Set<String> keys = nodes.keySet();
            for( String key : keys )
                nodes.get( key ).addToPropeties( props, prefix, false );
        }
    }


    /**
     * Sets a property to the tree.
     *
     * This behaves pretty much like Properties.setProperty
     * but it inserts a new path for key, if it doesn't exist.
     * If it exists, it overwrites the value of the last node in the path.
     *
     * @param key The key for the value.
     * @param val The value associated to key.
     */
    public void setProperty( @NotNull String key, String val ) {

        PropertyTree pt = addPath( key );
        pt.setValue( val );
    }


    public String getProperty( String key ) {
        
        PropertyTree pt = subTree( key );
        if( pt != null )
            return pt.getValue( );

        return null;
    }


    public String getProperty( String key, String def ) {
        String prop = getProperty( key );
        
        if( prop != null )
            return prop;

        return def;
    }


    public String getLabel() {
        return label;
    }


    protected void setLabel( String lbl ) {
        
        if( label != null )
            throw new IllegalStateException( "An existing label must not be changed" );

        label = lbl;
    }


    public String getValue() {
        return value;
    }


    public void setValue( String value ) {
        this.value = value;
    }


    /**
     * Internally uses to create corrct subtrees.
     *
     * @param lbl Label of the tree.
     * */
    protected void createSubTree( String lbl ) {
        if ( nodes == null )
            nodes = new HashMap<String,PropertyTree>( );
        
        nodes.put( lbl, new PropertyTree( lbl ) );
    }


    /**
     * Addes a subtree to this tree.
     *
     * @param lbl Label of the tree.
     * @param pt The tree itself.
     * @param chk Check if the tree already exists, throws an Illegal state exception if thats the case.
     */
    protected void addSubTree( String lbl, PropertyTree pt, boolean chk ) {

        if( pt.getLabel() != null )
            throw new IllegalStateException( "Insertion subtrees is not allowed" );

        if( chk && ( nodes != null &&  nodes.containsKey( lbl ) ) )
            throw new IllegalStateException( "A property tree with label " + lbl + " allready exists" );

        if( nodes == null )
            nodes = new HashMap<String,PropertyTree>( );

        pt.setLabel( lbl );
        nodes.put( lbl, pt );
    }


    /**
     * Little helper to handle property keys.
     *
     * Splits a key in the form <TT>A.B.C</TT> in <TT>A</TT> and
     * <TT>B.C</TT>.
     *
     * @param pth The path key to split.
     * @return A pair containing the head and tail of the split op.
     */
    protected Pair splitPath( @NotNull String pth ) {

        if( pth.startsWith( "." ) )
            pth = pth.substring( 1 );

        if( pth.trim().length() == 0 )
            throw new IllegalStateException( "Path with zero length received." );

        Pair p = new Pair( );
        int idx = pth.indexOf( "." );

        p.head = idx > -1 ? pth.substring( 0, idx ) : pth;
        p.tail = idx > -1 ? pth.substring( idx + 1, pth.length() ) : null;

        return p;
    }
    

    private class Pair {
        public String head;
        public String tail;
    }
}
