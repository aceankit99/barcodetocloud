package com.kenexx.model;

import com.kenexx.model.UserProfile;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-02-21T10:12:21")
@StaticMetamodel(Plans.class)
public class Plans_ { 

    public static volatile SingularAttribute<Plans, String> planStatus;
    public static volatile SingularAttribute<Plans, String> planName;
    public static volatile SingularAttribute<Plans, Long> id;
    public static volatile CollectionAttribute<Plans, UserProfile> userprofileCollection;

}