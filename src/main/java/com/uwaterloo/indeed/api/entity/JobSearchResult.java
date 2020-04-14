package com.uwaterloo.indeed.api.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class JobSearchResult {
	
	/*Sample [{"jobtitle":"SQL Application Analyst (Bilingual)",
	 * 	"company":"ACCEO Solutions Inc."
	 * 	,"city":"Saint-Laurent"
	 *  ,"state":"QC","country":"CA","language":"en","formattedLocation":"Saint-Laurent, QC"
	 *  ,"source":"Indeed","date":"Fri, 23 Mar 2018 23:11:50 GMT"
	 *  ,"snippet":"Document and resolve incidents following investigation communications. At GTechna, our team is redefining parking industry standards with real-time cloud..."
	 *  ,"url":"http://ca.indeed.com/viewjob?jk=aa5d0faa63e4c745&qd=umO-pnG59xMBSedp341Fscfb7ypkOe9Hsu95MbXS1upSRNBov7m_FlK9PBdr0_y3wLD5drbljGsdazw-D4gJKcISVeVAPsBnN2U2Bze_4r4Aw9v0S7k1HDWVt-tAg3l5&indpubnum=4615400877876484&chnl=ca&atk=1c9ff15si4o9i9c8"
	 *  ,"onmousedown":"indeed_clk(this,'8528');"
	 *  ,"latitude":45.50008,"longitude":-73.66585
	 *  ,"jobkey":"aa5d0faa63e4c745"
	 *  ,"sponsored":false,"expired":false
	 *  ,"indeedApply":true
	 *  ,"formattedLocationFull":"Saint-Laurent, QC"
	 *  ,"formattedRelativeTime":"1 day ago"
	 *  ,"stations":""}
	 */
	
	@Getter@Setter private String jobtitle;
	@Getter@Setter private String company;
	@Getter@Setter private String state;
	//@Getter@Setter private String country;
	@Getter@Setter private String language;
	@Getter@Setter private String formattedLocation;
	//@Getter@Setter private String formattedLocationFull;
	//@Getter@Setter private String source;
	//@Getter@Setter private String latitude;
	//@Getter@Setter private String longitude;
	//@Getter@Setter private String url;
	//@Getter@Setter private boolean sponsored;
	@Getter@Setter private boolean expired;
	@Getter@Setter private Date date;
	
	
}
