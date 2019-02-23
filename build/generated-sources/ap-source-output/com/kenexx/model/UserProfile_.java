package com.kenexx.model;

import com.kenexx.model.Plans;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-02-21T10:12:21")
@StaticMetamodel(UserProfile.class)
public class UserProfile_ { 

    public static volatile SingularAttribute<UserProfile, String> password;
    public static volatile SingularAttribute<UserProfile, String> address;
    public static volatile SingularAttribute<UserProfile, String> gender;
    public static volatile SingularAttribute<UserProfile, String> phone;
    public static volatile SingularAttribute<UserProfile, String> forgetPasswordCode;
    public static volatile SingularAttribute<UserProfile, Plans> planId;
    public static volatile SingularAttribute<UserProfile, String> forgetPasswordRequest;
    public static volatile SingularAttribute<UserProfile, Long> id;
    public static volatile SingularAttribute<UserProfile, String> userName;
    public static volatile SingularAttribute<UserProfile, String> email;

}