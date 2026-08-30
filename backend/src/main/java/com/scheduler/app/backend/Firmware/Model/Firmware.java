package com.scheduler.app.backend.Firmware.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.scheduler.Base.ModelBase.ModelBase;
import com.scheduler.app.backend.aREST.Models.Board;

// firmware storage
@Entity(name="firmware")
@Table(indexes = @Index(columnList = "version"))
public class Firmware extends ModelBase{
    // version e.g 1.0.0
    @Column
    private String version="";
    // version numbers
    @Column
    private int majorVersion;
    // version cycle
    @Column
    private int minorVersion;
    // patch
    @Column
    private int patchVersion;
    // firmware version is that latest
    @Column
    private boolean latest;
    // require update
    @Column
    private boolean mandatoryUpdate;
    // notes
    @Column
    private String notes="";
    // main program 
    @Column
    private String mainFile="";
    // bootloader
    @Column
    private String bootloaderFile="";
    // merge file
    @Column
    private String mapFile="";
    // partition file
    @Column
    private String partitionFile="";
    // boards that thuse this firmware
    @JsonManagedReference("firmware-board")
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "firmware", cascade =CascadeType.ALL)
    private List<Board> boardUsedFirmware=new ArrayList<>();

    public void createVersion(){
        this.version.trim();
        String [] arr=version.split("\\.");
        if(arr.length==3){
            this.majorVersion=Integer.parseInt(arr[0]);
            this.minorVersion=Integer.parseInt(arr[1]);
            this.patchVersion=Integer.parseInt(arr[2]);
        }
    }
    public boolean validateVersion(){
        boolean valid=false;
        String [] arr=version.trim().split("\\.");
        if(arr.length==3) valid=true;
        return valid;
    }
    @PrePersist
    protected void onCreate(){
        this.mainFile.trim();
        this.bootloaderFile.trim();
        this.partitionFile.trim();
        this.mapFile.trim();
    }

    public Firmware() {
    }

    public Firmware(String version, int majorVersion, int minorVersion, int patchVersion, boolean latest, boolean mandatoryUpdate, String notes, String mainFile, String bootloaderFile, String mapFile, String partitionFile) {
        this.version = version;
        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
        this.patchVersion = patchVersion;
        this.latest = latest;
        this.mandatoryUpdate = mandatoryUpdate;
        this.notes = notes;
        this.mainFile = mainFile;
        this.bootloaderFile = bootloaderFile;
        this.mapFile = mapFile;
        this.partitionFile = partitionFile;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getMajorVersion() {
        return this.majorVersion;
    }

    public void setMajorVersion(int majorVersion) {
        this.majorVersion = majorVersion;
    }

    public int getMinorVersion() {
        return this.minorVersion;
    }

    public void setMinorVersion(int minorVersion) {
        this.minorVersion = minorVersion;
    }

    public int getPatchVersion() {
        return this.patchVersion;
    }

    public void setPatchVersion(int patchVersion) {
        this.patchVersion = patchVersion;
    }

    public boolean isLatest() {
        return this.latest;
    }

    public boolean getLatest() {
        return this.latest;
    }

    public void setLatest(boolean latest) {
        this.latest = latest;
    }

    public boolean isMandatoryUpdate() {
        return this.mandatoryUpdate;
    }

    public boolean getMandatoryUpdate() {
        return this.mandatoryUpdate;
    }

    public void setMandatoryUpdate(boolean mandatoryUpdate) {
        this.mandatoryUpdate = mandatoryUpdate;
    }

    public String getNotes() {
        return this.notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getMainFile() {
        return this.mainFile;
    }

    public void setMainFile(String mainFile) {
        this.mainFile = mainFile;
    }

    public String getBootloaderFile() {
        return this.bootloaderFile;
    }

    public void setBootloaderFile(String bootloaderFile) {
        this.bootloaderFile = bootloaderFile;
    }

    public String getMapFile() {
        return this.mapFile;
    }

    public void setMapFile(String mapFile) {
        this.mapFile = mapFile;
    }

    public String getPartitionFile() {
        return this.partitionFile;
    }

    public void setPartitionFile(String partitionFile) {
        this.partitionFile = partitionFile;
    }

    public Firmware version(String version) {
        setVersion(version);
        return this;
    }

    public Firmware majorVersion(int majorVersion) {
        setMajorVersion(majorVersion);
        return this;
    }

    public Firmware minorVersion(int minorVersion) {
        setMinorVersion(minorVersion);
        return this;
    }

    public Firmware patchVersion(int patchVersion) {
        setPatchVersion(patchVersion);
        return this;
    }

    public Firmware latest(boolean latest) {
        setLatest(latest);
        return this;
    }

    public Firmware mandatoryUpdate(boolean mandatoryUpdate) {
        setMandatoryUpdate(mandatoryUpdate);
        return this;
    }

    public Firmware notes(String notes) {
        setNotes(notes);
        return this;
    }

    public Firmware mainFile(String mainFile) {
        setMainFile(mainFile);
        return this;
    }

    public Firmware bootloaderFile(String bootloaderFile) {
        setBootloaderFile(bootloaderFile);
        return this;
    }

    public Firmware mapFile(String mapFile) {
        setMapFile(mapFile);
        return this;
    }

    public Firmware partitionFile(String partitionFile) {
        setPartitionFile(partitionFile);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Firmware)) {
            return false;
        }
        Firmware firmware = (Firmware) o;
        return Objects.equals(version, firmware.version) && majorVersion == firmware.majorVersion && minorVersion == firmware.minorVersion && patchVersion == firmware.patchVersion && latest == firmware.latest && mandatoryUpdate == firmware.mandatoryUpdate && Objects.equals(notes, firmware.notes) && Objects.equals(mainFile, firmware.mainFile) && Objects.equals(bootloaderFile, firmware.bootloaderFile) && Objects.equals(mapFile, firmware.mapFile) && Objects.equals(partitionFile, firmware.partitionFile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(version, majorVersion, minorVersion, patchVersion, latest, mandatoryUpdate, notes, mainFile, bootloaderFile, mapFile, partitionFile);
    }

    @Override
    public String toString() {
        return "{" +
            " version='" + getVersion() + "'" +
            ", majorVersion='" + getMajorVersion() + "'" +
            ", minorVersion='" + getMinorVersion() + "'" +
            ", patchVersion='" + getPatchVersion() + "'" +
            ", latest='" + isLatest() + "'" +
            ", mandatoryUpdate='" + isMandatoryUpdate() + "'" +
            ", notes='" + getNotes() + "'" +
            ", mainFile='" + getMainFile() + "'" +
            ", bootloaderFile='" + getBootloaderFile() + "'" +
            ", mapFile='" + getMapFile() + "'" +
            ", partitionFile='" + getPartitionFile() + "'" +
            "}";
    }
    
}
