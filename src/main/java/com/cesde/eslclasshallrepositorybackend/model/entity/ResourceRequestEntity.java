package com.cesde.eslclasshallrepositorybackend.model.entity;

import com.cesde.eslclasshallrepositorybackend.model.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.cesde.eslclasshallrepositorybackend.model.enums.RequestStatus;
import com.cesde.eslclasshallrepositorybackend.model.enums.RequestType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "requests_resources")
public class ResourceRequestEntity extends BaseEntity {

    public ResourceRequestEntity(RequestType requestType, String detailsJson, User requestedBy,
            Resource targetResource) {
        this.requestType = requestType;
        this.detailsJson = detailsJson;
        this.requestedBy = requestedBy;
        this.targetResource = targetResource;
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private RequestType requestType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private RequestStatus status = RequestStatus.PENDING;

    @Column(length = 4000)
    private String detailsJson;

    @ManyToOne(optional = false)
    @JoinColumn(name = "requested_by_id", nullable = false)
    @JsonIgnoreProperties({ "profile", "requests", "createdResources" })
    private User requestedBy;

    @ManyToOne
    @JoinColumn(name = "approved_by_id")
    @JsonIgnoreProperties({ "profile", "requests", "createdResources" })
    private User approvedBy;

    @ManyToOne
    @JoinColumn(name = "target_resource_id")
    @JsonIgnoreProperties({ "courses" })
    private Resource targetResource;
}

