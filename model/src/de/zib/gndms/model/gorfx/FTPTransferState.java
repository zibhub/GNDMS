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
public class FTPTransferState extends GridEntity {


    // maybe use fc constraint here
    private String transferId;

    private String ftpArgs;
    
    private String currentFile;


    @Id
    @Column(name="transfer_id", nullable=false, length=36, columnDefinition="CHAR", updatable=false)
    public String getTransferId() {
        return transferId;
    }


    @Column(name="ftp_arguments", columnDefinition="VARCHAR")
    public String getFtpArgs() {
        return ftpArgs;
    }


    @Column(name="current_file", columnDefinition="VARCHAR")
    public String getCurrentFile() {
        return currentFile;
    }

    /**
     * Returns the ready to use version of the ftpArgs argument.
     *
     * The normal getFtpArgs method only returns the pure data,
     * which isn't suited to construct a restart marker.
     */
    @Transient
    public String getFtpArgsString( ) {
        return "Range Marker " + ftpArgs;
    }


    public void setTransferId( String transferId ) {
        this.transferId = transferId;
    }


    public void setFtpArgs( String ftpArgs ) {
        this.ftpArgs = ftpArgs;
    }


    public void setCurrentFile( String currentFile ) {
        this.currentFile = currentFile;
    }
}
