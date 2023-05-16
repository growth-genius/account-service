package com.sgyj.accountservice.infra.security;

public record JwtAuthentication(Long accountId, String email) {}
