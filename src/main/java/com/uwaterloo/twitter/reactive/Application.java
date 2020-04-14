package com.uwaterloo.twitter.reactive;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import com.uwaterloo.twitter.reactive.auth.OAuth1Credentials;
import com.uwaterloo.twitter.reactive.auth.OAuth1SignatureUtil;
import com.uwaterloo.twitter.reactive.cfg.TwitterApiConfiguration;
import com.uwaterloo.twitter.reactive.entity.Tweet;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



@EnableConfigurationProperties(TwitterApiConfiguration.class)
@SpringBootApplication
public class Application {

	@Autowired
	TwitterApiConfiguration apiConfig;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    public static boolean matchesSearchTerms(Tweet tweet) {
    	if(tweet == null || tweet.getText() == null) {
    		return false;
    	}
    	
    	String text = tweet.getText().toLowerCase();    	
    	if (text.contains("covid19") 
    			|| text.contains("covid")
    			|| text.contains("pandemic") 
    			|| text.contains("covid-19") 
    			|| text.contains("job")) {
    		return true;
    	}
    	return false;
    }
    
    @Bean
    ApplicationRunner twitterStream(WebClient.Builder wcb, OAuth1SignatureUtil oauthUtil) {
        return args -> {
        	
        	TwitterToKinesisProducer twitterToKinesisProducer = new TwitterToKinesisProducer(apiConfig.getStreamName());

            WebClient webClient = wcb
                    .baseUrl("https://stream.twitter.com/1.1")
                    .filter(oauthFilter(oauthUtil))
                    .build();

            //Bounding boxes for locations
    		String restOfCanada = "-138.46,49.36,-58.40,73.24";
    		String aroundTO =  "-82.07,42.75,-79.13,48.98";
    		String tOtoOttawa = "-78.88,43.95,-76.65,44.83";
    		String ottToQc = "-79.46,45.26,-71.90,48.53";
    		String qcCity = "-72.08,46.00,-70.32,48.94";
    		String maritimes = "-66.81,43.63,-52.55,51.70";
            
            Flux<Tweet> tweets = webClient.get().uri(uriBuilder -> uriBuilder.path("/statuses/filter.json")
                    .queryParam("lang", "en")
                    .queryParam("locations", restOfCanada+","+aroundTO+","+tOtoOttawa+","+ottToQc+","+qcCity+","+maritimes)                   
                    .build())
                    .exchange()
                    .flatMapMany(clientResponse -> clientResponse.bodyToFlux(Tweet.class));
            
            tweets
            	//.limitRate(prefetchRate) OPTIONAL
            	.filter(Application::matchesSearchTerms)
            	.subscribe(tweet -> {
            	  System.out.println("Sending tweet");
            	  twitterToKinesisProducer.sendTweetToKinesis(tweet);
              });
        };
    }

    @Bean
    OAuth1SignatureUtil oAuth1SignatureUtil(TwitterApiConfiguration props) {
        OAuth1Credentials creds = new OAuth1Credentials(props.getKey(), props.getSecret(),
                props.getAccessToken(), props.getAccessTokenSecret());
        return new OAuth1SignatureUtil(creds);
    }

    private ExchangeFilterFunction oauthFilter(OAuth1SignatureUtil oauthUtil) {
        return ExchangeFilterFunction.ofRequestProcessor(req -> {
            ClientRequest oauthReq = ClientRequest.from(req)
                    .headers(headers -> headers.add(HttpHeaders.AUTHORIZATION, oauthUtil.oAuth1Header(req)))
                    .build();
            return Mono.just(oauthReq);
        });
    }
}
