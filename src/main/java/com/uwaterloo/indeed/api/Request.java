package com.uwaterloo.indeed.api;

import com.amazonaws.services.importexport.model.JobType;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents an Indeed API Request
 * 
 *
 */
public class Request {

	private final static String URL_PROLOG = "http://api.indeed.com/ads/apisearch";

	@Getter@Setter private String publisherId;
	@Getter@Setter private String apiVersion = "2";
	@Getter@Setter private String outputFormat;
	@Getter@Setter private String callbackFx;
	@Getter@Setter private String query;
	@Getter@Setter private String location;
	@Getter@Setter private int start;
	@Getter@Setter private int limit;
	@Getter@Setter private int fromage;
	@Getter@Setter private int radius;
	
	@Getter@Setter private String country = "ca";
	@Getter@Setter private String channelName = "ca";
	@Getter@Setter private String userIP ="1.1.1.1";
	
	//TODO: Automated web crawling tools can use a simplified form, where an important field is contact information in case of problems.
	/*By convention the word "bot" is included in the name of the agent[citation needed]. For example:
		Googlebot/2.1 (+http://www.google.com/bot.html)*/
	@Getter@Setter private String userAgent = "Mozilla/5.0";
	@Getter@Setter private SortType sort;
	@Getter@Setter private SiteType siteType;
	@Getter@Setter private JobType jobType;
	@Getter@Setter private boolean highlight = false;
	@Getter@Setter private boolean removeDuplicates = true;
	
	public String toUrl(){
		StringBuffer url = new StringBuffer(URL_PROLOG)
				 .append("?publisher="+publisherId)
				 .append("&q=").append(query)
				 .append("&l=").append(location)
				 .append("&sort=").append(SortType.DATE.name().toLowerCase())
				 .append("&radius=").append(radius);
				if(siteType != null){
					url.append("&st=").append(siteType.name().toLowerCase());
				}
				url.append("&start=").append(start)
				.append("&limit=").append(limit)
				.append("&fromage=").append(fromage)
				.append("&filter=").append(removeDuplicates?1:0)
				.append("&co=").append(channelName)
				.append("&chnl=").append(country)
				.append("&userip=").append(userIP)
				.append("&useragent=").append(userIP)
				.append("&format=").append(outputFormat)
				.append("&v=").append(apiVersion);
		return url.toString();
	}
	
	
}
