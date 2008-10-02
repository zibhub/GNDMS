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
    @Id @Column(name="id", nullable=false, length=36, columnDefinition="CHAR", updatable=false)
    private String transferId;

    @Column(name="ftp_arguments", columnDefinition="VARCHAR")
    private String ftpArgs;
    
    @Column(name="current_file", columnDefinition="VARCHAR")
    private String currentFile;


    public FTPTransferState( ) {
    }

    
    public String getTransferId() {
        return transferId;
    }


    public void setTransferId( String transferId ) {
        this.transferId = transferId;
    }


    public String getFtpArgs() {
        return ftpArgs;
    }


    public void setFtpArgs( String ftpArgs ) {
        this.ftpArgs = ftpArgs;
    }


    public String getCurrentFile() {
        return currentFile;
    }


    public void setCurrentFile( String currentFile ) {
        this.currentFile = currentFile;
    }
}
