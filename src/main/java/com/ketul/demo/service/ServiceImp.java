package com.ketul.demo.service;

import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ketul.demo.Repo.UserRepo;
import com.ketul.demo.exception.UserNotFoundException;
import com.ketul.demo.module.User;

@Service
public class ServiceImp implements UserDetailsService {

	@Autowired
	private UserRepo userRepo;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	public ResponseEntity<Object> save(@Valid User user) {
		user.setPass(passwordEncoder.encode(user.getPass()));
		userRepo.save(user);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();
		
		return ResponseEntity.created(location).build();
	}

	public List<User> getAll() {
		
		return userRepo.findAll();
	}

	public User getOne(int id) {
		
		User user = userRepo.findById(id).orElse(null);
		return user;
	}

	public void deleteById(int id) {
		userRepo.deleteById(id);
		
	}

	public User update(int id, User user) {
		User findUser = userRepo.findById(id).orElse(null);
		
		if(findUser == null)
			throw new UserNotFoundException("ID :- " + id + " not found");
		
		findUser.setName(user.getName());
		findUser.setAge(user.getAge());
		findUser.setEmail(user.getEmail());
		findUser.setPass(user.getPass());
		
		return userRepo.saveAndFlush(findUser);
	}

	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepo.findByEmail(email);
		Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        if (user == null) {
            throw new UserNotFoundException("User not authorized.");
        } else {
        	
        	grantedAuthorities.add(new SimpleGrantedAuthority(user.getEmail()));
           
            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPass(), grantedAuthorities);
        }
	}

}
