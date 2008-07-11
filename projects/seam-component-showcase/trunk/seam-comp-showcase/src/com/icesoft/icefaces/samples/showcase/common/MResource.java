package com.icesoft.icefaces.samples.showcase.common;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jboss.seam.annotations.Name;

@Entity
@Name("mresource")
@Table(name = "MResource")
public class MResource implements Serializable {
	//pgName,lookUp,pgDoc,pgTutorial
	private Long id;
    private String pgName;
    private String lookup;
    private String pgDoc;
    private String pgTutorial;
    
    //need empty constructor for Entity
    public MResource(){
    	
    }
    
    @Id @GeneratedValue
    public Long getId()
    {
       return id;
    }
    public void setId(Long id)
    {
       this.id = id;
    }
    
    public String getPgName(){
    	return this.pgName;
    }
    public String getLookup(){
    	return this.lookup;
    }
    public String getPgDoc(){
    	return this.pgDoc;
    }
    public String getPgTutorial(){
    	return this.pgTutorial;
    }
    public void setPgName(String spgName){
    	this.pgName=spgName;
    }
    public void setlookup(String lookup){
    	this.lookup=lookup;
    }
    public void setPgDoc(String pgDoc){
    	this.pgDoc = pgDoc;
    }
    public void setPgTutorial(String pgTut){
    	this.pgTutorial=pgTut;
    }
   
    
}
