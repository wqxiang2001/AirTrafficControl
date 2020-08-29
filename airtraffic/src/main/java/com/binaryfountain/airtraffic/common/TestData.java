package com.binaryfountain.airtraffic.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.binaryfountain.airtraffic.entity.AirCraft;
import com.binaryfountain.airtraffic.entity.AirCraft.CraftSize;
import com.binaryfountain.airtraffic.entity.AirCraft.CraftType;
import com.binaryfountain.airtraffic.service.AirTrafficService;

@Configuration
class TestData {
	private static final Logger log = LoggerFactory.getLogger(TestData.class);

	/*
	 * @Bean CommandLineRunner initDatabase(AirTrafficService service) { return args
	 * -> { log.info("Inserting " + service.enqueue(new AirCraft(CraftType.Cargo,
	 * CraftSize.LARGE))); log.info("Inserting " + service.enqueue(new
	 * AirCraft(CraftType.VIP, CraftSize.LARGE))); }; }
	 */
}