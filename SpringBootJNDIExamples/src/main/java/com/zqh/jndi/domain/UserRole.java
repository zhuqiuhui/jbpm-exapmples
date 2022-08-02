package com.zqh.jndi.domain;

import javax.persistence.*;

@Entity
@Table(name="kpi_user_role")
public class UserRole {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    private int rid;

    private int uid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "UserRole [id=" + id + ", rid=" + rid + ", uid=" + uid + "]";
    }

}
