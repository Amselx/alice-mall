package org.alice.data.pojo;

import lombok.Data;

import java.util.Date;


/**
 *
 *
 * @author  Amse
 * @date  19/01/2021 18:03
 * @version 1.0
 */
@Data
public class TokenInfo {

	private boolean active;
	
	private String clientId;
	
	private String[] scope;
	
	private String username;
	
	private String[] aud;
	
	private Date exp;
	
	private String[] authorities;
 	
}
