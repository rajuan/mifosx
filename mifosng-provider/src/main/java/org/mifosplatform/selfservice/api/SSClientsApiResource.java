/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifosplatform.selfservice.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.mifosplatform.portfolio.client.api.ClientsApiResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

@Path("/self/clients")
@Component
@Scope("singleton")
public class SSClientsApiResource{

    private final ClientsApiResource clientApiResource;

    @Autowired
    public SSClientsApiResource(ClientsApiResource clientApiResource) {
        this.clientApiResource = clientApiResource;
    }

    @GET
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String retrieveAll(@Context final UriInfo uriInfo, @QueryParam("sqlSearch") final String sqlSearch,
            @QueryParam("officeId") final Long officeId, @QueryParam("externalId") final String externalId,
            @QueryParam("displayName") final String displayName, @QueryParam("firstName") final String firstname,
            @QueryParam("lastName") final String lastname, @QueryParam("underHierarchy") final String hierarchy,
            @QueryParam("offset") final Integer offset, @QueryParam("limit") final Integer limit,
            @QueryParam("orderBy") final String orderBy, @QueryParam("sortOrder") final String sortOrder,
            @QueryParam("orphansOnly") final Boolean orphansOnly) {


        return this.clientApiResource.retrieveAll(uriInfo, sqlSearch, officeId, externalId, displayName, firstname, lastname, 
        		hierarchy, offset, limit, orderBy, sortOrder, orphansOnly);
    }

    @GET
    @Path("{clientId}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String retrieveOne(@PathParam("clientId") final Long clientId, @Context final UriInfo uriInfo,
            @DefaultValue("false") @QueryParam("staffInSelectedOfficeOnly") final boolean staffInSelectedOfficeOnly) {

    	System.out.println("SSClientsApiResource.retrieveOne():clientId:"+clientId);
    	if(clientId == 2){
    		System.out.println("SSClientsApiResource.retrieveOne():throwing exception");
    		throw new BadCredentialsException("Just like that");
    	}
    	System.out.println("SSClientsApiResource.retrieveOne():still working");
        return this.clientApiResource.retrieveOne(clientId, uriInfo, staffInSelectedOfficeOnly);
    }

}