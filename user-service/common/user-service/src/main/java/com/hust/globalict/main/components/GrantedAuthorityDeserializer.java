package com.hust.globalict.main.components;

import java.io.IOException;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class GrantedAuthorityDeserializer extends JsonDeserializer<GrantedAuthority>{
	@Override
    public GrantedAuthority deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String authority = p.getText();
        return new SimpleGrantedAuthority(authority);
    }
}
