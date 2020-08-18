package com.joker.webchatting.springboot.config.auth;
import com.joker.webchatting.springboot.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomUserOAuth2UserService customUserOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                .antMatchers("/", "/css/**", "/images/**",
                        "/js/**", "/h2-console/**").permitAll()
                .antMatchers("/api/v1/**").hasRole(Role.USER.name())
                .anyRequest().authenticated()
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .and()
                .oauth2Login()
                .userInfoEndpoint()
                .userService(customUserOAuth2UserService);
        super.configure(http);
    }


/*
    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        List<ClientRegistration> clientRegistrations = new ArrayList<>();
        String DEFAULT_LOGIN_REDIRECT_URL = "{baseUrl}/login/oauth2/code/{registrationId}";
        ClientRegistration.Builder lineBuilder = ClientRegistration.withRegistrationId("google");
        lineBuilder.clientAuthenticationMethod(ClientAuthenticationMethod.POST);
        lineBuilder.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE);
        lineBuilder.redirectUriTemplate(DEFAULT_LOGIN_REDIRECT_URL);
        lineBuilder.authorizationUri("https://access.line.me/oauth2/v2.1/authorize");
        lineBuilder.tokenUri("https://api.line.me/oauth2/v2.1/token");
        lineBuilder.clientName("google");
        lineBuilder.scope("profile", "openid");
        lineBuilder.clientId("502867052711-fgo01t4ge4c1hdg4vu1bc99iaffub2fg.apps.googleusercontent.com");
        lineBuilder.clientSecret("dbW2ACCpIh46qC8yJwa5cHKJ");
        clientRegistrations.add(lineBuilder.build());
        return new InMemoryClientRegistrationRepository(clientRegistrations);
    }
*/
}