package com.ketul.demo.Repo.controller;


import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ketul.demo.Repo.module.AuthenticationResponse;
import com.ketul.demo.exception.UserNotFoundException;
import com.ketul.demo.jwt.JwtUtil;
import com.ketul.demo.module.User;
import com.ketul.demo.module.dto.UserDto;
import com.ketul.demo.service.ServiceImp;



@RestController
public class USerController {

	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private ServiceImp serviceImp;

	@Autowired
	private AuthenticationManager authenticationManager;

	

	@Autowired
	private JwtUtil jwtUtil;

	
	
	@PostMapping("/user")
	public ResponseEntity<Object> save(@RequestBody UserDto userDto) {
		User user = mapper.map(userDto, User.class);
		return serviceImp.save(user);
	}
	
	@GetMapping("/users")
	public List<User> getAll() {
		return serviceImp.getAll();
	}
	
	@GetMapping("/user/{id}")
	public User findOne(@PathVariable int id) {
	
		User user = serviceImp.getOne(id);
		
		if(user == null)
			throw new UserNotFoundException("ID :- " + id + " not found");
		
		return user;
	}
	
	@PutMapping("/user/{id}")
	public User update(@PathVariable int id, @RequestBody UserDto userDto) {
		User user = mapper.map(userDto, User.class);
		return serviceImp.update(id, user);
	}
	
	@DeleteMapping("/user/{id}")
	public void deleteById(@PathVariable int id) {
		serviceImp.deleteById(id);
	}
	
	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody UserDto userDto, HttpServletResponse res) throws Exception {

		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPass())
			);
		}
		catch (BadCredentialsException e) {
			throw new Exception("Incorrect username or password", e);
		}


		final UserDetails userDetails = serviceImp
				.loadUserByUsername(userDto.getEmail());

		final String jwt =  jwtUtil.generateToken(userDetails);
	
//		System.out.println(jwt);
		
		Cookie cookie = new Cookie("token", jwt);
        cookie.setPath("/");
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        
//        res.setHeader("Access-Control-Allow-Credentials", "true");
        res.addCookie(cookie);
		
        if(res != null)
        	System.out.println("Hello res");
        System.out.println(cookie.getValue());
		
        
        
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}
	
	
//	@RequestMapping("/")
//    public ResponseEntity<AuthenticationResponse> loginUser(@RequestBody AuthenticationResponse request, HttpServletResponse res) {
//		AuthenticationResponse response = new AuthenticationResponse("this_is_my_token");
//        Cookie cookie = new Cookie("token", "this_is_my_token");
//        cookie.setPath("/");
//        cookie.setSecure(true);
//        cookie.setHttpOnly(true);
//        res.setHeader("Access-Control-Allow-Credentials", "true");
//        res.addCookie(cookie);
//        return ResponseEntity.ok().body(response);
//    }
}
