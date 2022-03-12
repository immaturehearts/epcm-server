package com.epcm.entity.builder;

import com.epcm.entity.PunchInHistory;

import java.util.Date;

public final class PunchInHistoryBuilder {
    private Long id;
    private Long uid;
    private Integer health;
    private String degree;
    private Integer vaccine;
    private String location;
    private Date gmtModify;
    private Date gmtCreate;

    private PunchInHistoryBuilder() {
    }

    public static PunchInHistoryBuilder aPunchInHistory() {
        return new PunchInHistoryBuilder();
    }

    public PunchInHistoryBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public PunchInHistoryBuilder withUid(Long uid) {
        this.uid = uid;
        return this;
    }

    public PunchInHistoryBuilder withHealth(Integer health) {
        this.health = health;
        return this;
    }

    public PunchInHistoryBuilder withDegree(String degree) {
        this.degree = degree;
        return this;
    }

    public PunchInHistoryBuilder withVaccine(Integer vaccine) {
        this.vaccine = vaccine;
        return this;
    }

    public PunchInHistoryBuilder withLocation(String location) {
        this.location = location;
        return this;
    }

    public PunchInHistoryBuilder withGmtModify(Date gmtModify) {
        this.gmtModify = gmtModify;
        return this;
    }

    public PunchInHistoryBuilder withGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
        return this;
    }

    public PunchInHistory build() {
        PunchInHistory punchInHistory = new PunchInHistory();
        punchInHistory.setId(id);
        punchInHistory.setUid(uid);
        punchInHistory.setHealth(health);
        punchInHistory.setDegree(degree);
        punchInHistory.setVaccine(vaccine);
        punchInHistory.setLocation(location);
        punchInHistory.setGmtModify(gmtModify);
        punchInHistory.setGmtCreate(gmtCreate);
        return punchInHistory;
    }
}
