package ro.mpp.dto;

import ro.mpp.utils.IStringifiable;

import java.io.Serializable;

public class EntityDTO implements Serializable, IStringifiable {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
