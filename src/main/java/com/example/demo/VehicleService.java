package com.example.demo;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class VehicleService implements ApplicationListener<ApplicationStartedEvent> {

	private final VehicleRepository vehicleRepository;

	public VehicleService(VehicleRepository vehicleRepository) {
		this.vehicleRepository = vehicleRepository;
	}

	@Override
	public void onApplicationEvent(ApplicationStartedEvent event) {
		
		// "warmup"
		executeSingle("A");
		
		executeSingle("B");
		executeSingle("B");
		executeSingle("B");
		executeSingle("B");
		
		executeSingle("C");
		executeSingle("D");
		executeSingle("E");
		
		executeMultipleQuery();
		
	}
	
	private void executeSingle(String type) {
		long start = System.currentTimeMillis();
		Page<Vehicle> vehicles = vehicleRepository.findByType(type, PageRequest.of(0, 1000, Sort.by("updatedAt").ascending()));
		System.out.println(type + ": " + (System.currentTimeMillis() - start) + "ms");
		// do something with vehicles...
	}
	
	private void executeMultipleQuery() {
		
		for (String type : Vehicle.types) {
			
			Pageable pageable = PageRequest.of(0, 1000, Sort.by("updatedAt").ascending());
			Page<Vehicle> vehicles = null;
			
			do {
				long start = System.currentTimeMillis();
				vehicles = vehicleRepository.findByType(type, pageable);
				
				System.out.println(type + ": " + (System.currentTimeMillis() - start) + "ms");
				
				pageable = pageable.next();
				
			} while (vehicles.hasNext());
			
		}
		
	}

}
