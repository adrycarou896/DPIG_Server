package com.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import com.util.entrenamiento.Entrenar;
import com.util.eventsserver.IEventsServer;
import com.util.patterns.PatternsManager;
import com.util.smarthings.IPCamera;
import com.util.smarthings.IPCameraRecord;

@Configuration
public class EventsServerConfiguration {
	
	@Autowired
	private PatternsManager patternsManager;
	
	@Bean
	public TaskExecutor createTaskExecutor() {
		return new SimpleAsyncTaskExecutor();
	}
	
	@Bean
	public CommandLineRunner createEventServerRunner(TaskExecutor executor, IEventsServer server) {
		return new CommandLineRunner() {
			@Override
			public void run(String... args) throws Exception {
				executor.execute(server);
			}
		};
	}
	
	
	@Bean
	public CommandLineRunner schedulingRunner(TaskExecutor executor) {
	    return new CommandLineRunner() {
	        public void run(String... args) throws Exception {
	            executor.execute(new Runnable() {
					@Override
					public void run() {
						System.load("C:\\opencv\\build\\java\\x64\\opencv_java400.dll");
						
						Entrenar train = new Entrenar();
						train.run();
						IPCameraRecord ipCameraRecord = new IPCameraRecord(new IPCamera(), train);
						while (true) {
							//Hacerlo todo en servidor
							//hacer pruebas de usuario através de peticiones web a este servidor
							//hacer lo de asignar cámaras de smarthings con las de mi .properties
							
							ipCameraRecord.run();
							
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						
					}
				});
	        }
	    };
	}
	
	
}
