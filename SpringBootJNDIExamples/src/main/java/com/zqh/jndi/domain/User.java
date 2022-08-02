package com.zqh.jndi.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name="kpi_user")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    private String username;

    private String password;

    private int age;

    private int status;

    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REMOVE}, fetch = FetchType.EAGER)
    @JoinTable(
            name="kpi_user_role",
            joinColumns=@JoinColumn(name="uid",referencedColumnName="id"),
            inverseJoinColumns=@JoinColumn(name="rid")
    )
    @JsonIgnore
    private List<Role> roles = new ArrayList<Role>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Role> getRoles(){
        return roles;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", username=" + username + ", password=" + password + ", age=" + age + ", status="
                + status + "]";
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO Auto-generated method stub
        for(Role role:roles) {
            role.setRolename("ROLE_"+role.getRolename());
            System.out.println(role.getAuthority());
        }
        return roles;
    }

    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return true;
    }

    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return true;
    }
}
