package com.gcp.pubsub.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.integration.AckMode;
import com.google.cloud.spring.pubsub.integration.inbound.PubSubInboundChannelAdapter;

@RestController
public class PubSubController {
	
	String message;
	
	@GetMapping("getMessage")
	public String getMessage()
	{
		return "Message from GCP"+message;
	}
	
	@Bean
	  public PubSubInboundChannelAdapter messageAdapter(
	    @Qualifier("pubsubInputChannel") MessageChannel inputChannel,
	    PubSubTemplate pubSubTemplate) {
	  PubSubInboundChannelAdapter adapter =
	    new PubSubInboundChannelAdapter(pubSubTemplate, "PubSubDemoSubscription");
	  adapter.setOutputChannel(inputChannel);
	  adapter.setAckMode(AckMode.MANUAL);

	  return adapter;
	  }
	
	@Bean
	  public MessageChannel pubsubInputChannel() {
	  return new DirectChannel();
	  }
	
	
	  @ServiceActivator(inputChannel = "pubsubInputChannel")
	  public void receiveMessage(String payload) 
	{
	  this.message=payload;
	  }
	

}
