package com.jakan.incident_service.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "attachments")
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_type")
    private String fileType;

    @Lob
    @Column(name = "file_data", nullable = false)
    private byte[] fileData;

    @Column(name = "uploader_id", nullable = false)
    private Long uploaderId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "upload_date", nullable = false)
    private Date uploadDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "incident_id", nullable = false)
    private Incident incident;
}
