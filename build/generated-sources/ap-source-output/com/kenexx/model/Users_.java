package com.kenexx.model;

import com.kenexx.model.UserProfile;
import java.util.List;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-02-21T10:12:21")
@StaticMetamodel(Users.class)
public class Users_ { 

    public static volatile SingularAttribute<Users, String> emailAddress;
    public static volatile SingularAttribute<Users, String> name;
    public static volatile SingularAttribute<Users, Boolean> admin;
    public static volatile SingularAttribute<Users, List> files;
    public static volatile SingularAttribute<Users, UserProfile> userprofileid;
    public static volatile SingularAttribute<Users, Long> id;
    public static volatile SingularAttribute<Users, String> title;
    public static volatile SingularAttribute<Users, String> accessToken;
    public static volatile SingularAttribute<Users, String> url;
    public static volatile SingularAttribute<Users, String> refreshToken;

}