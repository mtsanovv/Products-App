package com.mtsan.techstore.controllers;

import com.mtsan.techstore.entities.Client;
import com.mtsan.techstore.entities.User;
import com.mtsan.techstore.exceptions.TechstoreDataException;
import com.mtsan.techstore.repositories.ClientRepository;
import com.mtsan.techstore.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class ClientsController {

	@Autowired
	private ClientRepository clientRepository;
	@Autowired
	private UserRepository userRepository;

	//fetching a list of all clients per merchant
	@RequestMapping(value = "/clients", method = RequestMethod.GET)
	public ResponseEntity clients(Authentication authentication) throws TechstoreDataException {
		User merchant = userRepository.getUserByUsername(authentication.getName()).get(0);
		if (merchant.getClients().size() > 0) {
			return ResponseEntity.status(HttpStatus.OK).body(merchant.getClients());
		} else {
			throw new TechstoreDataException(HttpServletResponse.SC_NOT_FOUND, "No clients found");
		}
	}

	//adding a client
	@RequestMapping(value = "/clients", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ResponseEntity addClient(@RequestBody Client postedClient, Authentication authentication) throws TechstoreDataException {
		User merchant = userRepository.getUserByUsername(authentication.getName()).get(0);
		Matcher clientNameMatcher = Pattern.compile(Client.namePattern).matcher(postedClient.getName());

		if(!clientNameMatcher.matches()) {
			throw new TechstoreDataException(HttpServletResponse.SC_BAD_REQUEST, "Client name must be between 1 and 1024 characters.");
		}

		postedClient.setMerchant(merchant);
		Client savedClient = clientRepository.save(postedClient);

		return ResponseEntity.status(HttpStatus.CREATED).body(savedClient);
	}

	//fetching a client
	@RequestMapping(value = "/clients/{clientId}", method = RequestMethod.GET)
	public ResponseEntity getClient(@PathVariable Long clientId, Authentication authentication) throws TechstoreDataException {
		User merchant = userRepository.getUserByUsername(authentication.getName()).get(0);
		List<Client> clients = merchant.getClients();
		if (clients.size() > 0) {
			boolean isIdReal = clientRepository.existsById(clientId);
			if (isIdReal) {
				for (Client client : clients) {
					if (client.getId().equals(clientId)) {
						return ResponseEntity.status(HttpStatus.OK).body(client);
					}
				}
			}
			throw new TechstoreDataException(HttpServletResponse.SC_NOT_FOUND, "Client not found");
		} else {
			throw new TechstoreDataException(HttpServletResponse.SC_NOT_FOUND, "No clients found");
		}
	}

	//editing a client
	@RequestMapping(value = "/clients/{clientId}", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.PUT)
	public ResponseEntity editClient(@RequestBody Client newClient, @PathVariable Long clientId, Authentication authentication) throws TechstoreDataException {
		User merchant = userRepository.getUserByUsername(authentication.getName()).get(0);
		List<Client> clients = merchant.getClients();
		if (clients.size() > 0) {
			boolean isIdReal = clientRepository.existsById(clientId);
			if (isIdReal) {
				for (Client client : clients) {
					if (client.getId().equals(clientId)) {
						Matcher clientNameMatcher = Pattern.compile(Client.namePattern).matcher(newClient.getName());
						if(!clientNameMatcher.matches()) {
							throw new TechstoreDataException(HttpServletResponse.SC_BAD_REQUEST, "Client name must be between 1 and 1024 characters.");
						}

						newClient.setId(clientId);
						newClient.setMerchant(merchant);
						Client savedClient = clientRepository.save(newClient);
						return ResponseEntity.status(HttpStatus.OK).body(savedClient);
					}
				}
			}
			throw new TechstoreDataException(HttpServletResponse.SC_NOT_FOUND, "Client not found");
		} else {
			throw new TechstoreDataException(HttpServletResponse.SC_NOT_FOUND, "No clients found");
		}
	}

	//deleting a client
	@RequestMapping(value = "/clients/{clientId}", method = RequestMethod.DELETE)
	public ResponseEntity deleteClient(@PathVariable Long clientId, Authentication authentication) throws TechstoreDataException {
		User merchant = userRepository.getUserByUsername(authentication.getName()).get(0);
		List<Client> clients = merchant.getClients();
		if (clients.size() > 0) {
			boolean isIdReal = clientRepository.existsById(clientId);
			if (isIdReal) {
				for (Client client : clients) {
					if (client.getId().equals(clientId)) {
						clientRepository.deleteById(clientId);
						return ResponseEntity.status(HttpStatus.OK).build();
					}
				}
			}
			throw new TechstoreDataException(HttpServletResponse.SC_NOT_FOUND, "Client not found");
		} else {
			throw new TechstoreDataException(HttpServletResponse.SC_NOT_FOUND, "No clients found");
		}
	}
}
