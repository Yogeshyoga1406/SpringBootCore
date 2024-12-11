package com.beautyhub.beautyhub_service.jwt;

import com.coreerp.model.User;

import java.io.Serializable;

public class JwtAuthenticationResponse implements Serializable {

    private static final long serialVersionUID = 1250166508152483573L;

    private  String token;
//    private final UserDetails user;
    private  User user;

    public JwtAuthenticationResponse(String token, User user) {
        this.token = token;
        this.user = user;
    }

    public JwtAuthenticationResponse() {
    }

    public String getToken() {
		return token;
	}

	public User getUser() {
		return user;
	}

	
}
