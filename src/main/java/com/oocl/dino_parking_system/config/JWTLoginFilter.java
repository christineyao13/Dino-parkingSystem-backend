package com.oocl.dino_parking_system.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oocl.dino_parking_system.entitie.User;
import com.oocl.dino_parking_system.service.ParkingBoyService;
import com.oocl.dino_parking_system.service.UserService;
import com.oocl.dino_parking_system.util.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.CollationKey;

import static com.oocl.dino_parking_system.constant.Constants.SALT_STRING;

/**
 * Created by Vito Zhuang on 7/31/2018.
 */

public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

	private final UserService userDetailsService;

	private final ParkingBoyService parkingBoyService;

	public JWTLoginFilter(String url, AuthenticationManager authManager, UserService userDetailsService, ParkingBoyService parkingBoyService) {
		super(new AntPathRequestMatcher(url));
		this.userDetailsService = userDetailsService;
		this.parkingBoyService = parkingBoyService;
		setAuthenticationManager(authManager);

	}

	@Override
	public Authentication attemptAuthentication(
			HttpServletRequest req, HttpServletResponse res)
			throws AuthenticationException, IOException, ServletException {
		User creds = new ObjectMapper()
				.readValue(req.getInputStream(), User.class);
		UserDetails userDetails = userDetailsService.loadUserByUsername(creds.getUsername());
		if(userDetails==null) {
			JSONObject cookies = new JSONObject();
			cookies.put("status", false);
			res.addHeader("Cookies", cookies.toJSONString());
			System.out.println(res.getHeader("Cookies"));
			return getAuthenticationManager().authenticate(
					new UsernamePasswordAuthenticationToken(
							"",
							"",
							null
					));
		}else {
			return getAuthenticationManager().authenticate(
					new UsernamePasswordAuthenticationToken(
							creds.getUsername(),
							new PasswordEncoder(SALT_STRING, "MD5")
									.encode(creds.getPassword())
									.substring(0, 7),
							userDetails.getAuthorities()
					)
			);
		}
	}

	@Override
	protected void successfulAuthentication(
			HttpServletRequest req,
			HttpServletResponse res, FilterChain chain,
			Authentication auth) throws IOException, ServletException {
		TokenAuthenticationService.addAuthentication(res, auth.getName(), auth.getAuthorities());
		JSONObject unReadOrderNumJson = parkingBoyService.findUnReadOrderNum(auth.getName());
		String roleName = auth.getAuthorities().toString().replace("[","").replace("]","");
		JSONObject cookieJson = new JSONObject();
		cookieJson.put("id",unReadOrderNumJson.get("parkingBoyId"));
		cookieJson.put("role",roleName);
		res.addHeader("Cookies",cookieJson.toJSONString());
		WebSocketServer.sendInfo(unReadOrderNumJson.toJSONString(), unReadOrderNumJson.get("parkingBoyId").toString());
	}
}
