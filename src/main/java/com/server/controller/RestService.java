package com.server.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.server.model.Camera;
import com.server.model.Match;
import com.server.model.Person;
import com.server.model.dto.CameraDTO;
import com.server.model.dto.MatchDTO;
import com.server.model.dto.PersonDTO;
import com.server.repository.CameraRepository;
import com.server.repository.MatchRepository;
import com.server.repository.PersonRepository;

@RestController
@RequestMapping(path = "/sendFrame")//Rura en la que encontramos el servicio
public class RestService{
	
	@Autowired
	private MatchRepository matchRepository;
	
	@Autowired
	private CameraRepository cameraRepository;
	
	@Autowired
	private PersonRepository personRepository;
	
	@RequestMapping(method = RequestMethod.POST, path = "/insertMatch", //dirección del servicio
			consumes = "application/json", produces = "application/json")
	public @ResponseBody Match //Convierte Camara a JSON
	validateUser(@RequestBody MatchDTO matchDto) throws Exception {
		CameraDTO cameraDto = matchDto.getCamera();
		PersonDTO personDto = matchDto.getPerson();
		Date date = matchDto.getDate();
		
		Camera camera = cameraRepository.findByName(cameraDto.getName());
		Person person = personRepository.findByName(personDto.getName());
		Match match = new Match(camera, person, date);
		
		matchRepository.save(match);
		
		return match;
	}
}
