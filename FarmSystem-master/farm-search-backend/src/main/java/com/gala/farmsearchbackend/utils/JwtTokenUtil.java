package com.gala.farmsearchbackend.utils;

import com.gala.farmsearchbackend.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 */
@Component
public class JwtTokenUtil {

    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;

    public JwtTokenUtil(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
        this.secretKey = Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes());
    }

    /**
     * 生成管理员令牌
     */
    public String generateAdminToken(Integer adminId, String adminName, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userType", "admin");
        claims.put("username", adminName);
        claims.put("role", role); // 添加角色信息
        return generateToken(claims, adminId.toString(), jwtConfig.getExpiration());
    }

    /**
     * 生成节点企业令牌
     */
    public String generateNodeToken(Integer nodeId, String nodeName, Integer nodeType) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userType", getNodeTypeString(nodeType));
        claims.put("enterpriseType", nodeType);
        claims.put("username", nodeName);
        claims.put("role", "ENTERPRISE");
        return generateToken(claims, nodeId.toString(), jwtConfig.getExpiration());
    }

    /**
     * 生成刷新令牌
     */
    public String generateRefreshToken(Integer userId, String userType) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userType", userType);
        claims.put("tokenType", "refresh");
        return generateToken(claims, userId.toString(), jwtConfig.getRefreshExpiration());
    }

    private String generateToken(Map<String, Object> claims, String subject, long expiration) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey)
                .compact();
    }

    public Integer getUserIdFromToken(String token) {
        String subject = getClaimsFromToken(token).getSubject();
        return Integer.parseInt(subject);
    }

    public String getUserTypeFromToken(String token) {
        return getClaimsFromToken(token).get("userType", String.class);
    }

    public Integer getEnterpriseTypeFromToken(String token) {
        return getClaimsFromToken(token).get("enterpriseType", Integer.class);
    }

    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).get("username", String.class);
    }

    public String getTokenTypeFromToken(String token) {
        return getClaimsFromToken(token).get("tokenType", String.class);
    }

    /**
     * 从token中获取角色信息
     */
    public String getRoleFromToken(String token) {
        return getClaimsFromToken(token).get("role", String.class);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimsFromToken(token).getExpiration();
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public boolean isRefreshToken(String token) {
        return "refresh".equals(getTokenTypeFromToken(token));
    }

    private String getNodeTypeString(Integer nodeType) {
        switch (nodeType) {
            case 1: return "farm";
            case 2: return "slaughter";
            case 3: return "wholesaler";
            case 4: return "retailer";
            default: return "unknown";
        }
    }
}