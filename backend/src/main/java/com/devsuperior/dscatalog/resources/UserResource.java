package com.devsuperior.dscatalog.resources;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.devsuperior.dscatalog.dto.UserDTO;
import com.devsuperior.dscatalog.dto.UserInsertDTO;
import com.devsuperior.dscatalog.dto.UserUpdateDTO;
import com.devsuperior.dscatalog.services.UserService;

@RestController
@RequestMapping(value = "/users")
public class UserResource {
	@Autowired
	private UserService userService;
	
	@GetMapping
	public ResponseEntity<Page<UserDTO>>findAll(Pageable pageable){
	 Page<UserDTO> dto =	userService.findAllPaged(pageable);
	 return ResponseEntity.ok(dto);
		
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<UserDTO>findById(@PathVariable Long id){
		UserDTO dto = userService.findById(id);
		
		return ResponseEntity.ok(dto);
		
	}
	
	@PostMapping
	public ResponseEntity<UserDTO>salvar(@Valid @RequestBody UserInsertDTO dto){
		UserDTO newDto = userService.salvar(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(newDto.getId()).toUri();
		
		 
		return ResponseEntity.created(uri).body(newDto);
				
	}
	
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<UserDTO>atualizar(@Valid @RequestBody UserUpdateDTO dto, @PathVariable Long id){
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(dto.getId()).toUri();
		
		UserDTO newDto = userService.atualizar(dto, id);
		return ResponseEntity.created(uri).body(dto);
				
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void>excluir(@PathVariable Long id){
		userService.excluir(id);
		return ResponseEntity.noContent().build();
	}

}
