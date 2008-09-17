package de.zib.gndms.model.gorfx.types;

import de.zib.gndms.model.gorfx.types.SpaceConstraintType;

/**
 * @author Maik Jorra <jorra@zib.de>
 * @verson \$id$
 * <p/>
 * User: bzcjorra Date: Sep 15, 2008 4:10:44 PM
 *
 * Todo: add altitude as soon as it is specified
 */
public class SpaceConstraint implements DataConstraint {

    private SpaceConstraintType kind;
    private double minValue;
    private double maxValue;

    // make choices parallel

    public SpaceConstraintType getKind() {
        return kind;
    }


    public void setKind( SpaceConstraintType kind ) {
        this.kind = kind;
    }


    public double getMinValue() {
        return minValue;
    }


    public void setMinValue( double minValue ) {
        this.minValue = minValue;
    }


    public double getMaxValue() {
        return maxValue;
    }


    public void setMaxValue( double maxValue ) {
        this.maxValue = maxValue;
    }
}
