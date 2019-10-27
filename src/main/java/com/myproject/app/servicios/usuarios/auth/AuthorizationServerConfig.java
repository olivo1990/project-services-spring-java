package com.myproject.app.servicios.usuarios.auth;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/* 2)
 * 
 * Se encarga de todo el proceso de autenticacion por el lado de oauth2
 * Configura el token y del JWT (Json Web Token) desde el procesdo de login
 * AuthorizationServerConfigurerAdapter, sirve para implementar algunos metodos
 * 
 * */

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter{

	/*@Autowired
	@Qualifier("passwordEncoderMyProject")
	private BCryptPasswordEncoder passwordEncoder;*/
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	/*
	 * - Se inyecta el atributo  authenticationManager de la clase SpringSecurityConfig
	 * 
	 * */
	@Autowired
	@Qualifier("authenticationManager")
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private InfoAdicionalToken infoAdicionalToken;
	
	/*
	 * - Configura los permisos a los ENDPOINT de los sevicios con Spring Security OAuth2
	 * - tokenKeyAccess, el metodo da el pemiso al ENDPOINT a todos para autenticarse (/oauth/token/). Login
	 * - tokenKeyAccess, genera el token
	 * - checkTokenAccess, valida el token que se envia
	 * 
	 * */
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.tokenKeyAccess("permitAll()")
		.checkTokenAccess("isAuthenticated()");
	}
	
	/*
	 * - Registra la aplicacion que quiera acceder a los servicios.
	 * - Autentica la aplicacion del FRONTEND
	 * - Da el permiso de de lectura y escritura a la aplicación (scope)
	 * - Devuelve el "token de acceso" por medio de authorizedGrantTypes y el password
	 * - Valida el tiempo del token de acceso en segundos (accessTokenValiditySecond y refreshTokenValiditySeconds)
	 * 
	 * */

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory().withClient("angularapp")
		.secret(passwordEncoder.encode("12345"))
		.scopes("read", "write")
		.authorizedGrantTypes("password", "refresh_token")
		.accessTokenValiditySeconds(3600)
		.refreshTokenValiditySeconds(3600);
	}

	
	/*
	 * - Se encarga de todo el proceso de autenticación
	 * - Valida el token
	 * */
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		tokenEnhancerChain.setTokenEnhancers(Arrays.asList(infoAdicionalToken, accessTokenConverter()));
		
		/*
		 * - Registra el authenticationManager
		 * - accessTokenConverter, registra los datos del usuario (usuario, password, roles)
		 * - Se valida el usuario y genera el token
		 * */
		endpoints.authenticationManager(authenticationManager)
		.tokenStore(tokenStore())
		.accessTokenConverter(accessTokenConverter())
		.tokenEnhancer(tokenEnhancerChain);
	}
	
	@Bean
	public JwtTokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}
	
	/*
	 * - Genera el token
	 * - setSigningKey, guarda la clave secreta y la cifra con HMACSHA256
	 * 
	 * */
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
		jwtAccessTokenConverter.setSigningKey("234234hj4g2h423g4h23h423p");
		//jwtAccessTokenConverter.setVerifierKey(JwtConfig.RSA_PUBLICA);
		return jwtAccessTokenConverter;
	}
	

}
