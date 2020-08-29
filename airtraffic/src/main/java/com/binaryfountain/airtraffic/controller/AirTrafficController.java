package com.binaryfountain.airtraffic.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.binaryfountain.airtraffic.entity.AirCraft;
import com.binaryfountain.airtraffic.service.AirTrafficService;

@RestController
@RequestMapping("/aircraft/")
public class AirTrafficController {
	private final AirTrafficService airTrafficService;

	AirTrafficController(AirTrafficService service) {
		this.airTrafficService = service;
		airTrafficService.initialize();
	}

	@PostMapping("/enqueue")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<AirCraft> enqueue(@RequestBody AirCraft aircraft) {
		long t1 = System.currentTimeMillis();
		try {
			AirCraft craft = airTrafficService.enqueue(aircraft);
			return ResponseEntity.ok().body(craft);
		} catch (IllegalArgumentException iae) {
			return ResponseEntity.badRequest().build();
		} finally {
			System.out.println("enqueue ms:" + (System.currentTimeMillis() - t1));
		}

	}

	@GetMapping("/dequeue")
	@ResponseBody
	public AirCraft dequeue(HttpServletResponse response) {
		long t1 = System.currentTimeMillis();
		try {
			AirCraft aircraft = airTrafficService.dequeue();
			if (aircraft == null) {
				response.setStatus(HttpStatus.NO_CONTENT.value());
				// return ResponseEntity.noContent().build();
			}
			return aircraft;
		} catch (Exception e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return null;
		} finally {
			System.out.println("dequeue ms:" + (System.currentTimeMillis() - t1));
		}

		// return ResponseEntity.ok().body(aircraft);
	}

	@GetMapping(path = "/list")
	@ResponseBody
	public Iterable<AirCraft> getAllAirCrafts() {

		long t1 = System.currentTimeMillis();
		try {
			return airTrafficService.findAll();
		} finally {
			System.out.println("list ms:" + (System.currentTimeMillis() - t1));
		}

	}
}
