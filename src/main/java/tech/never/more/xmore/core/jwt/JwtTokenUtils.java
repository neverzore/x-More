package tech.never.more.xmore.core.jwt;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import io.jsonwebtoken.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import tech.never.more.xmore.system.service.IUserService;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Configuration
public class JwtTokenUtils {

    private static final String ISSUER = "http://www.iflower.tech/";
    
    public static final String BEARER = "Bearer ";
    public static final String AUTHORITY = "auth";
    public static final String KEEP = "keep";
    public static final String TOKEN = "token";

    private String secret;

    private SignatureAlgorithm algorithm;

    private SecretKey secretKey;

    @Value("${jwt.expire}")
    private Long duration;

    public JwtTokenUtils(@Value("${jwt.secret}") String secret) throws UnsupportedEncodingException {
        this.secret = secret;
        this.algorithm = SignatureAlgorithm.HS512;
        this.secretKey = new SecretKeySpec(secret.getBytes("UTF-8"), this.algorithm.getJcaName());
    }

    public Claims parseToken(String token) {
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token);
        return claimsJws.getBody();
    }

    public String generateToken(String username, List<GrantedAuthority> authorityList) {
        List<String> auths = convertGrantAuthority2String(authorityList);
        return generateToken(username, auths, true);
    }

    public String generateToken(String username, List<String> authorityList, boolean keep) {
        JwtBuilder builder = Jwts.builder();

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        if (duration >= 0) {
            long expMillis = nowMillis + duration;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        builder
                .setId(new ObjectId().toHexString())
                .setIssuer(ISSUER)
                .setAudience(username)
                .setIssuedAt(now).signWith(this.secretKey, algorithm)
                .claim(AUTHORITY, authorityList)
                .claim(KEEP, keep);

        return builder.compact();
    }

    public List<String> convertGrantAuthority2String(List<GrantedAuthority> authorityList) {
        List<String> auths = new ArrayList<>();
        if (!CollectionUtils.isEmpty(authorityList)) {
            for (GrantedAuthority authority : authorityList) {
                auths.add(authority.getAuthority());
            }
        }

        return auths;
    }

    public List<GrantedAuthority> convert2GrantedAuthority(Object auths) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        if (auths != null) {
            List<String> grantedAuthorities = (List<String>) auths;
            if (!org.springframework.util.CollectionUtils.isEmpty(grantedAuthorities)) {
                for (String role : grantedAuthorities) {
                    authorities.add(new SimpleGrantedAuthority(role));
                }
            }
        }

        return authorities;
    }
}
