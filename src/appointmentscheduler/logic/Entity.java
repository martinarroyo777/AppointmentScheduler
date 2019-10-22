/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointmentscheduler.logic;

/**
 *
 * @author marro
 */
public abstract class Entity {
    private int id;
    private String name;
    private int status; // determines Active/Inactive status (0,1 respectively)
    
    public int getID(){
        return this.id;
    }
    public String getName(){
        return this.name;
    }
    public void setName(String rename){
        this.name = rename;
    }
    public int getStatus(){
        return this.status;
    }
    public void setStatus(int newStat){
        this.status = newStat;
    }
    public boolean isActive(){
        return this.getStatus() == 0;
    }
    
}
