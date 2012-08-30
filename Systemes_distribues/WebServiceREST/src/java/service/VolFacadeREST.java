/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import WebServiceREST.Vol;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;

/**
 *
 * @author rapha
 */
@Stateless
@Path("webservicerest.vol")
public class VolFacadeREST extends AbstractFacade<Vol> {
    @PersistenceContext(unitName = "WebServiceRESTPU")
    private EntityManager em;

    public VolFacadeREST() {
        super(Vol.class);
    }

//    @POST
//    @Override
//    @Consumes({"application/xml", "application/json"})
//    public void create(Vol entity) {
//        super.create(entity);
//    }
    
    // Trouver les vols avec le depart et l'arrivée
    @GET
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<Vol> findVols(@PathParam("from") Integer from, @PathParam("to") Integer to)
    {
        List<Vol> volsAll = this.findAll();
        LinkedList<Vol> vols = new LinkedList<Vol>();
        
        for (Vol v : volsAll) {
            if (v.getAeroportDepart().getIdAeroport() == from.intValue()
                && v.getAeroportArrivee().getIdAeroport() == to.intValue()
            ) {
                vols.add(v);
            }
        }
        
        return vols;
    }
    
    // Trouver les vols avec le depart et l'arrivée avec un status
    @GET
    @Path("{from}/{to}/status")
    @Produces({"application/xml", "application/json"})
    public List<Vol> findVolsStatus(@PathParam("from") Integer from,
        @PathParam("to") Integer to, @QueryParam("landed") Integer landed)
    {
        String status;
        if (landed.intValue() != 0)
            status = "landed";
        else
            status = "envol";
        
        List<Vol> volsAll = this.findVols(from, to);
        LinkedList<Vol> vols = new LinkedList<Vol>();
        
        for (Vol v : volsAll) {
            if (v.getStatus().equals(status)) {
                vols.add(v);
            }
        }
        
        return vols;
    }

    // Changer le status
    @PUT
    @Path("{id}/status/{status}")
    public void edit(@PathParam("id") Integer id,
        /*@QueryParam("landed")*/ @PathParam("status") Integer landed)
    {
        Vol v = this.find(id);
        
        String status;
        if (landed.intValue() != 0)
            status = "landed";
        else
            status = "envol";
        
        v.setStatus(status);
        super.edit(v);
    }

    // Obtenir un vol avec son ID
    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public Vol find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    // Supprimer un vol avec son ID
    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    // Lister tous les vols
    @GET
    @Override
    @Produces({"application/xml", "application/json"})
    public List<Vol> findAll() {
        return super.findAll();
    }

//    @GET
//    @Path("{from}/{to}")
//    @Produces({"application/xml", "application/json"})
//    public List<Vol> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
//        return super.findRange(new int[]{from, to});
//    }
//
//    @GET
//    @Path("count")
//    @Produces("text/plain")
//    public String countREST() {
//        return String.valueOf(super.count());
//    }

    @java.lang.Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
