package com.kg.platform.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PupilTributeLogModel implements Serializable {

    private int count;
    private List<Long> ids;

    public PupilTributeLogModel() {
    }

    public PupilTributeLogModel(int count, List<Long> ids) {
        this.count = count;
        this.ids = ids;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public static PupilTributeLogModel init() {
        return new PupilTributeLogModel(0, new ArrayList<>());
    }

    public static PupilTributeLogModel addCount(PupilTributeLogModel model) {
        int count = model.getCount();
        count++;
        return new PupilTributeLogModel(count, new ArrayList<>());
    }

    public static PupilTributeLogModel addList(PupilTributeLogModel model, List<Long> ids) {
        List<Long> idsList = model.getIds();
        idsList.addAll(ids);
        model.setIds(idsList);
        return model;
    }
}
