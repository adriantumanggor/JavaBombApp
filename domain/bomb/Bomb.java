/*
 * Copyright (c) 1997, Oracle and/or its affiliates. All rights reserved.
 * 
 * This code follows Java Code Conventions as specified in:
 * Java Code Conventions, September 12, 1997
 */

package domain.bomb;

import domain.bomb.formatting.BombStateFormatter;
import domain.bomb.validation.BombValidator;
import java.util.UUID;

public abstract class Bomb {
    
    private final String id;
    private String name;
    private String location;
    private boolean active;
    private final BombType type;
    
    protected Bomb(String name, String location, BombType type) {
        this.id = UUID.randomUUID().toString();
        BombValidator.validateName(name);
        BombValidator.validateLocation(location);
        BombValidator.validateType(type);
        
        this.name = name;
        this.location = location;
        this.type = type;
        this.active = false;
    }
    
    public String getId() { 
        return id; 
    }
    
    public String getName() { 
        return name; 
    }
    
    public String getLocation() {
        return location; 
    }
    
    public boolean isActive() { 
        return active;
    }
    
    public BombType getType() { 
        return type; 
    }
    
    public void setName(String name) {
        BombValidator.validateName(name);
        this.name = name;
    }
    
    public void setLocation(String location) {
        BombValidator.validateLocation(location);
        this.location = location;
    }
    
    public void activate() { 
        this.active = true; 
    }
    
    public void deactivate() { 
        this.active = false; 
    }
    
    public String getCurrentState() {
        BombState state = new BombState(name, location, active, type);
        return BombStateFormatter.formatCurrentState(state);
    }
    
    public abstract String explode();
}