package com.synclyplatform.synclyprojectbackend.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.security.Principal;

@Getter
@Setter
@AllArgsConstructor
public class UsernamePrincipal implements Principal {
    private final String name;
}
