package com.hammersmith.cammembercard.model;

import java.io.Serializable;

/**
 * Created by imac on 25/10/17.
 */

public class Model implements Serializable {

    private String itemName;
    private boolean isSelected;

    public Model(String itemName, boolean isSelected) {
        this.itemName = itemName;
        this.isSelected = isSelected;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}