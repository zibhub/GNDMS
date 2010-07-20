package de.zib.gndms.model.gorfx;

import de.zib.gndms.model.common.GridEntity;

import javax.persistence.*;


/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 30.09.2008, Time: 17:24:14
 */
@Entity(name="FTPTransferState")
@Table(name="ftp_transfer_state", schema="gorfx")
class FTPTransferState extends GridEntity {


    // maybe use fc constraint here
    @Id
    @Column(name="transfer_id", nullable=false, length=36, columnDefinition="CHAR", updatable=false)
    String transferId;

    @Column(name="ftp_arguments", columnDefinition="VARCHAR")
    String ftpArgs;
    
    @Column(name="current_file", columnDefinition="VARCHAR")
    String currentFile;

    /**
     * Returns the ready to use version of the ftpArgs argument.
     *
     * The normal getFtpArgs method only returns the pure data,
     * which isn't suited to construct a restart marker.
     */
    public String getFtpArgsString( ) {
        return "Range Marker " + ftpArgs;
    }
}
