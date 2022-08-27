package com.example.external.ldap.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.ldap.LdapBindAuthenticationManagerFactory;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.userdetails.PersonContextMapper;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguation {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((authz) -> authz.anyRequest().authenticated()).formLogin();
		
		return http.build();
	}

	@Bean
	public DefaultSpringSecurityContextSource defaultSpringSecurityContextSource() {

		var contextSourceFromProviderUrl = new DefaultSpringSecurityContextSource(
				"ldap://localhost:10389/dc=example,dc=com");
		contextSourceFromProviderUrl.setUserDn("login");
		contextSourceFromProviderUrl.setPassword("password");
		return contextSourceFromProviderUrl;
	}

	@Bean
	AuthenticationManager ldapAuthenticationManager(BaseLdapPathContextSource contextSource) {
		LdapBindAuthenticationManagerFactory factory = new LdapBindAuthenticationManagerFactory(contextSource);
		factory.setUserDnPatterns("uid={0},ou=people");
		factory.setUserDetailsContextMapper(new PersonContextMapper());
		return factory.createAuthenticationManager();
	}
	
//	https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/ldap.html
//	@Bean
//	AuthenticationManager authenticationManager(BaseLdapPathContextSource contextSource) {
//		LdapPasswordComparisonAuthenticationManagerFactory factory = new LdapPasswordComparisonAuthenticationManagerFactory(
//				contextSource, new BCryptPasswordEncoder());
//		factory.setUserDnPatterns("uid={0},ou=people");
//		factory.setPasswordAttribute("userPassword");  
//		return factory.createAuthenticationManager();
//	}
	
//	@Bean
//	LdapAuthoritiesPopulator authorities(BaseLdapPathContextSource contextSource) {
//		String groupSearchBase = "";
//		DefaultLdapAuthoritiesPopulator authorities =
//			new DefaultLdapAuthoritiesPopulator(contextSource, groupSearchBase);
//		authorities.setGroupSearchFilter("member={0}");
//		return authorities;
//	}
//
//	@Bean
//	AuthenticationManager authenticationManager(BaseLdapPathContextSource contextSource, LdapAuthoritiesPopulator authorities) {
//		LdapBindAuthenticationManagerFactory factory = new LdapBindAuthenticationManagerFactory(contextSource);
//		factory.setUserDnPatterns("uid={0},ou=people");
//		factory.setLdapAuthoritiesPopulator(authorities);
//		return factory.createAuthenticationManager();
//	}
}
