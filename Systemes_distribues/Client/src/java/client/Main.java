/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import javax.ejb.EJB;

/**
 *
 * @author rapha
 */
public class Main
{
    @EJB
    private static SysDistBean.SysDistBeanRemote remote;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println(remote.listerPilotes().length);
    }
}
