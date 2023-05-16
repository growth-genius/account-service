package com.sgyj.accountservice.infra.security;

import static com.sgyj.accountservice.modules.utils.CommonUtil.authorities;

import com.sgyj.accountservice.infra.advice.exceptions.NotFoundException;
import com.sgyj.accountservice.modules.account.AccountService;
import com.sgyj.accountservice.modules.account.dto.AccountDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final AccountService accountService;

    @Override
    public Authentication authenticate ( Authentication authentication ) throws AuthenticationException {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) authentication;
        return processUserAuthentication( String.valueOf( authenticationToken.getPrincipal() ), authenticationToken.getCredentials() );
    }

    private Authentication processUserAuthentication ( String principal, CredentialInfo credential ) {
        try {
            final AccountDto finalAccountDto = accountService.login( principal, credential );
            CredentialInfo credentialInfo = new CredentialInfo( finalAccountDto.getPassword(), finalAccountDto.getLoginType() );
            JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken( new JwtAuthentication( finalAccountDto.getAccountId(),
                                                                                                            finalAccountDto.getEmail() ),
                                                                                     credentialInfo,
                                                                                     authorities( finalAccountDto.getRoles() ) );
            authenticationToken.setDetails( finalAccountDto );
            return authenticationToken;
        } catch ( NotFoundException e ) {
            throw new UsernameNotFoundException( e.getMessage() );
        } catch ( IllegalArgumentException e ) {
            throw new BadCredentialsException( e.getMessage() );
        } catch ( DataAccessException e ) {
            throw new AuthenticationServiceException( e.getMessage(), e );
        }
    }

    @Override
    public boolean supports ( Class<?> authentication ) {
        return authentication.isAssignableFrom( JwtAuthenticationToken.class );
    }
}
