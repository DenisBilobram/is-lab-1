package is.lab1.is_lab1.service;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import is.lab1.is_lab1.model.IsUser;
import lombok.Getter;

public class IsUserDetails implements UserDetails {

    @Getter
    private IsUser isUser;
    private Collection<? extends GrantedAuthority> authorities;

    public IsUserDetails(IsUser isUser, Collection<? extends GrantedAuthority> authorities) {
        this.isUser = isUser;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return isUser.getPassword();
    }

    @Override
    public String getUsername() {
        return isUser.getUsername();
    }

}
