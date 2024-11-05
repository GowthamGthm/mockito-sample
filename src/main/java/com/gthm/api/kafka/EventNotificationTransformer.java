package com.gthm.api.kafka;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class EventNotificationTransformer {

    @Autowired
    RestTemplate restTemplate;


    public void transformDealerNavExceptions(StructureRequest request) {
        System.out.println("transforming the deal structure request");

        request.getReviews()
               .forEach(ele ->
                       System.out.println("processing each pokemon reviews: " + ele.getStars())
                );

        restTemplate.exchange("http://www.google.com", HttpMethod.GET, null, String.class);

    }

}