package org.gooru.nucleus.handlers.libraries.processors.repositories.activejdbc.entities;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

/**
 * @author szgooru Created On: 29-May-2017
 */
@Table("tenant")
public class AJEntityTenant extends Model {

    public static final String ID = "id";
    
    public static final String SELECT_GLOBAL_AND_DISCOVERABLE_TENANTS =
        "SELECT id FROM tenant WHERE content_visibility != 'tenant'";
}
