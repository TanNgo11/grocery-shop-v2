package com.thanhtan.groceryshop.service.impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.thanhtan.groceryshop.dto.request.AuthenticationRequest;
import com.thanhtan.groceryshop.dto.request.IntrospectRequest;
import com.thanhtan.groceryshop.dto.request.LogoutRequest;
import com.thanhtan.groceryshop.dto.request.RefreshRequest;
import com.thanhtan.groceryshop.dto.response.AuthenticationResponse;
import com.thanhtan.groceryshop.dto.response.IntrospectResponse;
import com.thanhtan.groceryshop.entity.InvalidatedToken;
import com.thanhtan.groceryshop.entity.RefreshToken;
import com.thanhtan.groceryshop.entity.User;
import com.thanhtan.groceryshop.exception.AppException;
import com.thanhtan.groceryshop.exception.ErrorCode;
import com.thanhtan.groceryshop.repository.InvalidatedTokenRepository;
import com.thanhtan.groceryshop.repository.RefreshTokenRepository;
import com.thanhtan.groceryshop.repository.UserRepository;
import com.thanhtan.groceryshop.service.IAuthenticationService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationService implements IAuthenticationService {

    final UserRepository userRepository;

    final RefreshTokenRepository refreshTokenRepository;

    final InvalidatedTokenRepository invalidatedTokenRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-access-duration}")
    protected long VALID_ACCESS_DURATION;

    @NonFinal
    @Value("${jwt.valid-refresh-duration}")
    protected long VALID_REFRESH_DURATION;


    @Override
    @Transactional
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        var user = userRepository.findByUsername(authenticationRequest.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        boolean authenticated = passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword());

        if (!authenticated) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        var accessToken = generateToken(user, false);
        var refreshToken = generateToken(user, true);


        RefreshToken refreshTokenEntity = new RefreshToken();

        refreshTokenEntity.setToken(refreshToken);
        refreshTokenEntity.setUsername(user.getUsername());
        refreshTokenEntity.setExpiryTime(Date.from(Instant.now().plus(VALID_REFRESH_DURATION, ChronoUnit.SECONDS)));
        refreshTokenRepository.save(refreshTokenEntity);

        return AuthenticationResponse
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .authenticated(true)
                .build();
    }


    @Override
    @Transactional
    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        SignedJWT signedJWT = verifyToken(request.getRefreshToken());

        refreshTokenRepository.deleteByToken(request.getRefreshToken());

        SignedJWT accessSignedJWT = verifyToken(request.getAccessToken());

        String accessTokenId = accessSignedJWT.getJWTClaimsSet().getJWTID();
        Date expiryTime = accessSignedJWT.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(accessTokenId)
                .expiryTime(expiryTime)
                .build();

        invalidatedTokenRepository.save(invalidatedToken);
    }

    @Override
    public String generateToken(User user, boolean isRefreshToken) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        long duration = isRefreshToken ? VALID_REFRESH_DURATION : VALID_ACCESS_DURATION;

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("thanhtan.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(duration, ChronoUnit.SECONDS).toEpochMilli()
                ))
                .claim("customClaim", "custom")
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());


        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("can not create token", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private String resetRefreshToken(User user, long expirationTime, String jwtId) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);


        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("thanhtan.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        expirationTime
                ))
                .claim("customClaim", "custom")
                .jwtID(jwtId)
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());


        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("can not create token", e.getMessage());
            throw new RuntimeException(e);
        }
    }


    @Override
    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();
        boolean isValid = true;
        try {

            verifyToken(token);
        } catch (AppException e) {
            isValid = false;
        }
        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions()))
                    role.getPermissions().forEach(permission -> {
                        stringJoiner.add(permission.getName());
                    });
            });
        }
        System.out.println("roles ne " + stringJoiner.toString());
        return stringJoiner.toString();
    }


    @Override
    @Transactional
    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
        var signedJWT = verifyToken(request.getRefreshToken());

        var jit = signedJWT.getJWTClaimsSet().getJWTID();
        var expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        var username = signedJWT.getJWTClaimsSet().getSubject();

        var user = userRepository.findByUsername(username).orElseThrow(
                () -> new AppException(ErrorCode.UNAUTHENTICATED)
        );
        if(!refreshTokenRepository.existsByToken(request.getRefreshToken()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        refreshTokenRepository.deleteByToken(request.getRefreshToken());

        var refreshToken = resetRefreshToken(user, expiryTime.getTime(), jit);
        RefreshToken refreshTokenEntity = new RefreshToken();


        refreshTokenEntity.setToken(refreshToken);
        refreshTokenEntity.setUsername(user.getUsername());
        refreshTokenEntity.setExpiryTime(expiryTime);
        refreshTokenRepository.save(refreshTokenEntity);


        var accesstoken = generateToken(user, false);

        return AuthenticationResponse.builder()
                .accessToken(accesstoken)
                .refreshToken(refreshToken)
                .authenticated(true)
                .build();
    }


    private SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        String tokenId = signedJWT.getJWTClaimsSet().getJWTID();

        System.out.println("token id " + tokenId);

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);



        if (!(verified && expiryTime.after(new Date())))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        if (invalidatedTokenRepository.existsById(tokenId))
            throw new AppException(ErrorCode.UNAUTHENTICATED);


        return signedJWT;
    }
}
