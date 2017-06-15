package org.akshara.model;

import java.util.HashMap;


public class UserModel<T> {
    private HashMap<T, T> template_data;
    private HashMap<T, T> label_single_data;
    private HashMap<T, T> label_multiple_data;
    private HashMap<T, T> label_order;

    public HashMap<T, T> getTemplate_data() {
        return template_data;
    }

    public void setTemplate_data(HashMap<T, T> template_data) {
        this.template_data = template_data;
    }

    public HashMap<T, T> getLabel_single_data() {
        return label_single_data;
    }

    public void setLabel_single_data(HashMap<T, T> label_single_data) {
        this.label_single_data = label_single_data;
    }

    public HashMap<T, T> getLabel_multiple_data() {
        return label_multiple_data;
    }

    public void setLabel_multiple_data(HashMap<T, T> label_multiple_data) {
        this.label_multiple_data = label_multiple_data;
    }

    public HashMap<T, T> getLabel_order() {
        return label_order;
    }

    public void setLabel_order(HashMap<T, T> label_order) {
        this.label_order = label_order;
    }
}
