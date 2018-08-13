package com.oocl.dino_parking_system.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

/**
 * Created by Vito Zhuang on 7/31/2018.
 */
class TokenAuthenticationService {
	static final long EXPIRATIONTIME = 1000*60*60*1; // 1 hour
	static final String SECRET = "DINO_SECREY_KEY";
	static final String HEADER_STRING = "Authorization";

	static void addAuthentication(HttpServletResponse res, String username, Collection<? extends GrantedAuthority> roles) {
		Map<String,Object> data = new HashMap<>();
		data.put("username",username);
		data.put("roles",roles.toString());
		String JWT = Jwts.builder()
				.setClaims(data)
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
				.signWith(SignatureAlgorithm.HS512, SECRET)
				.compact();
		res.addHeader(HEADER_STRING, JWT);
	}

	static Authentication getAuthentication(HttpServletRequest request) {
		String token = request.getHeader(HEADER_STRING);
		if (token != null) {
			// parse the token.
			Claims claims;
			String user="";
			List<String> rolesName = new LinkedList<>();
			try {
				claims = Jwts.parser()
						.setSigningKey(SECRET)
						.parseClaimsJws(token)
						.getBody();
				user = claims.get("username").toString();
				String rolesString = claims.get("roles").toString();
				rolesName = Arrays.asList(rolesString.substring(1,rolesString.length()-1).split(", "));
			} catch (Exception e) {
				claims = null;
			}
			return !user.equals("") ?
					new UsernamePasswordAuthenticationToken(user,
							null,
							rolesName.stream()
									.map(SimpleGrantedAuthority::new)
									.collect(Collectors.toList())) :
					null;
		}
		return null;
	}
}