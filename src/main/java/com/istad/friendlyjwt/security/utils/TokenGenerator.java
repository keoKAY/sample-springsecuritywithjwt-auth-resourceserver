package com.istad.friendlyjwt.security.utils;

import com.istad.friendlyjwt.model.TokenDto;
import com.istad.friendlyjwt.security.UserDetailImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
public class TokenGenerator {
    @Autowired
    JwtEncoder accessTokenEncoder;
    @Autowired
    @Qualifier("jwtRefreshTokenEncoder")
    JwtEncoder refreshTokenEncoder;

    // access token
    public String createAccessToken(Authentication authentication){
        UserDetailImpl user = (UserDetailImpl) authentication.getPrincipal();
        Instant now = Instant.now();
        // id, username, password, role
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer("something@gmail.com")
                .issuedAt(now)
                .expiresAt(now.plus(5, ChronoUnit.MINUTES))
                .subject(user.getUsername())
                .build();
        return accessTokenEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }
    // refresh token
    public String createRefreshToken(Authentication authentication){
        UserDetailImpl user = (UserDetailImpl) authentication.getPrincipal();
        Instant now = Instant.now();

        JwtClaimsSet claimSet  = JwtClaimsSet.builder()
                .issuer("something@gmail.com")
                .issuedAt(now)
                .expiresAt(now.plus(30,ChronoUnit.DAYS))
                .subject(user.getUsername())
                .build();
        return refreshTokenEncoder.encode(JwtEncoderParameters.from(claimSet)).getTokenValue();
    }

   public TokenDto tokenResponse( Authentication authentication){
       if (!(authentication.getPrincipal() instanceof UserDetailImpl user)) {
           throw new BadCredentialsException(
                   MessageFormat.format("principle {0} is not of User type ", authentication.getPrincipal().getClass())
           );
       }

       TokenDto tokenDto = new TokenDto();
       tokenDto.setUserId(user.getId());
       // create an access token
       tokenDto.setAccessToken(createAccessToken(authentication));
       String refreshToken;
       if (authentication.getCredentials() instanceof Jwt jwt) {
           Instant now = Instant.now();
           Instant expireAt = jwt.getExpiresAt();
           Duration duration = Duration.between(now, expireAt);

           long daysUntilExpired = duration.toDays();
           if (daysUntilExpired < 7) {
               refreshToken = createRefreshToken(authentication);
           } else {
               refreshToken = jwt.getTokenValue();
           }
       } else {
           refreshToken = createRefreshToken(authentication);
       }

       tokenDto.setRefreshToken(refreshToken);

       return tokenDto;
   }


}
