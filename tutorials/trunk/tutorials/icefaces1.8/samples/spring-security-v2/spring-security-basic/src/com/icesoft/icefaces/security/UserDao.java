package com.icesoft.icefaces.security;

public interface UserDao {
    AppUser findUser(String username);
}
