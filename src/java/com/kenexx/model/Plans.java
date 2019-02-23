/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kenexx.model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@Table(name = "plans")
@XmlRootElement
public class Plans implements Comparable<Plans>, Serializable{

    @Basic(optional = false)
    @Column(name = "plan_status")
    private String planStatus;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "planId")
    private Collection<UserProfile> userprofileCollection;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PLANS_SEQ")
    @SequenceGenerator(name = "PLANS_SEQ", sequenceName = "PLANS_SEQ", allocationSize = 50)
    @Column(name = "plan_id")
    private Long id;
    
    @Column(name = "plan_name")
    private String planName;
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    
    public Plans() {
    }

    public String getPlanStatus() {
        return planStatus;
    }

    public void setPlanStatus(String planStatus) {
        this.planStatus = planStatus;
    }
    
    @Override
    public int compareTo(Plans obj) {
        return this.id.compareTo(obj.getId());
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Plans)) {
            return false;
        }
        Plans other = (Plans) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kenexx.model.Plans[ userId=" + id + " ]";
    }
    
}
