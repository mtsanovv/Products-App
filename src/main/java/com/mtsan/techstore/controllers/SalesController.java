package com.mtsan.techstore.controllers;

import com.mtsan.techstore.Rank;
import com.mtsan.techstore.entities.Sale;
import com.mtsan.techstore.entities.User;
import com.mtsan.techstore.exceptions.TechstoreDataException;
import com.mtsan.techstore.repositories.SalesRepository;
import com.mtsan.techstore.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@RestController
public class SalesController {
	@Autowired
	private SalesRepository salesRepository;
	@Autowired
	private UserRepository userRepository;

	//fetching a list of all sales
	@RequestMapping(value = "/sales", method = RequestMethod.GET)
	public ResponseEntity sales(@RequestParam(required = false) String start_date, @RequestParam(required = false) String end_date, @RequestParam(required = false) Long merchant_id, Authentication authentication) throws TechstoreDataException {
		if (salesRepository.count() > 0) {
			String rank = authentication.getAuthorities().toArray()[0].toString();
			if(rank.equals(Rank.Merchant.toString())) {
				//the authenticated user is Merchant, they can only see all their sales
				User merchant = userRepository.getUserByUsername(authentication.getName()).get(0);
				return ResponseEntity.status(HttpStatus.OK).body(merchant.getSales());
			}
			else if(rank.equals(Rank.Administrator.toString())) {
				//the authenticated user is Administrator, they can analyze sales given dates and merchants
				if(merchant_id != null) {
					//we are going to return data for the merchant
					boolean isIdReal = userRepository.existsById(merchant_id);
					if (isIdReal) {
						User merchant = userRepository.findById(merchant_id).get();
						if(merchant.getRank() == Rank.Merchant) {
							if(start_date != null && end_date != null) {
								//the id we need is indeed for a merchant
								//and we also have a start and end date given
								Date startDate = Date.valueOf(start_date);
								Date endDate = Date.valueOf(end_date);

								List<Sale> merchantSales = merchant.getSales();
								for(int i = 0; i < merchantSales.size(); i++) {
									if(merchantSales.get(i).getDateSold().compareTo(startDate) < 0 || merchantSales.get(i).getDateSold().compareTo(endDate) > 0) {
										merchantSales.remove(i);
									}
								}
								return ResponseEntity.status(HttpStatus.OK).body(merchantSales);
							}
							return ResponseEntity.status(HttpStatus.OK).body(merchant.getSales());
						} else {
							throw new TechstoreDataException(HttpServletResponse.SC_NOT_FOUND, "Invalid merchant ID");
						}
					} else {
						throw new TechstoreDataException(HttpServletResponse.SC_NOT_FOUND, "Merchant not found");
					}
				}
				if(start_date != null && end_date != null)
				{
					Date startDate = Date.valueOf(start_date);
					Date endDate = Date.valueOf(end_date);
					List<Sale> sales = salesRepository.getSalesByTimeRange(startDate, endDate);
					if(sales.size() > 0) {
						return ResponseEntity.status(HttpStatus.OK).body(sales);
					} else {
						throw new TechstoreDataException(HttpServletResponse.SC_NOT_FOUND, "No sales found for the given time period");
					}
				}
				return ResponseEntity.status(HttpStatus.OK).body(salesRepository.findAll());
			} else {
				throw new TechstoreDataException(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
			}
		} else {
			throw new TechstoreDataException(HttpServletResponse.SC_NOT_FOUND, "No sales found");
		}
	}
}
