package com.uwaterloo.indeed.api.controllers;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.uwaterloo.indeed.api.IndeedToKinesisProducer;
import com.uwaterloo.indeed.api.Request;
import com.uwaterloo.indeed.api.SortType;
import com.uwaterloo.indeed.api.cfg.IndeedApiConfiguration;
import com.uwaterloo.indeed.api.entity.JobSearchResponse;
import com.uwaterloo.indeed.api.entity.JobSearchResult;


@RestController
@RequestMapping("/search")
public class SearchController {

	@Autowired
	IndeedToKinesisProducer indeedToKinesisProducer;
	
	@Autowired
	IndeedApiConfiguration apiConfig;
	
    @RequestMapping(method = RequestMethod.GET)
    public void search(@RequestParam String location) throws UnknownHostException {
    	
    	int limit = 25;

    	RestTemplate restTemplate = new RestTemplate();

    	int matchCount = 0;
    	int startIndex = 0;
    	do {
 
        	Request indeedSearch = new Request();
        	indeedSearch.setPublisherId(apiConfig.getPublisherId());
        	indeedSearch.setOutputFormat("json");
        	indeedSearch.setLocation(location);
        	indeedSearch.setSort(SortType.DATE);
        	indeedSearch.setLimit(limit);
        	indeedSearch.setStart(startIndex);
        	indeedSearch.setFromage(90);
        	indeedSearch.setRadius(50);
        	//indeedSearch.setSiteType(SiteType.EMPLOYER);
        	//indeedSearch.setJobType(JobType.FULLTIME);
        	indeedSearch.setCountry("ca");
        	indeedSearch.setUserIP(InetAddress.getLocalHost().getHostAddress());
        	indeedSearch.setUserAgent("bot-bluegeminis");    		
    		
        	System.out.println(indeedSearch.toUrl());
        	
        	JobSearchResponse response = restTemplate.getForObject(indeedSearch.toUrl(), JobSearchResponse.class);
        	System.out.println(response.getResults());
        	
        	List<JobSearchResult> jobSearchResults = response.getResults();
        	
        	if (!CollectionUtils.isEmpty(jobSearchResults)) {
        		matchCount = jobSearchResults.size();
        		jobSearchResults.stream().forEach(job -> {
        			System.out.println(job);
        			
        			indeedToKinesisProducer.sendToKinesis(job);
        			
        		});
        		startIndex+=limit;
        	}else {
        		matchCount = 0;
        	}	
    	} while (matchCount == limit);
   
    	// Example query
    	/*{"query":"java","location":"Pierrefonds,Qc","totalResults":198,
    	 * "results":[
    	 *  {"jobtitle":"Software Tester"
    	 * 	,"company":"lamid"
    	 *  ,"state":"QC"
    	 *  ,"country":"CA"
    	 *  ,"language":"en"
    	 *  ,"formattedLocation":"Montréal, QC"
    	 *  ,"formattedLocationFull":"Montréal, QC"
    	 *  ,"source":"Indeed"
    	 *  ,"latitude":"45.50884"
    	 *  ,"longitude":"-73.58781","url":"http://ca.indeed.com/viewjob?jk=642a1853526e85a5&qd=umO-pnG59xMBSedp341Fscfb7ypkOe9Hsu95MbXS1upSRNBov7m_FlK9PBdr0_y3g0ivMm_ufy4F4BjVtqQCmoi0yKcTG9pln-6MNlmf2s9CVu_10KUVmkOHxupqCa2N&indpubnum=4615400877876484&chnl=ca&atk=1c9uqeeom1fle0id","sponsored":false,"expired":false,"date":1522511469000},{"jobtitle":"Software Developer: Integration Specialist","company":"Hitachi ID Systems","state":"QC","country":"CA","language":"en","formattedLocation":"Montréal, QC","formattedLocationFull":"Montréal, QC","source":"Hitachi ID Systems","latitude":"45.50884","longitude":"-73.58781","url":"http://ca.indeed.com/viewjob?jk=9a8c67e5105a5c5d&qd=umO-pnG59xMBSedp341Fscfb7ypkOe9Hsu95MbXS1upSRNBov7m_FlK9PBdr0_y3g0ivMm_ufy4F4BjVtqQCmoi0yKcTG9pln-6MNlmf2s9CVu_10KUVmkOHxupqCa2N&indpubnum=4615400877876484&chnl=ca&atk=1c9uqeeom1fle0id","sponsored":false,"expired":false,"date":1522491645000},{"jobtitle":"Java Developers","company":"ACCREON","state":"QC","country":"CA","language":"en","formattedLocation":"Montréal, QC","formattedLocationFull":"Montréal, QC","source":"ACCREON","latitude":"45.50884","longitude":"-73.58781","url":"http://ca.indeed.com/viewjob?jk=3ce5b6304f8c07e1&qd=umO-pnG59xMBSedp341Fscfb7ypkOe9Hsu95MbXS1upSRNBov7m_FlK9PBdr0_y3g0ivMm_ufy4F4BjVtqQCmoi0yKcTG9pln-6MNlmf2s9CVu_10KUVmkOHxupqCa2N&indpubnum=4615400877876484&chnl=ca&atk=1c9uqeeom1fle0id","sponsored":false,"expired":false,"date":1522476951000},{"jobtitle":"Programmeur Online - Rainbow 6","company":"Ubisoft","state":"QC","country":"CA","language":"fr","formattedLocation":"Montréal, QC","formattedLocationFull":"Montréal, QC","source":"Ubisoft","latitude":"45.50884","longitude":"-73.58781","url":"http://ca.indeed.com/viewjob?jk=b8d8f27bbdaa546b&qd=umO-pnG59xMBSedp341Fscfb7ypkOe9Hsu95MbXS1upSRNBov7m_FlK9PBdr0_y3g0ivMm_ufy4F4BjVtqQCmoi0yKcTG9pln-6MNlmf2s9CVu_10KUVmkOHxupqCa2N&indpubnum=4615400877876484&chnl=ca&atk=1c9uqeeom1fle0id","sponsored":false,"expired":false,"date":1522465585000},{"jobtitle":"Software Specialists, You're Always Encouraged to Apply","company":"Talent 300","state":"QC","country":"CA","language":"en","formattedLocation":"Montréal, QC","formattedLocationFull":"Montréal, QC","source":"Talent 300","latitude":"45.50884","longitude":"-73.58781","url":"http://ca.indeed.com/viewjob?jk=0879ea37a0264a5b&qd=umO-pnG59xMBSedp341Fscfb7ypkOe9Hsu95MbXS1upSRNBov7m_FlK9PBdr0_y3g0ivMm_ufy4F4BjVtqQCmoi0yKcTG9pln-6MNlmf2s9CVu_10KUVmkOHxupqCa2N&indpubnum=4615400877876484&chnl=ca&atk=1c9uqeeom1fle0id","sponsored":false,"expired":false,"date":1522475387000},{"jobtitle":"Database Developer (SQL / Java / Finance processes)","company":"Trigyn","state":"QC","country":"CA","language":"en","formattedLocation":"Montréal, QC","formattedLocationFull":"Montréal, QC","source":"Trigyn","latitude":"45.50884","longitude":"-73.58781","url":"http://ca.indeed.com/viewjob?jk=d7039d1481df0649&qd=umO-pnG59xMBSedp341Fscfb7ypkOe9Hsu95MbXS1upSRNBov7m_FlK9PBdr0_y3g0ivMm_ufy4F4BjVtqQCmoi0yKcTG9pln-6MNlmf2s9CVu_10KUVmkOHxupqCa2N&indpubnum=4615400877876484&chnl=ca&atk=1c9uqeeom1fle0id","sponsored":false,"expired":false,"date":1522468973000},{"jobtitle":"Programmeur(se) Web","company":"Vortex Solution","state":"QC","country":"CA","language":"fr","formattedLocation":"Montréal, QC","formattedLocationFull":"Montréal, QC","source":"Espresso-Jobs","latitude":"45.50884","longitude":"-73.58781","url":"http://ca.indeed.com/viewjob?jk=da294aed6bc140e8&qd=umO-pnG59xMBSedp341Fscfb7ypkOe9Hsu95MbXS1upSRNBov7m_FlK9PBdr0_y3g0ivMm_ufy4F4BjVtqQCmoi0yKcTG9pln-6MNlmf2s9CVu_10KUVmkOHxupqCa2N&indpubnum=4615400877876484&chnl=ca&atk=1c9uqeeom1fle0id","sponsored":false,"expired":false,"date":1522474155000},{"jobtitle":"Front-End Developer","company":"Michael Page International","state":"QC","country":"CA","language":"en","formattedLocation":"Montréal, QC","formattedLocationFull":"Montréal, QC","source":"Michael Page International","latitude":"45.50884","longitude":"-73.58781","url":"http://ca.indeed.com/viewjob?jk=167a94ef8a38e045&qd=umO-pnG59xMBSedp341Fscfb7ypkOe9Hsu95MbXS1upSRNBov7m_FlK9PBdr0_y3g0ivMm_ufy4F4BjVtqQCmoi0yKcTG9pln-6MNlmf2s9CVu_10KUVmkOHxupqCa2N&indpubnum=4615400877876484&chnl=ca&atk=1c9uqeeom1fle0id","sponsored":false,"expired":false,"date":1522466182000},{"jobtitle":"DevOps Specialist","company":"Trigyn","state":"QC","country":"CA","language":"en","formattedLocation":"Montréal, QC","formattedLocationFull":"Montréal, QC","source":"Trigyn","latitude":"45.50884","longitude":"-73.58781","url":"http://ca.indeed.com/viewjob?jk=33e36acd25d1ca66&qd=umO-pnG59xMBSedp341Fscfb7ypkOe9Hsu95MbXS1upSRNBov7m_FlK9PBdr0_y3g0ivMm_ufy4F4BjVtqQCmoi0yKcTG9pln-6MNlmf2s9CVu_10KUVmkOHxupqCa2N&indpubnum=4615400877876484&chnl=ca&atk=1c9uqeeom1fle0id","sponsored":false,"expired":false,"date":1522468967000},{"jobtitle":"Front End Engineer","company":"Michael Page International","state":"QC","country":"CA","language":"en","formattedLocation":"Montréal, QC","formattedLocationFull":"Montréal, QC","source":"Michael Page International","latitude":"45.50884","longitude":"-73.58781","url":"http://ca.indeed.com/viewjob?jk=b3de1a734e21bcc1&qd=umO-pnG59xMBSedp341Fscfb7ypkOe9Hsu95MbXS1upSRNBov7m_FlK9PBdr0_y3g0ivMm_ufy4F4BjVtqQCmoi0yKcTG9pln-6MNlmf2s9CVu_10KUVmkOHxupqCa2N&indpubnum=4615400877876484&chnl=ca&atk=1c9uqeeom1fle0id","sponsored":false,"expired":false,"date":1522466183000},{"jobtitle":"Développeur 'Full Stack'","company":"Équipements Comact inc. Mirabel","state":"QC","country":"CA","language":"fr","formattedLocation":"Mirabel, QC","formattedLocationFull":"Mirabel, QC","source":"Jobillico","latitude":"45.65008","longitude":"-74.08251","url":"http://ca.indeed.com/viewjob?jk=3a16f0fba187c7b4&qd=umO-pnG59xMBSedp341Fscfb7ypkOe9Hsu95MbXS1upSRNBov7m_FlK9PBdr0_y3g0ivMm_ufy4F4BjVtqQCmoi0yKcTG9pln-6MNlmf2s9CVu_10KUVmkOHxupqCa2N&indpubnum=4615400877876484&chnl=ca&atk=1c9uqeeom1fle0id","sponsored":false,"expired":false,"date":1522478954000},{"jobtitle":"Directeur, Développement des applications","company":"BelairDirect","state":"QC","country":"CA","language":"fr","formattedLocation":"Montréal, QC","formattedLocationFull":"Montréal, QC","source":"belairdirect","latitude":"45.50884","longitude":"-73.58781","url":"http://ca.indeed.com/viewjob?jk=eeebc26af8ba3ae1&qd=umO-pnG59xMBSedp341Fscfb7ypkOe9Hsu95MbXS1upSRNBov7m_FlK9PBdr0_y3g0ivMm_ufy4F4BjVtqQCmoi0yKcTG9pln-6MNlmf2s9CVu_10KUVmkOHxupqCa2N&indpubnum=4615400877876484&chnl=ca&atk=1c9uqeeom1fle0id","sponsored":false,"expired":false,"date":1522464840000},{"jobtitle":"Administrator, SAP Basis","company":"Produits forestiers Résolu","state":"QC","country":"CA","language":"en","formattedLocation":"Montréal, QC","formattedLocationFull":"Montréal, QC","source":"Jobillico","latitude":"45.50884","longitude":"-73.58781","url":"http://ca.indeed.com/viewjob?jk=02805e50ed4995c8&qd=umO-pnG59xMBSedp341Fscfb7ypkOe9Hsu95MbXS1upSRNBov7m_FlK9PBdr0_y3g0ivMm_ufy4F4BjVtqQCmoi0yKcTG9pln-6MNlmf2s9CVu_10KUVmkOHxupqCa2N&indpubnum=4615400877876484&chnl=ca&atk=1c9uqeeom1fle0id","sponsored":false,"expired":false,"date":1522478028000},{"jobtitle":"Android Developer","company":"TrueRPO","state":"QC","country":"CA","language":"en","formattedLocation":"Lachine, QC","formattedLocationFull":"Lachine, QC","source":"Indeed","latitude":"45.4315","longitude":"-73.66908","url":"http://ca.indeed.com/viewjob?jk=7df812e9563668dd&qd=umO-pnG59xMBSedp341Fscfb7ypkOe9Hsu95MbXS1upSRNBov7m_FlK9PBdr0_y3g0ivMm_ufy4F4BjVtqQCmoi0yKcTG9pln-6MNlmf2s9CVu_10KUVmkOHxupqCa2N&indpubnum=4615400877876484&chnl=ca&atk=1c9uqeeom1fle0id","sponsored":false,"expired":false,"date":1522440292000},{"jobtitle":"Développeur S.T.I.","company":"Logient","state":"QC","country":"CA","language":"fr","formattedLocation":"Montréal, QC","formattedLocationFull":"Montréal, QC","source":"Logient","latitude":"45.50884","longitude":"-73.58781","url":"http://ca.indeed.com/viewjob?jk=3a3e78e815495e55&qd=umO-pnG59xMBSedp341Fscfb7ypkOe9Hsu95MbXS1upSRNBov7m_FlK9PBdr0_y3g0ivMm_ufy4F4BjVtqQCmoi0yKcTG9pln-6MNlmf2s9CVu_10KUVmkOHxupqCa2N&indpubnum=4615400877876484&chnl=ca&atk=1c9uqeeom1fle0id","sponsored":false,"expired":false,"date":1522379506000},{"jobtitle":"Développeur Java Sénior","company":"Manulife Financial","state":"QC","country":"CA","language":"fr","formattedLocation":"Montréal, QC","formattedLocationFull":"Montréal, QC","source":"Manulife Financial","latitude":"45.50884","longitude":"-73.58781","url":"http://ca.indeed.com/viewjob?jk=0ae970c06a0c7d33&qd=umO-pnG59xMBSedp341Fscfb7ypkOe9Hsu95MbXS1upSRNBov7m_FlK9PBdr0_y3g0ivMm_ufy4F4BjVtqQCmoi0yKcTG9pln-6MNlmf2s9CVu_10KUVmkOHxupqCa2N&indpubnum=4615400877876484&chnl=ca&atk=1c9uqeeom1fle0id","sponsored":false,"expired":false,"date":1522399116000},{"jobtitle":"Développeur Java Sénior - Senior Java Developer","company":"Manulife Financial","state":"QC","country":"CA","language":"fr","formattedLocation":"Montréal, QC","formattedLocationFull":"Montréal, QC","source":"Manulife Financial","latitude":"45.50884","longitude":"-73.58781","url":"http://ca.indeed.com/viewjob?jk=1b054ea79eb4c973&qd=umO-pnG59xMBSedp341Fscfb7ypkOe9Hsu95MbXS1upSRNBov7m_FlK9PBdr0_y3g0ivMm_ufy4F4BjVtqQCmoi0yKcTG9pln-6MNlmf2s9CVu_10KUVmkOHxupqCa2N&indpubnum=4615400877876484&chnl=ca&atk=1c9uqeeom1fle0id","sponsored":false,"expired":false,"date":1522399073000},{"jobtitle":"Analyste programmeur","company":"Bombardier","state":"QC","country":"CA","language":"fr","formattedLocation":"Montréal, QC","formattedLocationFull":"Montréal, QC","source":"Bombardier","latitude":"45.50884","longitude":"-73.58781","url":"http://ca.indeed.com/viewjob?jk=70ceae4d53c992fb&qd=umO-pnG59xMBSedp341Fscfb7ypkOe9Hsu95MbXS1upSRNBov7m_FlK9PBdr0_y3g0ivMm_ufy4F4BjVtqQCmoi0yKcTG9pln-6MNlmf2s9CVu_10KUVmkOHxupqCa2N&indpubnum=4615400877876484&chnl=ca&atk=1c9uqeeom1fle0id","sponsored":false,"expired":false,"date":1522373232000},{"jobtitle":"Développeur Support DevOps","company":"Planaxis | Groupaxis","state":"QC","country":"CA","language":"fr","formattedLocation":"Montréal, QC","formattedLocationFull":"Montréal, QC","source":"Planaxis | Groupaxis","latitude":"45.50884","longitude":"-73.58781","url":"http://ca.indeed.com/viewjob?jk=10691c2f978e6840&qd=umO-pnG59xMBSedp341Fscfb7ypkOe9Hsu95MbXS1upSRNBov7m_FlK9PBdr0_y3g0ivMm_ufy4F4BjVtqQCmoi0yKcTG9pln-6MNlmf2s9CVu_10KUVmkOHxupqCa2N&indpubnum=4615400877876484&chnl=ca&atk=1c9uqeeom1fle0id","sponsored":false,"expired":false,"date":1522437056000},{"jobtitle":"DevOps - Operations Developer","company":"IMMUNIO","state":"QC","country":"CA","language":"en","formattedLocation":"Montréal, QC","formattedLocationFull":"Montréal, QC","source":"IMMUNIO","latitude":"45.50884","longitude":"-73.58781","url":"http://ca.indeed.com/viewjob?jk=98835fdcbab24c12&qd=umO-pnG59xMBSedp341Fscfb7ypkOe9Hsu95MbXS1upSRNBov7m_FlK9PBdr0_y3g0ivMm_ufy4F4BjVtqQCmoi0yKcTG9pln-6MNlmf2s9CVu_10KUVmkOHxupqCa2N&indpubnum=4615400877876484&chnl=ca&atk=1c9uqeeom1fle0id","sponsored":false,"expired":false,"date":1522402684000},{"jobtitle":"Java/J2EE Developer","company":"BBG Management Corporation","state":"QC","country":"CA","language":"en","formattedLocation":"Montréal, QC","formattedLocationFull":"Montréal, QC","source":"BBG Management Corporation","latitude":"45.50884","longitude":"-73.58781","url":"http://ca.indeed.com/viewjob?jk=a09335462cfd53c2&qd=umO-pnG59xMBSedp341Fscfb7ypkOe9Hsu95MbXS1upSRNBov7m_FlK9PBdr0_y3g0ivMm_ufy4F4BjVtqQCmoi0yKcTG9pln-6MNlmf2s9CVu_10KUVmkOHxupqCa2N&indpubnum=4615400877876484&chnl=ca&atk=1c9uqeeom1fle0id","sponsored":false,"expired":false,"date":1522368944000},{"jobtitle":"Java Developer","company":"Alteo Inc. IT Recruiting Services","state":"QC","country":"CA","language":"en","formattedLocation":"Montréal, QC","formattedLocationFull":"Montréal, QC","source":"Alteo Inc. IT Recruiting Services","latitude":"45.50884","longitude":"-73.58781","url":"http://ca.indeed.com/viewjob?jk=f87487d4a6e0cc68&qd=umO-pnG59xMBSedp341Fscfb7ypkOe9Hsu95MbXS1upSRNBov7m_FlK9PBdr0_y3g0ivMm_ufy4F4BjVtqQCmoi0yKcTG9pln-6MNlmf2s9CVu_10KUVmkOHxupqCa2N&indpubnum=4615400877876484&chnl=ca&atk=1c9uqeeom1fle0id","sponsored":false,"expired":false,"date":1522369730000},{"jobtitle":"Senior functional analyst","company":"Generix Group - Amérique du Nord","state":"QC","country":"CA","language":"en","formattedLocation":"Montréal, QC","formattedLocationFull":"Montréal, QC","source":"Indeed","latitude":"45.50884","longitude":"-73.58781","url":"http://ca.indeed.com/viewjob?jk=d782eca1bd5668ca&qd=umO-pnG59xMBSedp341Fscfb7ypkOe9Hsu95MbXS1upSRNBov7m_FlK9PBdr0_y3g0ivMm_ufy4F4BjVtqQCmoi0yKcTG9pln-6MNlmf2s9CVu_10KUVmkOHxupqCa2N&indpubnum=4615400877876484&chnl=ca&atk=1c9uqeeom1fle0id","sponsored":false,"expired":false,"date":1522429330000},{"jobtitle":"Advanced Application Analyst","company":"Generix Group - Amérique du Nord","state":"QC","country":"CA","language":"en","formattedLocation":"Montréal, QC","formattedLocationFull":"Montréal, QC","source":"Indeed","latitude":"45.50884","longitude":"-73.58781","url":"http://ca.indeed.com/viewjob?jk=745f889289d2e427&qd=umO-pnG59xMBSedp341Fscfb7ypkOe9Hsu95MbXS1upSRNBov7m_FlK9PBdr0_y3g0ivMm_ufy4F4BjVtqQCmoi0yKcTG9pln-6MNlmf2s9CVu_10KUVmkOHxupqCa2N&indpubnum=4615400877876484&chnl=ca&atk=1c9uqeeom1fle0id","sponsored":false,"expired":false,"date":1522429235000},{"jobtitle":"Programmeur JDE (JD edwards)","company":"Planaxis | Groupaxis","state":"QC","country":"CA","language":"fr","formattedLocation":"Montréal, QC","formattedLocationFull":"Montréal, QC","source":"Planaxis | Groupaxis","latitude":"45.50884","longitude":"-73.58781","url":"http://ca.indeed.com/viewjob?jk=e99f48f838b3226a&qd=umO-pnG59xMBSedp341Fscfb7ypkOe9Hsu95MbXS1upSRNBov7m_FlK9PBdr0_y3g0ivMm_ufy4F4BjVtqQCmoi0yKcTG9pln-6MNlmf2s9CVu_10KUVmkOHxupqCa2N&indpubnum=4615400877876484&chnl=ca&atk=1c9uqeeom1fle0id","sponsored":false,"expired":false,"date":1522437056000}]}
    	*/
    	
    }

}
