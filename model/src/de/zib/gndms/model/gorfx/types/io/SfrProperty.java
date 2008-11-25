/**
 * (c) 2006 Zuse Institute Berlin, Takustr 7, D-14195 Berlin, Germany
 *
 * @author langhammer 
 *
 */
package de.zib.gndms.model.gorfx.types.io;

import java.io.OutputStream;
import java.io.PrintStream;

public enum SfrProperty {
	JUST_ASK("c3grid.StageFileRequest.JustAsk", "true or false"),
	OBJECT_ITEMS("c3grid.StageFileRequest.ObjectList.Item"),
	LON_MIN("c3grid.StageFileRequest.SpaceConstr.Longitude.Min"),
	LON_MAX("c3grid.StageFileRequest.SpaceConstr.Longitude.Max"),
	LAT_MIN("c3grid.StageFileRequest.SpaceConstr.Latitude.Min"),
	LAT_MAX("c3grid.StageFileRequest.SpaceConstr.Latitude.Max"),
	ALT_MIN("c3grid.StageFileRequest.SpaceConstr.Altitude.Min"),
	ALT_MAX("c3grid.StageFileRequest.SpaceConstr.Altitude.Max"),
	ALT_MINNAME("c3grid.StageFileRequest.SpaceConstr.Altitude.MinName"),
	ALT_MAXNAME("c3grid.StageFileRequest.SpaceConstr.Altitude.MaxName"),
	ALT_UNIT_MIN("c3grid.StageFileRequest.SpaceConstr.Altitude.MinUnit"),
	ALT_UNIT_MAX("c3grid.StageFileRequest.SpaceConstr.Altitude.MaxUnit"),
	ALT_VCRS("c3grid.StageFileRequest.SpaceConstr.Altitude.VerticalCRS"),
	TIME_MIN("c3grid.StageFileRequest.TimeConstr.MinTime"),
	TIME_MAX("c3grid.StageFileRequest.TimeConstr.MaxTime"),
	CFLIST_OLD("c3grid.StageFileRequest.CFList"),
	CFLIST_ITEMS("c3grid.StageFileRequest.CFList.CFItem"),
	CONSTRAINT_LIST("c3grid.StageFileRequest.ConstraintList","<undocumented>"),
	FILE_FORMAT("c3grid.StageFileRequest.TargetFileFormat"),
	BASE_FILE("c3grid.StageFileRequest.TargetBaseDataFile"),
	META_FILE("c3grid.StageFileRequest.TargetMetaDataFile"),
	STAGING_TIME("c3grid.StageFileResponse.StagingTime","1234"),
    // G2 additions
    META_FILE_FORMAT("c3grid.StageFileRequest.TargetMetaFileFormat"),
    META_FILE_ARCHIVE_FORMAT("c3grid.StageFileRequest.TargetMetaFileArchiveFormat"),
    FILE_ARCHIVE_FORMAT("c3grid.StageFileRequest.TargetFileArchiveFormat"),
    JUST_DOWNLOAD("c3grid.StageFileRequest.JustDownload" ),
    AREA_CRS("c3grid.StageFileRequest.SpaceConstr.AreaCRS"),
    // slice stagein
    GRID_SITE("c3grid.SliceStageInRequest.GridSite"),
    // file transfer orq
    FILE_TRANSFER_SOURCE_URI("c3grid.FileTransferRequest.SourceURI"),
    FILE_TRANSFER_DESTINATION_URI("c3grid.FileTransferRequest.DestinationURI"),
    FILE_TRANSFER_FILE_MAPPING("c3grid.FileTransferRequest.FileMapping"),
    
    // common data for muliple requests
    CONTEXT( "c3grid.CommonRequest.Context" ),
	
	EST_MAX_SIZE("c3grid.StageFileRequest.Estimate.MaxSize","10000"),
	EST_IF_DECISION_BEFORE("c3grid.StageFileRequest.Estimate.IfDecisionBefore"),
	EST_EXEC_LIKELY_UNTIL("c3grid.StageFileRequest.Estimate.ExecutionLikelyUntil"),
	EST_RESULT_VALID_UNTIL("c3grid.StageFileRequest.Estimate.ResultValidUntil");

    public final String key;
	public final String doc;
    
	SfrProperty(String key, String deflt) {
		this.key = key;
		doc = deflt;
	}
	SfrProperty(String key) {
		this.key = key;
		doc = null;
	}
    
	static void storeTemplateProperties(OutputStream out) {
		PrintStream o = new PrintStream(out);  
		for (SfrProperty s : SfrProperty.values()) {
			if (s.doc!= null) {
				o.println();
				o.println("## "+s.doc.trim().replaceAll("\n", "\n## "));
			}
			o.println("#"+s.key+"=");
		}
	}

}